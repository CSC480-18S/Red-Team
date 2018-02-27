'use strict'
/**
 * Imports the lodash library
 */
const _ = require('lodash')
/**
 * Imports the Tile class
 */
const Tile = require('../helpers/Tile')

class Gameboard {
  /**
   * @param {Number} size - the size of the board for both height and width
   */
  constructor(size) {
    this._size = size
    this._board = new Array(this._size)
    this._initialized = false
    this.sameLetters = 0
  }

  set board(board) {
    this._board = board
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

    this._initialized = true
  }
  /**
   * Method consumes the user's input.  If the word is not valid, it does not attempt to place the word on the board and will send back to the user that the word is invalid
   * @param {Number} x - start x
   * @param {Number} y - start y
   * @param {Boolean} h - is the word horizontal
   * @param {String} word - the word to be validated
   */
  consumeInput(x, y, h, word) {
    if (this.wordIsValid(word)) {
      return this.placeWord({ x: x, y: y }, { x: h ? x + word.length - 1 : x, y: h ? y : y + word.length - 1 }, word)
    }

    return {
      reason: 'Not a valid word',
      word: word
    }
  }

  /**
   * Method that checks to see if the word is in the DB
   * @param {String} word - the word to be checked against the DB
   */
  wordIsValid(word) {
    // TODO: Change this to work with the DB
    if (word === '') {
      return false
    }
    return true
  }

  /**
   * Method that places a word on the baord
   * @param {Object} startCoords - object structured as such: {x: x, y: y}
   * @param {Object} endCoords - object structured as such: {x: x, y: y}
   * @param {String} word - word that will be placed on the board
   */
  placeWord(startCoords, endCoords, word) {
    this.sameLetters = 0
    const tempBoard = _.cloneDeep(this.board)

    for (let i = startCoords.x; i <= endCoords.x; i++) {
      for (let j = startCoords.y; j <= endCoords.y; j++) {
        /**
         * Check to see if the word was somehow placed out of bounds
         */
        if (tempBoard[j][i] === undefined) {
          return {
            reason: 'Placed out of the bounds of the board',
            word: word
          }
        }
        let l = tempBoard[j][i].letter
        let w = startCoords.x === endCoords.x ? word[j - startCoords.y].toUpperCase() : word[i - startCoords.x].toUpperCase()

        /**
         * Validate the letter to be placed
         */
        if (!this.validatePosition(l, w)) {
          return {
            reason: 'Invalid placement',
            word: word
          }
        }
        tempBoard[j][i].letter = w
      }
    }
    /**
     * Duplicate word checker
     */
    if (this.sameLetters === word.length) {
      return {
        reason: 'Word: ' + word + ' placed on top of same word: ' + word,
        word: word
      }
    }

    this.board = tempBoard
    return true
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
      this.sameLetters++
      return true
    }

    return false
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
