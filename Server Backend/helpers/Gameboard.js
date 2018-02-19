'use strict'

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

  placeWord(startCoords, endCoords, word) {
    for (let i = startCoords.x; i <= endCoords.x; i++) {
      for (let j = startCoords.y; j <= endCoords.y; j++) {
        if (startCoords.x === endCoords.x) {
          this._board[j][i].letter = word[j - startCoords.y].toUpperCase()
        } else {
          this._board[j][i].letter = word[i - startCoords.x].toUpperCase()
        }
      }
    }
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
