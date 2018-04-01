'use strict'
/**
 * Imports lodash and axios
 */
const axios = require('axios')
/**
 * Imports the Gameboard class
 */
const Gameboard = require('./Gameboard')

// remove this once there is a connection to the DB
const letters = 'abcdefghijklmnopqrstuvwxyz'.split('')
const tiles = letters.map(t => {
  return {
    letter: t,
    score: 1
  }
})

class GameManager {
  constructor(io) {
    this._board = new Gameboard()
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
    return this._board.board
  }

  /**
   * This is just for a demo
   * @param {Array} board - board
   * @param {Object} player - player object
   */
  play(board, player) {
    this._board.replaceBoard(board)
    this._io.emit('wordPlayed', {
      playValue: 10,
      board: board
    })
    this._io.emit('gameEvent', {
      action: `${player.name} just played a word for ${10} points.`
    })
    player.isTurn = false
  }

  // play(board, player) {
  //   this.wordValidation(trimmed)
  //     .then(response => {
  //       console.log('The board now has an answer')
  //       let placement
  //       if (response === true) {
  //         placement = this._board.placeWords(trimmed, user)
  //       } else {
  //         return this.handleResponse(this._error, response, res)
  //       }
  //       return this.handleResponse(this._board.error, placement, res)
  //     })
  //     .catch(e => {
  //       return res.status(400).json({code: 'D1', title: 'Database Error', desc: e.code})
  //     })
  //   console.log('The board is thinking')
  // }

  /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordValidation(board) {
    let words = this.extractWords(board)
    let search = words.map(s => s.word).join(',')

    return axios.get('http://localhost:8090/dictionary/validate?words=' + search)
      .then(res => {
        return this.pruneResults(res.data)
      })
  }

  /**
   * Prunes the data set back from the DB to check if anywords are either invalid or bad words
   * @param {Array} response - word data sent back from DB
   */
  pruneResults(response) {
    for (let word of response) {
      if (word.bad) {
        this._error = 6
        return word.word
      }
      if (!word.valid) {
        this._error = 1
        return word.word
      }
    }

    return true
  }

  /**
   * Handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(error, word) {
    const result = {
      invalid: true
    }
    let reason = null

    switch (error) {
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
        reason = 'Word is a bad word'
        break
      default:
        result.invalid = false
    }
    if (result.invalid) {
      result['reason'] = reason.toUpperCase()
      result['word'] = word.toUpperCase()
    }

    this._error = 0
    return res.json(result)
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
    this._board = new Gameboard()
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
