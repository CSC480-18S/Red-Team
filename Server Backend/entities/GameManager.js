'use strict'
/**
 * Imports files
 */
const Gameboard = require('./Gameboard')
const PlayerManager = require('./PlayerManager')
const FrontendManager = require('./FrontendManager')
const sc = require('../helpers/ScoreCalculator')
const rh = require('../helpers/ResponseHandler')
const ex = require('../helpers/Extractor')
const dg = require('../helpers/Debug')(true)
const mg = require('../helpers/MacGrabber')
const db = require('../helpers/DB')

let timer

module.exports = (io) => {
  class GameManager {
    constructor() {
      this._gameBoard = new Gameboard()
      this._playerManagers = []
      this._frontendManagers = []
      this._greenScore = 0
      this._goldScore = 0
      this._swaps = 0
      this._currentPlayers = 0
      this._macs = []
      this.init()
    }

    /**
     * Board getter
     */
    get board() {
      return this._gameBoard
    }

    /**
     * Current client amount getters
     */
    get currentPlayers() {
      return this._currentPlayers
    }

    init() {
      dg('game manager created', 'debug')
      this.createPlayerManagers()
      this.listenForClients()
    }

    /**
     * Creates 4 player managers
     */
    createPlayerManagers() {
      dg(`creating 4 player managers`, 'debug')
      for (let i = 0; i < 4; i++) {
        let player = new PlayerManager(i, this)
        player.init()
        this._playerManagers.push(player)
        this._playerManagers[0].isTurn = true
      }
      dg('player managers created', 'debug')
    }

    /**
     * Creates only one frontend manager instance
     */
    createFrontendManager(socket) {
      let manager = new FrontendManager(socket)
      this._frontendManagers.push(manager)
      dg('frontend added', 'debug')
    }

    /**
     * Listens to socket events that happen
     */
    listenForClients() {
      io.on('connection', socket => {
        socket.emit('whoAreYou')

        socket.on('whoAreYou', response => {
          dg('asking client who they are', 'debug')
          this.determineClientType(socket, response)
        })

        socket.on('disconnect', () => {
          for (let i = 0; i < this._macs.length; i++) {
            if (this._macs[i] === socket.mac) {
              this._macs.splice(i, 1)
              break
            }
          }

          this.findClientThatLeft(socket.id)
        })
      })
    }

    /**
     * Determines what kind of client has connected to the server
     * @param {Object} socket - socket object
     * @param {Object} response - the type of the player
     */
    determineClientType(socket, response) {
      if (response.isAI) {
        this.addClientToManager('ai_test', 'Green', null, true, socket)
        dg('ai connected', 'info')
      } else if (response.isSF) {
        this.createFrontendManager(socket)
        dg('a server frontend connected', 'info')
      } else if (response.isClient) {
        mg(socket.handshake.address, (mac) => {
          if (this.checkIfPlayingAlready(socket, mac)) {
            return
          }
          this.checkUserInDatabase(socket, mac)
        })

        dg('client connected', 'info')
      } else if (response.isQueued) {
        this.emitCurrentPlayerCount()
        dg('queued player connected', 'info')
      }
    }

    /**
     * Checks to see if the user actually exists in the DB
     * @param {Object} socket - socket object
     * @param {String} mac - mac address
     */
    checkUserInDatabase(socket, mac) {
      db.checkIfUserExists(mac)
        .then(r => {
          if (db.pruneResults(r)) {
            socket.mac = mac
            this._macs.push(mac)

            db.getTeamURL(mac)
              .then(r2 => {
                let user = {
                  username: r[0].username,
                  team: r2 === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green',
                  link: r[0]._links.self.href
                }

                this.addClientToManager(user.username, user.team, user.link, false, socket)
              })
          } else {
            this.emitError(socket, 'Please login/register first.')
          }
        })
        .catch(e => {
          console.log(e)
        })
    }

    /**
     * Checks to see if the user is playing in more than one instance on their device
     * @param {Object} socket - socket object
     * @param {String} mac - mac address
     */
    checkIfPlayingAlready(socket, mac) {
      for (let i = 0; i < this._macs.length; i++) {
        if (this._macs[i] === mac) {
          this.emitError(socket, 'You can only have one game instance running.')
          return true
        }
      }
    }

    /**
     * Sends out the current players in the game
     */
    emitCurrentPlayerCount() {
      io.emit('currentPlayerCount', {
        amount: this._currentPlayers
      })
    }

    /**
     * Adds a client to a PlayerManager that does not have any data inside of it
     * @param {String} name - name of player
     * @param {String} team - team player is on
     * @param {String} link - link to player in DB
     * @param {Boolean} isAI - AI or not
     * @param {Object} socket - socket object
     */
    addClientToManager(name, team, link, isAI, socket) {
      console.log('DEBUG: FINDING MANAGER TO ADD TO')
      for (let manager of this._playerManagers) {
        if (manager.id === null) {
          manager.createHandshakeWithClient(name, team, link, isAI, socket, {yellow: this._goldScore, green: this._greenScore})
          this.emitGameEvent(`${manager.name} entered the game.`)
          this.updateFrontendData()
          if (isAI) {
            dg(`ai added to --> player manager ${manager.position}`, 'debug')
          } else {
            dg(`client added to --> player manager ${manager.position}`, 'debug')
            this._currentPlayers++
            this.emitCurrentPlayerCount()
          }

          return
        }
      }

      if (!this.attemptAIRemoval(name, team, link, isAI, socket)) {
        dg('there are already max players connected', 'error')
        this.emitError(socket, 'There are already 4 players connected to the game.')
      }
    }

    /**
     * Attempts to remove an AI if they are in the game
     * @param {String} name - name of player
     * @param {String} team - team player is on
     * @param {String} link - link to player in DB
     * @param {Boolean} isAI - AI or not
     * @param {Object} socket - socket object
     */
    attemptAIRemoval(name, team, link, isAI, socket) {
      if (!isAI) {
        for (let manager of this._playerManagers) {
          if (this._frontendManager !== null && manager.isAI) {
            let position = manager.position
            dg(`removing ai ${position}`, 'debug   ')
            manager.removeInformation()
            for (let frontend of this._frontendManagers) {
              frontend.sendEvent('removeAI', position)
            }
            manager.createHandshakeWithClient(name, team, link, isAI, socket, {yellow: this._goldScore, green: this._greenScore})
            this.emitGameEvent(`${manager.name} entered the game.`)
            this.updateFrontendData()
            dg(`client added to --> player manager ${manager.position}`, 'debug')
            this._currentPlayers++
            this.emitCurrentPlayerCount()
            return true
          }
        }
      }
      return false
    }

    /**
     * Sends an error event to a client
     * @param {Object} socket - socket object
     * @param {String} error - message
     */
    emitError(socket, error) {
      socket.emit('errorMessage', {
        error: error
      })
    }

    /**
     * Removes a player from a manager once they leave the game
     * @param {String} socketId - id of socket
     */
    findClientThatLeft(id) {
      dg('finding client to remove', 'debug')
      for (let manager of this._playerManagers) {
        if (manager.id === id) {
          this.emitGameEvent(`${manager.name} left the game.`)
          if (!manager.isAI) {
            this._currentPlayers--
            this.emitCurrentPlayerCount()
          }
          for (let frontend of this._frontendManagers) {
            if (!manager.isAI) {
              frontend.sendEvent('connectAI', manager.position)
            }
          }
          manager.removeInformation()
          this.updateFrontendData()
          return
        }
      }

      for (let i = 0; i < this._frontendManagers.length; i++) {
        if (this._frontendManagers[i].id === id) {
          dg('a frontend has disconnected', 'debug')
          this._frontendManagers.splice(i, 1)
          return
        }
      }
    }

    /**
   * Updates the frontend
   */
    updateFrontendData() {
      let data = {
        board: this._gameBoard.sendableBoard(),
        players: this._playerManagers,
        yellow: this._goldScore,
        green: this._greenScore
      }

      for (let frontend of this._frontendManagers) {
        frontend.sendEvent('updateState', data)
      }
    }

    /**
  * Updates clients' data
  */
    updateClientData() {
      for (let manager of this._playerManagers) {
        if (manager.id !== null) {
          manager.sendEvent('boardUpdate', {yellow: this._goldScore, green: this._greenScore})
          manager.sendEvent('dataUpdate')
        }
      }
    }

    swapMade(manager) {
      this._swaps++
      if (this.checkGameOver()) {
        this.gameOver()
        return
      }
      manager.updateHand(manager.tiles)
      io.emit('gameEvent', {
        action: `${manager.name} swapped tiles`
      })
      this.updateTurn(manager, true)
    }

    /**
   * Updates who's turn it is
   */
    updateTurn(manager, swapped) {
      let position = manager.position
      dg(`it was player ${manager.position}'s turn`, 'debug')
      manager.isTurn = false
      do {
        position++
        if (position > 3) {
          position = 0
        }
      } while (this._playerManagers[position].id === null)
      this._playerManagers[position].isTurn = true
      if (!swapped) {
        this._swaps = 0
      }
      dg(`it is now player ${position}'s turn`, 'debug')
      clearInterval(timer)
      this.updateFrontendData()
      this.updateClientData()
      this.timer()
    }

    /**
     * Timer for a player's turn
     */
    timer() {
      for (let manager of this._playerManagers) {
        if (manager.id !== null && manager.isTurn) {
          let time = 60
          timer = setInterval(() => {
            if (time >= 0) {
              if (manager.id !== null) {
                time--
              } else {
                clearInterval(timer)
              }
            } else {
              clearInterval(timer)
              dg(`${manager.name}'s time has expired`, 'info')
              this.emitGameEvent(`${manager.name}'s time has expired`)
              this._swaps++
              if (this.checkGameOver()) {
                this.gameOver()
                return
              }
              this.updateTurn(manager, true)
            }
          }, 1000)
        }
        break
      }
    }

    /**
     * Checks to see if the game is over
     */
    checkGameOver() {
      return this._swaps === 4
    }

    gameOver() {
      dg('all players have swapped tiles, game over', 'info')
      this.emitGameEvent('game over!')
      for (let manager of this._playerManagers) {
        if (manager.id !== null) {
          manager.isTurn = false
          manager.sendEvent('dataUpdate')
        }
      }

      this._swaps = 0

      let finalScores = []
      let winner = null
      let highestScore = 0
      for (let manager of this._playerManagers) {
        if (manager.id !== null) {
          if (manager.score > highestScore) {
            highestScore = manager.score
            winner = manager.name
          }
          let data = {
            name: manager.name,
            score: manager.score
          }
          finalScores.push(data)
        }
      }
      let goldWin = this._goldScore > this._greenScore
      db.updateWin('Gold', this._goldScore, goldWin)
      db.updateWin('Green', this._greenScore, !goldWin)

      io.emit('gameOver', {
        scores: finalScores,
        winner: winner === null ? 'No one!' : winner,
        winningTeam: goldWin ? 'Gold' : 'Green'
      })

      let timeUntil = 5
      // TODO: See if this can be moved to game event? @Landon
      let timer = setInterval(() => {
        if (timeUntil !== 0) {
          dg(`${timeUntil}`, 'debug')
          io.emit('newGameCountdown', {
            timer: timeUntil
          })
          this.emitGameEvent(`New game starts in ${timeUntil}`)
          timeUntil--
        } else {
          clearInterval(timer)
          dg('new game started!', 'info')
          this.startNewGame()
        }
      }, 1000)
    }

    /**
   * Sends out a boardUpdate event to all clients
   */
    boardUpdate() {
      io.emit('boardUpdate', {
        board: this._gameBoard.sendableBoard(),
        yellow: this._goldScore,
        green: this._greenScore
      })
    }

    play(newBoard, player) {
      const letters = ex.extractLetters(newBoard, this._gameBoard.board, player)

      if (!letters) {
        let response = {
          error: 7,
          data: 'cheater'
        }
        return rh(response, player, this)
      }

      const words = ex.extractWords(letters, newBoard)

      this.wordValidation(words)
        .then(response => {
          let boardPlay = null
          if (response === true) {
          // if invalid type of play, gets the word that was invalid, else is undefined
            boardPlay = this._gameBoard.placeWords(words, player)
          } else {
          // if the word is invalid
            return rh(response, player, this)
          }
          // if the board has attempted to play a word
          if (boardPlay.error === 0) {
            let ls = letters.map(l => l.letter)
            player.updateHand(ls)
          }
          return rh(boardPlay, player, this)
        })
        .catch(e => {
          dg(`${e}`, 'error')
        })
    }

    /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
    wordValidation(words) {
      const search = words.map(s => s.word).join(',')

      dg('checking words against database', 'debug')
      return db.dictionaryCheck(search).then(r => {
        return this.pruneResults(r)
      })
    }

    /**
   * Prunes the data sent back from the DB to check if anywords are either invalid or bad words
   * @param {Array} response - word data sent back from DB
   */
    pruneResults(response) {
      dg('pruning results of database response', 'debug')
      for (let word of response) {
        if (word.bad) {
          return {
            error: 6,
            data: word.word
          }
        }
        if (!word.valid) {
          return {
            error: 1,
            data: word.word
          }
        }
      }

      return true
    }

    /**
     * Emits a game event to users
     * @param {String} event - an event that just happened
     */
    emitGameEvent(event, bonus) {
      io.emit('gameEvent', {
        action: event,
        bonus: bonus
      })
    }

    /**
   * Calculates the score of a play
   * @param {Object} player - player to add score to
   * @param {Array} words - array of words to calculate score for
   */
    calculateScore(player, words) {
      let score = sc(words, this._gameBoard.board)

      if (!player.isAI) {
        db.updatePlayer(player, score.words)
      }

      this.addScore(player, score.totalScore)
      this.updateTurn(player, false)
      return score
    }

    /**
   * Adds the score to the player's score and the team they are on
   * @param {Object} player - player to add score to
   * @param {Number} score - score
   */
    addScore(player, score) {
    // Need to update DB as well
      player.addScore(score)

      switch (player.team) {
        case 'Green':
          this._greenScore += score
          break
        case 'Gold':
          this._goldScore += score
          break
      }
    }

    /**
   * Starts a new game by resetting everything
   */
    startNewGame() {
      this.resetPlayers()
      this.resetGameboard()
      io.emit('newGame')
      this.boardUpdate()
      this.updateClientData()
      io.emit('gameEvent', {
        action: 'New game started'
      })
    }

    /**
   * Creates a new gameboard and initializes it
   */
    resetGameboard() {
      this._gameBoard = new Gameboard()
      this._playerManagers[0].isTurn = true
    }

    /**
   * Resets all players' scores
   */
    resetPlayers() {
      this._greenScore = 0
      this._goldScore = 0

      this._playerManagers.map(p => {
        p.resetScore()
        p.updateHand(p.tiles)
      })
    }
  }

  return GameManager
}
