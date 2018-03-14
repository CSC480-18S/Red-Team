'use strict'

class Player {
  constructor() {
    this._name = ''
    this._position = null
    this._tiles = ['a', 'b', 'c']
  }

  set name(name) {
    this._name = name
  }

  set position(position) {
    this._position = position
  }

  set tiles(tiles) {
    this._tiles.push(...tiles)
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

  removeTiles(tiles) {
    this._tiles = this._tiles.filter(t => {
      tiles.forEach(e => {
        if (t !== e) {
          return t
        }
      })
    })
  }
}

module.exports = Player
