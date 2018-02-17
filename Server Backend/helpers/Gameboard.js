'use strict'
const Tile = require('../helpers/Tile')

class Gameboard {
  constructor(size) {
    this._size = size
    this._board = new Array(this._size)
    this._initialized = false
  }

  set size(size) {
    this._size = size
  }

  get size() {
    return this._size
  }

  get initialized() {
    return this._initialized
  }

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

module.exports = Gameboard
