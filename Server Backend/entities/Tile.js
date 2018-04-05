'use strict'

class Tile {
  /**
   * @param {Number} x - x position
   * @param {Number} y - y position
   * @param {String} multiplier - the multiplier this Tile has
   */
  constructor(y, x, multiplier) {
    this._x = x
    this._y = y
    this._m = multiplier
    this._lP = false
    this._l = null
    this._pB = ''
    this._tP = null
  }

  /**
   * Letter Setter
   */
  set letter(letter) {
    this._l = letter
    this._lP = true
  }

  /**
   * Played by setter
   */
  set playedBy(playedBy) {
    this._pB = playedBy
  }

  /**
   * Time played at setter
   */
  set timePlayedAt(timePlayedAt) {
    this._tP = timePlayedAt
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
    return this._m
  }

  /**
   * Letter placed getter
   */
  get letterPlaced() {
    return this._lP
  }

  /**
   * Letter getter
   */
  get letter() {
    return this._l
  }

  /**
   * Played by getter
   */
  get playedBy() {
    return this._pB
  }

  /**
   * Time played at getter
   */
  get timePlayedAt() {
    return this._tP
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Tile
