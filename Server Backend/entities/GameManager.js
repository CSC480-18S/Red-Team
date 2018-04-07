'use strict'
/**
 * Imports lodash, axios, Gameboard, and Debug classes
 */
const axios = require('axios')
const _ = require('lodash')
const Gameboard = require('./Gameboard')
require('../helpers/Debug')

// remove this once there is a connection to the DB
const letters = 'abcdefghijklmnopqrstuvwxyz'.split('')
const tiles = letters.map(t => {
  return {
    letter: t,
    score: 1
  }
})

class GameManager {
  constructor(io, serverManager) {
    this._gameBoard = new Gameboard()
    this._tileScores = []
    this._greenScore = 0
    this._error = 0
    this._yellowScore = 0
    this._io = io
    this._serverManager = serverManager
  }

  /**
   * Board getter
   */
  get board() {
    return this._gameBoard.board
  }

  play(newBoard, player, callback) {
    let letters = this.extractLetters(newBoard)
    let words = this.extractWords(letters, newBoard)

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
          return callback(this.handleResponse(response.error, response.word, player))
        }
        // if the board has attempted to play a word
        return callback(this.handleResponse(boardPlay.error, boardPlay.word, player))
      })
      .catch(e => {
        console.log(`ERROR: ${e}`.error)
      })
  }

  /**
   * Extracts letters from the new board that were played by the user
   * @param {Array} newBoard - board given by the user
   */
  extractLetters(newBoard) {
    let letters = []
    for (let i = 0; i < newBoard.length; i++) {
      for (let j = 0; j < newBoard[0].length; j++) {
        let currentBoardLetter = this.board[j][i].letter
        let newBoardLetter = newBoard[j][i]

        if (newBoardLetter !== null) {
          if (currentBoardLetter !== newBoardLetter) {
            let tile = {
              letter: newBoardLetter,
              x: i,
              y: j
            }
            letters.push(tile)
          }
        }
      }
    }
    console.log(`DEBUG: LETTERS EXTRACTED`.debug)
    console.log(`DATA: ${JSON.stringify(letters, null, 4)}`.data)
    return letters
  }

  /**
   * Extracts words out of the newboard
   * @param {Array} letters - array of letters
   * @param {Array} newBoard- board
   */
  extractWords(letters, newBoard) {
    let words = []

    letters.map(letterObject => {
      let inSpace = 0
      for (let i = 0; i < 2; i++) {
        let word = ''
        let startOfWord = false
        let endOfWord = false
        let startPosition = null
        let position = {
          x: letterObject.x,
          y: letterObject.y
        }

        let check = 0
        while (!startOfWord) {
          if (position.x < 0 || position.y < 0 || newBoard[position.y][position.x] === null) {
            startOfWord = true
            position.x = i === 0 ? position.x : position.x + 1
            position.y = i === 0 ? position.y + 1 : position.y
          } else {
            check++
            position.x = i === 0 ? position.x : position.x - 1
            position.y = i === 0 ? position.y - 1 : position.y
          }
        }
        if (check === 1) {
          inSpace++
        }

        startPosition = _.cloneDeep(position)

        while (!endOfWord) {
          if (position.x >= newBoard.length || position.y >= newBoard.length || newBoard[position.y][position.x] === undefined || newBoard[position.y][position.x] === null) {
            endOfWord = true
          } else {
            check++
            word += newBoard[position.y][position.x]
            position.x = i === 0 ? position.x : position.x + 1
            position.y = i === 0 ? position.y + 1 : position.y
          }
        }

        if (check === 2) {
          inSpace++
        }

        if (word.length > 1 || inSpace === 4) {
          let wordObject = {
            word: word,
            x: startPosition.x,
            y: startPosition.y,
            h: i === 1
          }
          words.push(wordObject)
        }
        word = ''
        check = 0
      }
    })

    let uniqueWords = _.uniqWith(words, _.isEqual)
    console.log(`DEBUG: WORDS EXTRACTED`.debug)
    console.log(`DATA: ${JSON.stringify(uniqueWords, null, 4)}`.data)
    return uniqueWords
  }

  /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordValidation(words) {
    let search = words.map(s => s.word).join(',')

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
   * Handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(error, word, player) {
    console.log('DEBUG: SENDING RESPONSE TO CLIENT...'.debug)
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
      player.sendEvent('play', result)
      return false
    }

    this._io.emit('wordPlayed', {
      board: this._gameBoard.sendableBoard()
    })
    return true
  }

  /**
   * Calculates the score of a play
   * @param {Object} player - player to add score to
   * @param {Array} words - array of words to calculate score for
   * @param {Object} bonus - bonus to factor in
   */
  calculateScore(player, words, bonus) {
    console.log('DEBUG: CALCULATING SCORE...'.debug)
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
