'use strict'

/**
 * Imports files
 */
const ld = require('../helpers/LetterDistributor')
const dg = require('../helpers/Debug')(true)

class PlayerManager {
  constructor(position, gameManager) {
    this._name = null
    this._team = null
    this._isAI = null
    this._link = null
    this._socket = null
    this._socketId = null
    this._position = position
    this._tiles = []
    this._isTurn = false
    this._score = 0
    this._gameManager = gameManager
  }

  /**
   * Tile setter
   */
  set tiles(tiles) {
    this._tiles = tiles
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
   * Link getter
   */
  get link() {
    return this._link
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
  }

  /**
   * Listens for events coming from cient
   */
  listenForEvents() {
    if (this._socketId !== null) {
      this._socket.on('playWord', newBoard => {
        dg(`${this.name} attempting to make play...`, 'debug')
        this._gameManager.play(newBoard, this)
      })

      this._socket.on('swap', () => {
        dg(`player ${this.name} has swapped tiles`, 'info')
        this._gameManager.swapMade(this)
      })
    }
  }

  sendEvent(event, data) {
    switch (event) {
      case 'play':
        this.socket.emit(event, data)
        break
      case 'dataUpdate':
        this.socket.emit(event, {
          name: this.name,
          position: this.position,
          tiles: this.tiles,
          isTurn: this.isTurn,
          score: this.score
        })
        break
      case 'boardUpdate':
        this.socket.emit(event, {
          board: this._gameManager.board.sendableBoard()
        })
    }
  }

  /**
   * Creates an object that is sent over an event
   */
  sendableData() {
    return {
      name: this._name,
      isAI: this._isAI,
      position: this._position,
      isTurn: this._isTurn,
      tiles: this._tiles,
      score: this._score,
      team: this._team
    }
  }

  /**
   * When a client connects, their information is injected into the manager
   * @param {String} name - name of player
   * @param {String} team - team player is on
   * @param {String} link = player link in db
   * @param {Boolean} isAI - AI or not
   * @param {Object} socket - socket object
   */
  createHandshakeWithClient(name, team, link, isAI, socket) {
    this._name = name
    this._team = team
    this._link = link
    this._isAI = isAI
    this._socket = socket
    this._socketId = socket.id
    this.sendEvent('boardUpdate')
    this.sendEvent('dataUpdate')
    this.listenForEvents()
  }

  /**
   * Once a play is made, the player's hand is updated
   * @param {Array} tilesUsed - tiles that were used in a play
   */
  updateHand(tilesUsed) {
    this.removeTiles(tilesUsed)
    this.addToHand()
  }

  /**
   * Puts new tiles into the player's hand
   */
  addToHand() {
    let newLetters = []
    let lettersToGenerate = 7 - this._tiles.length

    // generate the new letters
    for (let a = 0; a < lettersToGenerate; a++) {
      let index = Math.floor(Math.random() * ld.totalLetters)

      for (let i = 0; i < ld.intervals.length; i++) {
        if (index <= ld.intervals[i]) {
          newLetters.push(ld.letters[i])
          break
        }
      }
    }

    this._tiles.push(...newLetters)
  }

  /**
   * Removes information
   */
  removeInformation() {
    dg(`${this.name} disconnected from player manager ${this.position}`, 'debug')
    this._name = null
    this._team = null
    this._link = null
    this._isAI = null
    this._socket = null
    this._socketId = null
  }

  /**
   * Removes tiles from hand
   * @param {Array} tiles - array of tiles to remove
   */
  removeTiles(tilesToBeRemoved) {
    let currentHand = this._tiles

    for (let t = tilesToBeRemoved.length - 1; t >= 0; t--) {
      let tile = tilesToBeRemoved[t]
      for (let i = currentHand.length - 1; i >= 0; i--) {
        let letter = currentHand[i]
        if (tile === letter) {
          currentHand.splice(i, 1)
          tilesToBeRemoved.splice(t, 1)
          break
        }
      }
    }

    this._tiles = currentHand
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
