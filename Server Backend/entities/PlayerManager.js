'use strict'

/**
 * Imports files
 */
const ld = require('../helpers/LetterDistributor')
const dg = require('../helpers/Debug')(true)
const db = require('../helpers/DB')
const mg = require('../helpers/MacGrabber')

class PlayerManager {
  constructor(position, socket, isAI, disconnect, gamePlays) {
    this._name = null
    this._team = null
    this._isAI = isAI
    this._link = null
    this._socket = socket
    this._position = position
    this._tiles = []
    this._isTurn = false
    this._score = 0
    this.disconnect = disconnect
    this.gamePlays = gamePlays
  }

  /**
   * Tile setter
   */
  set tiles(tiles) {
    this._tiles = tiles
  }

  /**
   * Name setter
   */
  set name(name) {
    this._name = name
  }

  /**
   * Turn setter
   */
  set isTurn(turn) {
    this._isTurn = turn
  }

  /**
   * Link setter
   */
  set link(link) {
    this._link = link
  }

  /**
   * Team setter
   */
  set team(team) {
    this._team = team
  }

  /**
   * Socket setter
   */
  set socket(socket) {
    this._socket = socket
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
   *
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

  /**
   * Retrieves the player's information from the DB
   */
  retrieveDBInfo(callback) {
    mg(this.socket._socket.remoteAddress, (mac) => {
      db.checkIfUserExists(mac)
        .then(r => {
          if (db.pruneResults(r)) {
            db.getTeamURL(mac)
              .then(r2 => {
                let user = {
                  username: r[0].username,
                  team: r2 === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green',
                  link: r[0]._links.self.href
                }
                this.injectDatabaseData(user.username, user.team, user.link)
                callback(this.name)
              })
          } else {
            // TODO: Tell player that they need to login/register first
          }
        })
        .catch(e => {
          console.log(e)
        })
    })
  }

  /**
   * Injects data of old player
   * @param {Array} tiles - tiles
   * @param {Boolean} isTurn - turn
   */
  injectOldData(tiles, isTurn) {
    this.tiles = tiles
    this.isTurn = isTurn
  }

  /**
   * Injects pulled from the database
   * @param {String} name - name
   * @param {team} team - team
   * @param {URL} link - player DB url
   */
  injectDatabaseData(name, team, link) {
    this.name = name
    this.team = team
    this.link = link
    dg(`${name} connected`, 'debug')
    this.setUp()
  }

  injectAIData(number, callback) {
    this.name = `AI_${number}`
    let random = Math.floor(Math.random() * 2)
    this.team = random === 0 ? 'Gold' : 'Green'
    dg(`${this.name} connected`, 'debug')
    callback(this.name)
    this.setUp()
  }

  setUp() {
    // TODO: Fix this @Landon
    // this.sendEvent('boardUpdate', null)
    this.addToHand()
    this.sendEvent('dataUpdate')
    this.listenForIncoming()
  }

  /**
   * Listen for incoming events
   */
  listenForIncoming() {
    this.socket.on('close', z => {
      this.disconnect(this.name, this.position, this.oldDataSave())
    })

    this.socket.on('message', event => {
      this.determineEvent(event)
    })
  }

  /**
   * Grabs data to save for new player
   */
  oldDataSave() {
    return {
      tiles: this.tiles,
      isTurn: this.isTurn
    }
  }

  /**
   * Determines the event the player made
   * @param {String} event - event
   */
  determineEvent(event) {
    let e = JSON.parse(event)
    switch (e.event) {
      case 'playWord':
        dg(`${this.name} made play`, 'debug')
        this.gamePlays('play', this, e.newBoard)
        break
      case 'swap':
        dg(`${this.name} swapped`, 'info')
        this.updateHand(this.tiles)
        this.gamePlays('swap', this)
        break
    }
  }

  /**
   * Sends event
   * @param {String} event = event
   * @param {Object} data - data
   */
  sendEvent(event, data) {
    let eventData = {
      event: event,
      data: {}
    }

    switch (event) {
      case 'play':
        eventData.data = data
        break
      case 'dataUpdate':
        eventData.data = {
          name: this.name,
          position: this.position,
          tiles: this.tiles,
          isTurn: this.isTurn,
          score: this.score
        }
        break
      case 'gameEvent':
        eventData.data = {
          action: data,
          bonus: false
        }
        break
      case 'boardUpdate':
      // TODO: Check if this can be removed
        eventData.data = {
          board: data.board,
          yellow: data.yellow,
          green: data.green
        }
        break
    }

    this.socket.send(JSON.stringify(eventData))
  }

  gameEvent(data) {
    this.sendEvent('gameEvent', data)
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
  createHandshakeWithClient(name, team, link, isAI, socket, data) {
    this._name = name
    this._team = team
    this._link = link
    this._isAI = isAI
    this._socket = socket
    this._socketId = socket.id
    this.sendEvent('boardUpdate', data)
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
