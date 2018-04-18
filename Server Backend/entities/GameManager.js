'use strict'
/**
 * Imports files
 */
const axios = require('axios')
const Gameboard = require('./Gameboard')
const sc = require('../helpers/ScoreCalculator')
const rh = require('../helpers/ResponseHandler')
const ex = require('../helpers/Extractor')
const dg = require('../helpers/Debug')
const PlayerManager = require('./PlayerManager')
const FrontendManager = require('./FrontendManager')
require('../helpers/Debug')

module.exports = (io) => {
  class GameManager {
    constructor() {
      this._gameBoard = new Gameboard()
      this._playerManagers = []
      this._frontendManagers = []
      this._greenScore = 0
      this._yellowScore = 0
      this._swaps = 0
      this._players = 0
      this._ai = 0
      this.init()
    }

    /**
     * Board getter
     */
    get board() {
      return this._gameBoard
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
        this.addClientToManager('ai_test', 'Green', true, socket)
        // this.updateConnectionCounts('a', 1)
        dg('ai connected', 'info')
      } else if (response.isSF) {
        this.createFrontendManager(socket)
        dg('a server frontend connected', 'info')
      } else if (response.isClient) {
        this.addClientToManager('client_test', 'Yellow', false, socket)
        // this.updateConnectionCounts('p', 1)
        dg('client connected', 'info')
      }
    }

    /**
     * Adds a client to a PlayerManager that does not have any data inside of it
     * @param {String} name - name of player
     * @param {String} team - team player is on
     * @param {Boolean} isAI - AI or not
     * @param {Object} socket - socket object
     */
    addClientToManager(name, team, isAI, socket) {
      let teams = ['Green', 'Yellow']
      let n = isAI === true ? 'AI' : 'CLIENT'
      console.log('DEBUG: FINDING MANAGER TO ADD TO')
      for (let manager of this._playerManagers) {
        if (manager.id === null) {
          manager.createHandshakeWithClient(`${n}_${manager.position}`, teams[Math.floor(Math.random() * teams.length)], isAI, socket)
          this.emitGameEvent(`${manager.name} entered the game.`)
          this.updateFrontendData()
          if (isAI) {
            dg(`ai added to --> player manager ${manager.position}`, 'debug')
          } else {
            dg(`client added to --> player manager ${manager.position}`, 'debug')
          }

          return
        }
      }

      if (!isAI) {
        for (let manager of this._playerManagers) {
          if (this._frontendManager !== null && manager.isAI) {
            let position = manager.position
            dg(`removing ai ${position}`, 'debug   ')
            manager.removeInformation()
            for (let frontend of this._frontendManagers) {
              frontend.sendEvent('removeAI', position)
            }
            manager.createHandshakeWithClient(`${n}_${manager.position}`, teams[Math.floor(Math.random() * teams.length)], isAI, socket)
            this.emitGameEvent(`${manager.name} entered the game.`)
            this.updateFrontendData()
            dg(`client added to --> player manager ${manager.position}`, 'debug')
            return
          }
        }
      }

      dg('there are already max players connected', 'error')
      socket.emit('errorMessage', {
        error: 'There are already 4 players connected to the game.'
      })
    }

    /**
     * Removes a player from a manager once they leave the game
     * @param {String} socketId - id of socket
     */
    findClientThatLeft(id) {
      console.log('DEBUG: FINDING CLIENT TO REMOVE')
      for (let manager of this._playerManagers) {
        if (manager.id === id) {
          this.emitGameEvent(`${manager.name} left the game.`)
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
     * Adds or subtracts player and ai and sends it out to clients
     * @param {String} type - p for player or a for ai
     * @param {Number} amount - +1 or -1 based
     */
    updateConnectionCounts(type, amount) {
      switch (type) {
        case 'p':
          this._players += amount
          break
        case 'a':
          this._ai += amount
          break
      }

      io.emit('connections', {
        players: this._players,
        ai: this._ai
      })
    }

    /**
   * Updates the frontend
   */
    updateFrontendData() {
      let data = {
        board: this._gameBoard.sendableBoard(),
        players: this._playerManagers,
        yellow: this._yellowScore,
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
          manager.sendEvent('dataUpdate')
        }
      }
    }

    swapMade(manager) {
      this._swaps++
      if (this._swaps === 4) {
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

      this.updateClientData()
      this.updateFrontendData()
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
      io.emit('gameOver', {
        scores: finalScores,
        winner: winner === null ? 'No one!' : winner,
        winningTeam: this._yellowScore > this._greenScore ? 'Yellow' : 'Green'
        // winningTeam: {
        //   team: this._yellowScore > this._greenScore ? 'Yellow' : 'Green',
        //   score: this._yellowScore > this._greenScore ? this._yellowScore : this._greenScore
        // }
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
        yellow: this._yellowScore,
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
      return axios.get('http://localhost:8090/dictionary/validate?words=' + search)
        .then(res => {
          return this.pruneResults(res.data)
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

      this.addScore(player, score)
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
        case 'Yellow':
          this._yellowScore += score
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
      this._yellowScore = 0

      this._playerManagers.map(p => {
        p.resetScore()
        p.updateHand(p.tiles)
      })
    }
  }

  return GameManager
}
