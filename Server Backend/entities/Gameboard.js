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
  consumeInput(x, y, h, word, res) {
    if (word.trim() === '') {
      this.error = 1
      return res.json(this.handleResponse(word))
    }

    this.wordIsValid(word).then(t => {
      console.log('The board now has an answer')
      if (t === true) {
        this.placeWord({ x: x, y: y }, { x: h ? x + word.length - 1 : x, y: h ? y : y + word.length - 1 }, word, h)
      }
      return res.json(this.handleResponse(word))
    }).catch(e => {
      return res.json(e)
    })
    console.log('The board is thinking')
  }

  /**
   * This method handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(word) {
    const result = {
      invalid: true
    }
    let reason = null

    result['word'] = word.toUpperCase()

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
  wordIsValid(word) {
    return axios.get('http://localhost:8080/dictionary/validate?word=' + word)
      .then((res) => {
        if (res.data) {
          return true
        }
        this.error = 1
      }).catch((e) => {
        console.log(e)
      })
  }

  /**
   * Method that places a word on the baord
   * @param {Object} startCoords - object structured as such: {x: x, y: y}
   * @param {Object} endCoords - object structured as such: {x: x, y: y}
   * @param {String} word - word that will be placed on the board
   * @param {Boolean} h - is this word horizontal
   */
  placeWord(startCoords, endCoords, word, h) {
    this.error = 0
    const tempBoard = _.cloneDeep(this.board)

    /**
     * For word placement validation
     */
    let validWordPlacement = false

    for (let i = startCoords.x; i <= endCoords.x; i++) {
      for (let j = startCoords.y; j <= endCoords.y; j++) {
        /**
         * Check to see if the word was somehow placed out of bounds
         */
        if (tempBoard[j][i] === undefined) {
          this.error = 2
          return
        }
        let l = tempBoard[j][i].letter
        let wordLetter = h ? word[i - startCoords.x].toUpperCase() : word[j - startCoords.y].toUpperCase()

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
    if (this.firstPlay) {
      const c = Math.floor(this.size / 2)
      if (!tempBoard[c][c].letterPlaced) {
        this.error = 4
        return
      } else {
        this.firstPlay = false
        this.board = tempBoard
        return
      }
    }

    if (!validWordPlacement) {
      this.error = 5
      return
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
