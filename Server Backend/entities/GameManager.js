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

// letter distribution, alphabetically
const letterDist = [9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1]
let totalLetters = 0
let intervals = []

class GameManager {

  constructor() {
    this._board = null
    this._tileScores = []
    this._players = []
    this._greenScore = 0
    this._error = 0
    this._yellowScore = 0

    // set up intervals
    // push first interval
    intervals.push(letterDist[0])
    totalLetters += letterDist[0]
    // add the rest of the intervals
    for (let i = 1; i < letterDist.length; ++i) {
      intervals.push(intervals[i - 1] + letterDist[i])
      totalLetters += letterDist[i]
    }
  }

  /**
   * Board getter
   */
  get board() {
    return this._board.board
  }

  /**
   * Plays a user's move
   * @param {Array} words - words to play
   * @param {Object} res - response object
   * @param {String} user - user that played this move
   */
  play(words, res, user, io) {
    let trimmed = this.trimWords(words)

    this.wordValidation(trimmed)
      .then(response => {
        console.log('The board now has an answer')
        let placement
        if (response === true) {
          placement = this._board.placeWords(trimmed, user)
        } else {
          return this.handleResponse(this._error, response, res)
        }
        io.emit('board', this._board.board)
        return this.handleResponse(this._board.error, placement, res)
      })
      .catch(e => {
        return res.status(400).json({code: 'D1', title: 'Database Error', desc: e.code})
      })
    console.log('The board is thinking')
  }

  /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordValidation(words) {
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
   * Removes whitespace from words
   * @param {Array} words - words to trimmed
   */
  trimWords(words) {
    return words.map(w => {
      let word = w.word
      w['word'] = word.trim()
      return w
    })
  }

  /**
   * Handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(error, word, res) {
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
   * Determines what new letters a player will get after they
   * play their turn.
   * @param {int} lettersUsed number of letters to generate
   */
  getNewLetters(lettersUsed) {
    let newLetters = []

    // generate the new letters
    for (let a = 0; a < lettersUsed; ++a) {
      let index = Math.floor(Math.random() * totalLetters)

      for (let i = 0; i < intervals.length; ++i) {
        if (index <= intervals[i]) {
          newLetters.push(letters[i])
          break
        }
      }
    }

    return newLetters
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
