'use strict'
/**
 * Imports files
 */
const axios = require('axios')
const Gameboard = require('./Gameboard')
require('../helpers/Debug')
const sc = require('../helpers/ScoreCalculator')
const rh = require('../helpers/ResponseHandler')
const ex = require('../helpers/Extractor')

class GameManager {
  constructor(io) {
    this._gameBoard = new Gameboard()
    this._tileScores = []
    this._greenScore = 0
    this._error = 0
    this._yellowScore = 0
    this._io = io
  }

  /**
   * Board getter
   */
  get board() {
    return this._gameBoard
  }

  play(newBoard, player, callback) {
    const words = ex(newBoard, this._gameBoard.board)

    console.log('DEBUG: THE BOARD IS THINKING...'.debug)
    this.wordValidation(words)
      .then(response => {
        console.log('DEBUG: THE BOARD NOW HAS AN ANSWER...'.debug)
        let boardPlay = null
        if (response === true) {
          // if invalid type of play, gets the word that was invalid, else is undefined
          boardPlay = this._gameBoard.placeWords(words, player)
        } else {
          // if the word is invalid
          return callback(rh(response.error, response, player, this))
        }
        // if the board has attempted to play a word
        return callback(rh(boardPlay.error, boardPlay, player, this))
      })
      .catch(e => {
        console.log(`ERROR: ${e}`.error)
      })
  }

  /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordValidation(words) {
    const search = words.map(s => s.word).join(',')

    console.log('DEBUG: CHECKING WORDS AGAINST DATABASE...'.debug)
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
    console.log('DEBUG: PRUNING RESULTS OF DATABASE RESPONSE...'.debug)
    for (let word of response) {
      if (word.bad) {
        return {
          error: 6,
          word: word.word
        }
      }
      if (!word.valid) {
        return {
          error: 1,
          word: word.word
        }
      }
    }

    return true
  }

  /**
   * Calculates the score of a play
   * @param {Object} player - player to add score to
   * @param {Array} words - array of words to calculate score for
   * @param {Object} bonus - bonus to factor in
   */
  calculateScore(player, words) {
    console.log('DEBUG: CALCULATING SCORE...'.debug)
    let score = sc(words, this._gameBoard.board)

    this.addScore(player, score)
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
    this.resetScores()
    this.resetGameboard()
  }

  /**
   * Creates a new gameboard and initializes it
   */
  resetGameboard() {
    this._gameBoard = new Gameboard()
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
