'use strict'
const console = require('better-console')

// letter distribution, alphabetically
const letterDist = [9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1]
const letters = 'abcdefghijklmnopqrstuvwxyz'.split('')
let totalLetters = 0
let intervals = []

class PlayerManager {
  constructor(position) {
    this._name = null
    this._team = null
    this._isAI = null
    this._socket = null
    this._socketId = null
    this._position = position
    this._tiles = []
    this._isTurn = false
    this._score = 0
    // this._gameManager = gameManager
    // this._serverManger = serverManager
    // this.init()

    // set up intervals
    // push first interval
    intervals.push(letterDist[0])
    totalLetters += letterDist[0]
    // add the rest of the intervals
    for (let i = 1; i < letterDist.length; ++i) {
      intervals.push(intervals[i - 1] + letterDist[i])
      totalLetters += letterDist[i]
    }
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
   * Socket id getter
   */
  get id() {
    return this._socketId
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

  /**
   * When a client connects, their information is injected into the manager
   * @param {String} name - name of player
   * @param {String} team - team player is on
   * @param {Boolean} isAI - AI or not
   * @param {Object} socket - socket object
   */
  createHandshakeWithClient(name, team, isAI, socket) {
    this._name = name
    this._team = team
    this._isAI = isAI
    this._socket = socket
    this._socketId = socket.id
  }

  /**
   * Removes player information
   */
  removePlayerInformation() {
    this._name = null
    this._team = null
    this._isAI = null
    this._socket = null
    this._socketId = null
  }

  /**
   * Determines what new letters a player will get after they
   * play their turn.
   * @param {int} lettersUsed - number of letters to generate
   */
  getNewLetters(lettersUsed) {
    let newLetters = []

    // generate the new letters
    for (let a = 0; a < lettersUsed; ++a) {
      let index = Math.floor(Math.random() * totalLetters)

      for (let i = 0; i < intervals.length; ++i) {
        if (index <= intervals[i]) {
          newLetters.push(letters[i])
          break
        }
      }
    }

    return newLetters
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
