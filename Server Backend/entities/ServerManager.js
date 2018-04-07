'use strict'
/**
 * Imports the GameManager, PlayerManager, FrontendManager, and Debug classes
 */
const GameManager = require('./GameManager')
const PlayerManager = require('./PlayerManager')
const FrontendManager = require('./FrontendManager')
require('../helpers/Debug')

module.exports = function(io) {
  class ServerManager {
    constructor() {
      this._gameManager = null
      this._frontendManager = null
      this._playerManagers = []
      this._currentlyConnectedClients = 0
      this._maxPlayers = 4
      this._firstTurnSet = false
      this.init()
    }

    /**
     * Initializes game manager
     */
    init() {
      this.createGameManager()
      this.createPlayerManagers()
      this.listenForClients()
    }

    /**
     * Listens to socket events that happen
     */
    listenForClients() {
      io.on('connection', socket => {
        console.log('DBEUG: INCOMING CONNECTION'.debug)
        if (this._currentlyConnectedClients !== this._maxPlayers) {
          socket.emit('whoAreYou')

          socket.on('whoAreYou', response => {
            console.log('DEBUG: ASKING CLIENT WHO THEY ARE'.debug)
            this.determineClientType(socket, response)
          })

          socket.on('playWord', board => {
            this.determineWhoMadePlay(socket.id, board)
          })

          socket.on('disconnect', () => {
            this.findClientThatLeft(socket.id)
          })
        } else {
          console.log(`ERROR: THERE ARE ALREADY MAX ${this._maxPlayers} PLAYERS CONNECTED`.error)
          socket.emit('errorMessage', {
            error: 'There are already 4 players connected to the game.'
          })
        }
      })
    }

    /**
     * Creates only one frontend manager instance
     * @param {Object} socket - socket object
     */
    createFrontendManager(socket) {
      if (this._frontendManager === null) {
        this._frontendManager = new FrontendManager(socket)
      }
    }

    /**
     * Creates only one game manager instance
     */
    createGameManager() {
      if (this._gameManager === null) {
        this._gameManager = new GameManager(io, this)
        console.log('DEBUG: GAME MANAGER CREATED'.debug)
      } else {
        console.log('ERROR: GAME MANAGER ALREADY CREATED'.error)
      }
    }

    /**
     * Creates only one game manager instance
     */
    createPlayerManagers() {
      console.log(`DEBUG: CREATING ${this._maxPlayers} PLAYER MANAGERS`.debug)
      for (let i = 0; i < this._maxPlayers; i++) {
        this._playerManagers.push(new PlayerManager(i))
        console.log(`DEBUG: PLAYER MANAGER ${i} CREATED`.debug)
      }
      console.log(`DEBUG: ${this._maxPlayers} PLAYER MANAGERS CREATED`.debug)
    }

    /**
     * Determines what kind of client has connected to the server
     * @param {Object} socket - socket object
     * @param {Object} response - the type of the player
     */
    determineClientType(socket, response) {
      if (response.isAI) {
        this.addClientToManager('ai_test', 'team_test', true, socket)
        console.log(`INFO: AI ${'ai_test'.warn} CONNECTED`.info)
      } else if (response.isSF) {
        this.createFrontendManager(socket)
        console.log('INFO: SERVER FRONTEND CONNECTED'.info)
      } else if (response.isClient) {
        this.addClientToManager('client_test', 'team_test', false, socket)
        console.log(`INFO: CLIENT ${'client_test'.warn} CONNECTED`.info)
      }
    }

    /**
     * Removes a player from a manager once they leave the game
     * @param {String} socketId - id of socket
     */
    findClientThatLeft(id) {
      console.log('DEBUG: FINDING CLIENT TO REMOVE'.debug)
      for (let manager of this._playerManagers) {
        if (manager.id === id) {
          console.log(`INFO: PLAYER ${`${manager.name}`.warn} DISCONNECTED FROM ${'-->'.arrow} PLAYER MANAGER ${`${manager.position}`.warn}`.info)
          this._currentlyConnectedClients--
          manager.removePlayerInformation()
          return
        }
      }

      if (this._frontendManager.id === id) {
        console.log('INFO: SERVER FRONTEND DISCONNECTED'.info)
        this._frontendManager = null
      }
    }

    /**
     * Adds a client to a PlayerManager that does not have any data inside of it
     * @param {String} name - name of player
     * @param {String} team - team player is on
     * @param {Boolean} isAI - AI or not
     * @param {Object} socket - socket object
     */
    addClientToManager(name, team, isAI, socket) {
      console.log('DEBUG: FINDING MANAGER TO ADD TO'.debug)
      for (let manager of this._playerManagers) {
        if (manager.id === null) {
          manager.createHandshakeWithClient(name, team, isAI, socket)
          console.log(`DEBUG: CLIENT ADDED TO ${'-->'.arrow} PLAYER MANAGER ${`${manager.position}`.warn}`.debug)
          this._currentlyConnectedClients++
          return
        }
      }
    }

    /**
     * Determines who made a play, and then executes it
     * @param {String} id - socket id of player
     * @param {Array} board - board
     */
    determineWhoMadePlay(id, board) {
      for (let manager of this._playerManagers) {
        if (manager.id === id) {
          console.log(`DEBUG: PLAYER ${`${manager.name}`.warn} ATTEMPTING TO MAKE A PLAY...`.debug)
          this._gameManager.play(board, manager)
        }
      }
    }
  }

  return ServerManager
}
