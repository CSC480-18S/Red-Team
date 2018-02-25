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
   * Method that places a word on the baord
   * @param {Object} startCoords - object structured as such: {x: x, y: y}
   * @param {Object} endCoords - object structured as such: {x: x, y: y}
   * @param {String} word - word that will be placed on the board
   */
  placeWord(startCoords, endCoords, word) {
    const tempBoard = _.cloneDeep(this.board)
    const val = this.validatePosition

    for (let i = startCoords.x; i <= endCoords.x; i++) {
      for (let j = startCoords.y; j <= endCoords.y; j++) {
        let l = tempBoard[j][i].letter
        let w = startCoords.x === endCoords.x ? word[j - startCoords.y].toUpperCase() : word[i - startCoords.x].toUpperCase()

        if (!val(l, w)) {
          return false
        }

        tempBoard[j][i].letter = w
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
    return currentLetter === '.' || currentLetter === toBePlacedLetter
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
