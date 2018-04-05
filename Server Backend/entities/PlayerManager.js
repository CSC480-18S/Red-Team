'use strict'
const console = require('better-console')

class PlayerManager {
  constructor(position, name, team, ai, socket, gameManager, serverManager) {
    this._name = name
    this._team = team
    this._isAI = ai
    this._socket = socket
    this._position = position
    this._tiles = []
    this._isTurn = false
    this._score = 0
    this._gameManager = gameManager
    this._serverManger = serverManager
    this.init()
  }

  /**
   * Turn setter
   */
  set isTurn(turn) {
    this._isTurn = turn
  }

  /**
   * Name getter
   */
  get name() {
    return this._name
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
  get isTurn() {
    return this._isTurn
  }

  /**
   * Team getter
   */
  get team() {
    return this._team
  }

  /**
   * AI getter
   */
  get isAI() {
    return this._isAI
  }

  /**
   * Socket getter
   */
  get socket() {
    return this._socket
  }

  /**
   * Score getter
   */
  get score() {
    return this._score
  }

  init() {
    this.addToHand()
    this.listenForPlayerEvents()
  }

  addToHand() {

  }

  /**
   * Listens for events that come from the client
   */
  listenForPlayerEvents() {
    this._socket.on('playWord', board => {
      console.table([[this.name, this.socket.id, 'made a play.']])
      this._gameManager.play(board, this)
      this._serverManger.changeTurn(this.position)
    })

    this._socket.on('disconnect', () => {
      this._serverManger.removePlayer(this)
      console.table([[this.name, this.socket.id, 'has disconnected.']])
    })
  }

  /**
   * Adds details when this position was already occupied by another player controller
   * @param {Array} tiles - array of tiles
   * @param {Boolean} isTurn - turn
   * @param {Number} score - score
   */
  addPositionDetails(tiles, isTurn, score) {
    this._tiles = tiles
    this._isTurn = isTurn
    this._score = score
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
module.exports = PlayerManager
