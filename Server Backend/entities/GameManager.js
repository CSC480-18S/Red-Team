'use strict'
/**
 * Imports lodash and axios
 */
const _ = require('lodash')
const axios = require('axios')
/**
 * Imports the Gameboard class
 */
const Gameboard = require('./Gameboard')
const Player = require('./Player')

// remove this once there is a connection to the DB
const letters = 'abcdefghijklmnopqrstuvwxyz'.split('')
const tiles = letters.map(t => {
  return {
    letter: t,
    score: 1
  }
})

class GameManager {
  constructor() {
    this._board = new Gameboard()
    this._tileScores = []
    this._players = []
    this._greenScore = 0
    this._yellowScore = 0
  }

  /**
   * Encapsulates gamboard consumeInput()
   * @param {Array} words - words to play
   * @param {Object} res - response object
   * @param {String} user - user that played this move
   */
  play(words, res, user) {
    this._board.consumeInput(words, res, user)
  }

  /**
   * Calculates the score of a play
   * @param {Object} player - player to add score to
   * @param {Array} words - array of words to calculate score for
   * @param {Object} bonus - bonus to factor in
   */
  calculateScore(player, words, bonus) {
    let cumulativeScore = 0

    words.map(w => {
      let wordArray = w.toUpperCase().split('')

      let score = wordArray.map(l => {
        for (let t of tiles) {
          if (t.letter === l) {
            if (bonus.type === 'letter' && bonus.letter === l) {
              return t.score * bonus.bonus
            }
            return t.score
          }
        }
      }).reduce((prev, curr) => {
        return prev + curr
      })

      if (bonus.type === 'word') {
        score = score * bonus.bonus
      }

      cumulativeScore += score
    })

    this.addScore(player, cumulativeScore)
  }

  /**
   * Adds the score to the player's score and the team they are on
   * @param {Object} player - player to add score to
   * @param {Number} score - score
   */
  addScore(player, score) {
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
   * Adds a player to the game
   * @param {String} username - player's username
   */
  addPlayer(username) {
    this._players.push(new Player(username))
  }

  /**
   * Removes a player from the game
   * @param {String} username - user to be removed
   */
  removePlayer(username) {
    let users = this._players.filter(p => {
      return p.name !== username
    })

    this._players = users
  }

  /**
   * Starts a new game by resetting everything
   */
  startNewGame() {
    // Need to grab newest tile scores every game
    // PSUEDO: axios call out to DB, add scores to dictionary
    this.resetScores()
    this.resetPlayerScores()
    this.resetGameboard()
  }

  /**
   * Creates a new gameboard and initializes it
   */
  resetGameboard() {
    this._board = new Gameboard()
    this._board.init()
  }

  /**
   * Resets all players' scores
   */
  resetScores() {
    this._greenScore = 0
    this._yellowScore = 0

    this._players.map(p => {
      p.resetScore()
    })
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = GameManager
