'use strict'
/**
 * Imports files
 */
const Gameboard = require('./Gameboard')
const sc = require('../helpers/ScoreCalculator')
const ex = require('../helpers/Extractor')
const dg = require('../helpers/Debug')(true)
const db = require('../helpers/DB')

class GameManager {
  constructor(dataUpdate, gameEvent, frontendsUpdate) {
    this._gameBoard = new Gameboard()
    this._greenScore = 0
    this._goldScore = 0
    this._swaps = 0
    this.dataUpdate = dataUpdate
    this.gameEvent = gameEvent
    this.frontendsUpdate = frontendsUpdate
  }

  /**
     * Board getter
     */
  get board() {
    return this._gameBoard
  }

  /**
   * Determines what the player just did
   * @param {String} event - event
   * @param {Object} player - player
   * @param {Object} data - data
   */
  determineEvent(data, player) {
    switch (data.event) {
      case 'playWord':
        this.play(data.board, player)
        break
      case 'swap':
        this.swapMade(player)
        break
    }
  }

  play(newBoard, player) {
    const letters = ex.extractLetters(newBoard, this._gameBoard.board, player)

    if (letters.valid) {
      const words = ex.extractWords(letters.data, newBoard)

      this.wordValidation(words)
        .then(r => {
          let boardResponse = null
          if (r.valid === true) {
          // if invalid type of play, gets the word that was invalid, else is undefined
            boardResponse = this._gameBoard.placeWords(words, player)
            // if the board has attempted to play a word
            if (boardResponse.valid) {
              let ls = letters.map(l => l.letter)
              player.updateHand(ls)
            }
            return this.respond(boardResponse.error, boardResponse.data, player)
          } else {
          // if the word is invalid
            return this.respond(r.error, r.data, player)
          }
        })
        .catch(e => {
          dg(`${e}`, 'error')
        })
    } else {

    }
  }

  /**
   * Grabs the latest data of the game
   */
  latestData() {
    return {
      board: this.board.sendableBoard(),
      gold: this._goldScore,
      green: this._greenScore
    }
  }

  swapMade(player) {
    this._swaps++
    if (this.checkGameOver()) {
      this.gameOver()
      return
    }
    player.updateHand(player.tiles)
    this.gameEvent(`${player.name} swapped tiles`)
    this.updateTurn(player, true)
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
    // clearInterval(timer)
    this.frontendsUpdate()
    this.dataUpdate(this.board.sendableBoard())
    // this.timer()
  }

  /**
     * Timer for a player's turn
     */
  // timer() {
  //   for (let manager of this._playerManagers) {
  //     if (manager.id !== null && manager.isTurn) {
  //       let time = 60
  //       timer = setInterval(() => {
  //         if (time >= 0) {
  //           if (manager.id !== null) {
  //             time--
  //           } else {
  //             clearInterval(timer)
  //           }
  //         } else {
  //           clearInterval(timer)
  //           dg(`${manager.name}'s time has expired`, 'info')
  //           this.gameEvent(`${manager.name}'s time has expired`)
  //           this._swaps++
  //           if (this.checkGameOver()) {
  //             this.gameOver()
  //             return
  //           }
  //           this.updateTurn(manager, true)
  //         }
  //       }, 1000)
  //     }
  //     break
  //   }
  // }

  /**
     * Checks to see if the game is over
     */
  checkGameOver() {
    return this._swaps === 4
  }

  gameOver() {
    dg('all players have swapped tiles, game over', 'info')
    this.gameEvent('Game over!')
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

    let gameOverData = {
      event: 'gameOver',
      data: {
        scores: finalScores,
        winner: winner === null ? 'No one!' : winner,
        winningTeam: goldWin ? 'Gold' : 'Green'
      }
    }

    this.ws.send(JSON.stringify(gameOverData))

    let timeUntil = 5
    // TODO: See if this can be moved to game event? @Landon
    let timer = setInterval(() => {
      if (timeUntil !== 0) {
        dg(`${timeUntil}`, 'debug')
        let timer = {
          type: 'newGameCountdown',
          data: {
            timer: timeUntil
          }
        }
        this.ws.send(JSON.stringify(timer))
        this.gameEvent(`New game starts in ${timeUntil}`)
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
    let boardUpdateData = {
      event: 'boardUpdate',
      data: {
        board: this._gameBoard.sendableBoard(),
        yellow: this._goldScore,
        green: this._greenScore
      }
    }

    this.ws.send(JSON.stringify(boardUpdateData))
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
          valid: false,
          error: 6,
          data: word.word
        }
      }
      if (!word.valid) {
        return {
          valid: false,
          error: 1,
          data: word.word
        }
      }
    }

    return {
      valid: true
    }
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
    this.ws.send(JSON.stringify({event: 'newGame'}))
    this.boardUpdate()
    this.updateClientData()
    this.gameEvent('New game started')
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

  respond(e, data, player) {
    let reason = null
    let invalid = true

    switch (e) {
      case 1:
        reason = 'Not a valid word'
        break
      case 2:
        reason = 'Placed out of the bounds of the board'
        break
      case 3:
        reason = 'Invalid placement'
        break
      case 4:
        reason = 'Word was not played over the center tile'
        break
      case 5:
        reason = 'Word not connected to played tiles'
        break
      case 6:
        reason = 'That is a bad word'
        db.updatePlayerDirty(player, data)
        break
      case 7:
        reason = 'You cheated'
        break
      default:
        invalid = false
    }

    if (invalid) {
      player.invalidPlay(reason)
      return
    }
    let score = this.calculateScore(player, data.data)

    dg('sending out word played event', 'debug')
    this.dataUpdate(this.board.sendableBoard())
    dg('sending out game event event', 'debug')
    let words = data.data.map(w => w.word)
    let action = `${player.name} played ${words} for ${score.totalScore} points`
    dg(action, 'info')
    // TODO: Need to flag whether or not this is a bonus play or not @Landon
    // const search = words.map(s => s).join(',')
    // db.dictionaryCheck(search).then(r => {
    //   let bonus = false
    //   for (let word of r) {
    //     if (word.special) {
    //       bonus = true
    //     }
    //   }
    this.gameEvent(action)
    // }).catch(e => {
    //   console.log(e)
    // })
  }
}

module.exports = GameManager
