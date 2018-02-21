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
   * @param {Object} startCoords - object structured as such: {x: x, y: y}
   * @param {Object} endCoords - object structured as such: {x: x, y: y}
   * @param {String} word - word that will be placed on the board
   */
  placeWord(startCoords, endCoords, word) {
    let tempBoard = _.cloneDeep(this.board)
    for (let i = startCoords.x; i <= endCoords.x; i++) {
      for (let j = startCoords.y; j <= endCoords.y; j++) {
        if (startCoords.x === endCoords.x) {
          if (!this.validateWordPlacement(tempBoard[j][i].letter, word[j - startCoords.y].toUpperCase())) {
            return false
          }
          tempBoard[j][i].letter = word[j - startCoords.y].toUpperCase()
        } else {
          if (!this.validateWordPlacement(tempBoard[j][i].letter, word[i - startCoords.x].toUpperCase())) {
            return false
          }
          tempBoard[j][i].letter = word[i - startCoords.x].toUpperCase()
        }
      }
    }

    this.board = tempBoard
    return true
  }

  validateWordPlacement(currentLetter, toBePlacedLetter) {
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
