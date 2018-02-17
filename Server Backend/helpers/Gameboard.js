'use strict'
const t = require('../helpers/Tile')

class Gameboard {
  constructor (size) {
    this._height = size
    this._width = size
    this._board = new Array(this._width)
    this._initialized = false
  }

  init () {
    if (this._initialized) return

    for (let i = 0; i < this._board.length; i++) {
      this._board[i] = new Array(this._height)

      for (let j = 0; j < this._board[0].length; j++) {
        this._board[i][j] = new t(j, i, '1')
      }
    }

    this._initialized = true
  }
}

module.exports = Gameboard
