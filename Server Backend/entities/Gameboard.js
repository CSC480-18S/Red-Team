'use strict'
/**
 * Imports the lodash library
 */
const _ = require('lodash')
/**
 * Imports the Tile class
 */
const Tile = require('./Tile')
const axios = require('axios')

class Gameboard {
  /**
   * @param {Number} size - the size of the board for both height and width
   */
  constructor() {
    this._size = 11
    this._board = new Array(this._size)
    this._initialized = false
    this._firstPlay = true
    this._error = 0
  }

  set board(board) {
    this._board = board
  }

  set initialized(initialized) {
    this._initialized = initialized
  }

  set firstPlay(firstPlay) {
    this._firstPlay = firstPlay
  }

  set error(error) {
    this._error = error
  }

  /**
   * Size getter
   */
  get size() {
    return this._size
  }

  /**
   * Board getter
   */
  get board() {
    return this._board
  }

  /**
   * Initialized getter
   */
  get initialized() {
    return this._initialized
  }

  /**
   * First play getter
   */
  get firstPlay() {
    return this._firstPlay
  }

  /**
   * Error getter
   */
  get error() {
    return this._error
  }

  /**
   * Creates a double array of Tiles
   * If this method has already been run, it returns before it can recreate the double array
   */
  init() {
    if (this._initialized) {
      return true
    }

    for (let i = 0; i < this._board.length; i++) {
      this._board[i] = new Array(this._size)

      for (let j = 0; j < this._board[0].length; j++) {
        this._board[i][j] = new Tile(j, i, '1')
      }
    }

    this.initialized = true
  }
  /**
   * Method consumes the user's input.  If the word is not valid, it does not attempt to place the word on the board and will send back to the user that the word is invalid
   * @param {Number} x - start x
   * @param {Number} y - start y
   * @param {Boolean} h - is the word horizontal
   * @param {String} word - the word to be validated
   */
  consumeInput(words, res) {
    this.wordIsValid(words).then(response => {
      console.log('The board now has an answer')
      // let placementResponse
      if (response === true) {
        this.placeWords(words)
      } else {
        return res.json(this.handleResponse(response))
      }
      return res.json(this.handleResponse(words))
    }).catch(e => {
      return res.json(e)
    })
    console.log('The board is thinking')
  }

  /**
   * This method handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(words) {
    const result = {
      invalid: true
    }
    let reason = null

    result['word'] = words.map(w => w.word.toUpperCase())

    switch (this.error) {
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
        return result
    }

    result['reason'] = reason.toUpperCase()

    return result
  }

  /**
   * Method that checks to see if the word is in the DB
   * @param {String} word - the word to be checked against the DB
   */
  wordIsValid(words) {
    let search = words.map(s => s.word).join(',')

    return axios.get('http://localhost:8090/dictionary/validate?words=' + search)
      .then(res => {
        return this.pruneResults(res.data)
      }).catch((e) => {
        console.log(e)
      })
  }

  /**
   * Prunes the data set back from the DB to check if anywords are either invalid or bad words
   * @param {Array} response - word data sent back from DB
   */
  pruneResults(response) {
    for (let word of response) {
      if (word.bad) {
        this.error = 6
        return word.word
      }
      if (!word.valid) {
        this.error = 1
        return word.word
      }
    }

    return true
  }

  /**
   * Method that places words on the baord
   * @param {Array} words - array of words to place
   */
  placeWords(words) {
    this.error = 0
    const tempBoard = _.cloneDeep(this.board)

    /**
     * For word placement validation
     */
    let validWordPlacement = false
    for (let w of words) {
      let word = {
        word: w.word,
        sX: w.x,
        sY: w.y,
        eX: w.h ? w.x + w.word.length - 1 : w.x,
        eY: w.h ? w.y : w.y + w.word.length - 1
      }

      for (let i = word.sX; i <= word.eX; i++) {
        for (let j = word.sY; j <= word.eY; j++) {
        /**
         * Check to see if the word was somehow placed out of bounds
         */
          if (tempBoard[j][i] === undefined) {
            this.error = 2
            return
          }
          let l = tempBoard[j][i].letter
          let wordLetter = w.h ? word.word[i - word.sX].toUpperCase() : word.word[j - word.sY].toUpperCase()

          /**
         * Validate the letter to be placed
         */
          if (!this.validatePosition(l, wordLetter)) {
            this.error = 3
            return
          }

          if (!validWordPlacement && tempBoard[j][i].letterPlaced) {
            validWordPlacement = true
          }

          tempBoard[j][i].letter = wordLetter
        }
      }

      /**
     * Center tile validation
     */
      let check = true
      if (this.firstPlay) {
        const c = Math.floor(this.size / 2)
        if (!tempBoard[c][c].letterPlaced) {
          this.error = 4
          return
        } else {
          this.firstPlay = false
          check = false
        }
      }

      if (!validWordPlacement && check) {
        this.error = 5
        return
      }
    }

    this.board = tempBoard
  }

  /**
   * Validates whether a letter can be placed in the current position
   * @param {String} currentLetter - the letter that is currently on the board
   * @param {String} toBePlacedLetter - the letter that is to be placed on the board
   */
  validatePosition(currentLetter, toBePlacedLetter) {
    if (currentLetter === '.') {
      return true
    } else if (currentLetter === toBePlacedLetter) {
      return true
    }

    return false
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
