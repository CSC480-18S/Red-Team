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
    this._multiplier = multiplier
    this._letterPlaced = false
    this._letter = null
    this._playedBy = ''
    this._timePlayedAt = null
  }

  /**
   * Letter Setter
   */
  set letter(letter) {
    this._letter = letter
    this._letterPlaced = true
  }

  /**
   * Played by setter
   */
  set playedBy(playedBy) {
    this._playedBy = playedBy
  }

  /**
   * Time played at setter
   */
  set timePlayedAt(timePlayedAt) {
    this._timePlayedAt = timePlayedAt
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

  /**
   * Played by getter
   */
  get playedBy() {
    return this._playedBy
  }

  /**
   * Time played at getter
   */
  get timePlayedAt() {
    return this._timePlayedAt
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Tile
