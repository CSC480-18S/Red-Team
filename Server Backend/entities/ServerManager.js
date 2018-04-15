'use strict'
/**
 * Imports files
 */
const GameManager = require('./GameManager')
const dg = require('../helpers/Debug')

module.exports = function(io) {
  class ServerManager {
    constructor() {
      this._gameManager = null
      this._players = 0
      this._ai = 0

      this.init()
    }

    /**
     * Initializes managers
     */
    init() {
      this.createGameManager()
      this.listenForClients()
    }

    /**
     * Creates only one game manager instance
     */
    createGameManager() {
      if (this._gameManager === null) {
        this._gameManager = new GameManager(io, this)
        dg('game manager created', 'debug')
      }
    }

    /**
     * Listens to socket events that happen
     */
    listenForClients() {
      io.on('connection', socket => {
        socket.emit('whoAreYou')

        socket.on('whoAreYou', response => {
          dg('asking client who they are', 'debug')
          this.determineClientType(socket, response)
        })
      })
    }

    /**
     * Determines what kind of client has connected to the server
     * @param {Object} socket - socket object
     * @param {Object} response - the type of the player
     */
    determineClientType(socket, response) {
      if (response.isAI) {
        this._gameManager.addPlayer('ai_test', 'team_test', true, socket)
        this.updateConnectionCounts('a', 1)
        dg('ai connected', 'info')
      } else if (response.isSF) {
        this._gameManager.addFrontend(socket)
        dg('server frontend connected', 'info')
      } else if (response.isClient) {
        if (!this._gameManager.addPlayer('client_test', 'team_test', false, socket)) {
          this._gameManager.removeAI('client_test', 'team_test', socket)
          this.updateConnectionCounts('a', -1)
        }
        this.updateConnectionCounts('p', 1)
        dg('client connected', 'info')
      }
    }

    /**
     * Adds or subtracts player and ai and sends it out to clients
     * @param {String} type - p for player or a for ai
     * @param {Number} amount - +1 or -1 based
     */
    updateConnectionCounts(type, amount) {
      switch (type) {
        case 'p':
          this._players += amount
          break
        case 'a':
          this._ai += amount
          break
      }

      io.emit('connections', {
        players: this._players,
        ai: this._ai
      })
    }
  }

  return ServerManager
}
