'use strict'

class Player {
  constructor() {
    this._name = ''
    this._position = undefined
    this._tiles = []
  }

  set name(name) {
    this._name = name
  }

  set position(position) {
    this._position = position
  }

  get name() {
    return this._name
  }

  get position() {
    return this._position
  }

  get tiles() {
    return this._tiles
  }

  addTiles(tiles) {
    this._tiles.push(...tiles)
  }

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
}

module.exports = Player
