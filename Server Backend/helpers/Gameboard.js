'use strict'
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
   * Size setter
   */
  set size(size) {
    this._size = size
  }

  /**
   * Size getter
   */
  get size() {
    return this._size
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
    if (this._initialized) return

    for (let i = 0; i < this._board.length; i++) {
      this._board[i] = new Array(this._size)

      for (let j = 0; j < this._board[0].length; j++) {
        this._board[i][j] = new Tile(j, i, '1')
      }
    }

    this._initialized = true
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
