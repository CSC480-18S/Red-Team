'use strict'
/**
 * Imports the GameManager, PlayerManager, and FrontendManager classes
 */
const GameManager = require('./GameManager')
const PlayerManager = require('./PlayerManager')
const FrontendManager = require('./FrontendManager')

module.exports = function(io) {
  class ServerManager {
    constructor() {
      this._gameManager = null
      this._frontendManager = null
      this._players = new Array(4).fill(null)
      this._oldPlayerData = new Array(4).fill(null)
      this._currentlyConnectedClients = 0
      this.init()
      this.listenForClients()
    }

    /**
     * Initializes player and game managers
     */
    init() {
      this.createGameManager()
    }

    /**
     * Listens to socket events that happen
     */
    listenForClients() {
      io.on('connection', socket => {
        if (this._currentlyConnectedClients !== 4) {
          socket.emit('whoAreYou')

          socket.on('whoAreYou', response => {
            this.determineClientType(socket, response)
          })

          socket.on('disconnect', () => {
            if (this._frontendManager !== null) {
              let playerPosition = this.findWhichPlayerLeft(socket)
              this._frontendManager.askForAI(playerPosition)
            }
          })
        } else {
          socket.emit('errorMessage', {
            error: 'There are already 4 players connected to the game.'
          })
        }
      })
    }

    /**
     * Finds which player has disconnected from the game based on their socket
     * @param {Object} socket - socket object
     */
    findWhichPlayerLeft(socket) {
      for (let p of this._players) {
        if (p.socket === socket) {
          console.log('Client has disconnected.')
          let pos = p.position
          this.addOldData(pos, p)
          this._players.splice(pos, 1, null)
          this._currentlyConnectedClients--
          return p.position
        }
      }
    }

    addOldData(pos, p) {
      this._oldPlayerData.splice(pos, 1, {
        tiles: p.tiles,
        isTurn: p.isTurn,
        score: p.score
      })
    }

    /**
     * Creates 4 player managers
     */
    createPlayerManagers() {
      for (let i = 0; i < 4; i++) {
        this._players.push(new PlayerManager(i))
      }
    }

    /**
     * Creates only one frontend manager instance
     * @param {Object} socket - socket object
     */
    createFrontendManager(socket) {
      if (this._frontendManager === null) {
        console.log('Server Frontend Connected')
        this._frontendManager = new FrontendManager(socket)
      }
    }

    /**
     * Creates only one game manager instance
     */
    createGameManager() {
      if (this._gameManager === null) {
        this._gameManager = new GameManager()
      }
    }

    /**
     * Determines what kind of client has connected to the server
     * @param {Object} socket - socket object
     * @param {*} response - the type of the player
     */
    determineClientType(socket, response) {
      if (response.tisAI) {
        this.createPlayerManager('ai_test', 'team_test', true, socket)
      } else if (response.isSF) {
        this.createFrontendManager(socket)
      } else if (response.isClient) {
        this.createPlayerManager('client_test', 'team_test', false, socket)
      }
    }

    /**
     * Creates a player manager for a connected player
     * @param {String} name - name of the player
     * @param {String} team - player team
     * @param {Boolean} ai - is ai
     * @param {Object} socket - socket object
     */
    createPlayerManager(name, team, ai, socket) {
      for (let i = 0; i < this._players.length; i++) {
        let p = this._players[i]
        if (p === null) {
          let player = new PlayerManager(i, 'Player #' + i, team, ai, socket)
          if (this._oldPlayerData[i] !== null) {
            this.injectOldData(i, player)
          }
          this._players.splice(i, 1, player)
          break
        }
      }
    }

    /**
     * Injects old data from a past connected player into a new player manager
     * @param {Number} pos - position of the player
     * @param {PlayerManager} p - manager
     */
    injectOldData(pos, p) {
      let old = this._oldPlayerData[pos]
      p.addPositionDetails(old.tiles, old.isTurn, old.score)
      this._oldPlayerData.splice(pos, 1, null)
    }
  }

  return ServerManager
}
