'use strict'

class Player {
  constructor(user) {
    this._name = user
    this._position = undefined
    this._tiles = []
    this._turn = false
    this._score = 0
    this._team = null
  }

  /**
   * Name getter
   */
  get name() {
    return this._name
  }

  /**
   * Position setter
   */
  set position(position) {
    this._position = position
  }

  /**
   * Turn setter
   */
  set turn(turn) {
    this._tiles = turn
  }

  /**
   * Team setter
   */
  set team(team) {
    this._team = team
  }

  /**
   * Position getter
   */
  get position() {
    return this._position
  }

  /**
   * Tiles getter
   */
  get tiles() {
    return this._tiles
  }

  /**
   * Turn getter
   */
  get turn() {
    return this._turn
  }

  /**
   * Team getter
   */
  get team() {
    return this._team
  }

  /**
   * Adds tiles to the titles array
   * @param {Array} tiles - array of tiles to add to the existing tiles
   */
  addTiles(tiles) {
    this._tiles.push(...tiles)
  }

  /**
   * Removes tiles from array
   * @param {Array} tiles - array of tiles to remove
   */
  removeTiles(tiles) {
    let newTiles = []

    this._tiles.forEach(t => {
      let i = tiles.indexOf(t)
      if (i === -1) { // if t does not exist in the tiles array
        newTiles.push(t) // push to new array (letters that aren't being removed)
      } else { // if t is in the tiles array, remove it from the tiles array so multiple of the same letter is not removed
        tiles.splice(i, 1)
      }
    })

    this._tiles = newTiles
  }

  /**
   *
   * @param {Number} score - score to be added
   */
  addScore(score) {
    this._score += score
  }

  /**
   * Resets the player's score
   */
  resetScore() {
    this._score = 0
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Player
