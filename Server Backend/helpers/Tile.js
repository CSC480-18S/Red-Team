'use strict'

class Tile {
  /**
   * @param {Number} x - x position
   * @param {Number} y - y position
   * @param {String} multiplier - the multiplier this Tile has
   */
  constructor(x, y, multiplier) {
    this._x = x
    this._y = y
    this._multiplier = multiplier
    this._letterPlaced = false
    this._letter = '.'
  }

  /**
   * Letter Setter
   */
  set letter(letter) {
    this._letter = letter
    this._letterPlaced = true
  }

  /**
   * X Getter
   */
  get x() {
    return this._x
  }

  /**
   * Y Getter
   */
  get y() {
    return this._y
  }

  /**
   * Multiplier Getter
   */
  get multiplier() {
    return this._multiplier
  }

  /**
   * Letter placed getter
   */
  get letterPlaced() {
    return this._letterPlaced
  }

  /**
   * Letter getter
   */
  get letter() {
    return this._letter
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Tile
