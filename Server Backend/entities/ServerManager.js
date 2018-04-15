'use strict'
/**
 * Imports files
 */
const GameManager = require('./GameManager')
const PlayerManager = require('./PlayerManager')
const FrontendManager = require('./FrontendManager')
const dg = require('../helpers/Debug')

module.exports = function(io) {
  class ServerManager {
    constructor() {
      this._gameManager = null
      this._frontendManager = null
      this._playerManagers = []
      this._players = 0
      this._ai = 0

      this.init()
    }

    /**
     * Initializes managers
     */
    init() {
      this.createFrontendManager()
      this.createGameManager()
      this.createPlayerManagers()
      this.listenForClients()
    }

    /**
     * Creates only one frontend manager instance
     */
    createFrontendManager() {
      if (this._frontendManager === null) {
        this._frontendManager = new FrontendManager()
        dg('frontend created', 'debug')
      }
    }

    /**
     * Creates only one game manager instance
     */
    createGameManager() {
      if (this._gameManager === null) {
        this._gameManager = new GameManager(io)
        dg('game manager created', 'debug')
      }
    }

    /**
     * Creates 4 player managers
     */
    createPlayerManagers() {
      dg(`creating 4 player managers`, 'debug')
      for (let i = 0; i < this._maxPlayers; i++) {
        this._playerManagers.push(new PlayerManager(i, this._gameManager))
      }
      dg('player managers created', 'debug')
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
        this.addClientToManager('ai_test', 'team_test', true, socket)
        this._ai++
        dg('ai connected', 'info')
      } else if (response.isSF) {
        this._frontendManager.createHandshakeWithFrontend(socket)
        dg('server frontend connected', 'info')
      } else if (response.isClient) {
        if (!this.addClientToManager('client_test', 'team_test', false, socket)) {
          this.removeAIPlayer('client_test', 'team_test', socket)
        }
        this._players++
        dg('client connected', 'info')
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
      for (let manager of this._playerManagers) {
        if (manager.id === null) {
          manager.createHandshakeWithClient(name, team, isAI, socket)
          this.updateFrontendData()
          dg(`client added to player manager ${manager.position}`, 'debug')
          return true
        }
        return false
      }
    }

    /**
     * Removes an AI player if an actual person tries to connect and there are AI playing
     * @param {String} name - name of player
     * @param {String} team - name of team
     * @param {Object} socket - socket object
     */
    removeAIPlayer(name, team, socket) {
      for (let manager of this._playerManagers) {
        if (this._frontendManager.socketId !== null && manager.isAI) {
          dg(`removing ai from position ${manager.position}`, 'debug')
          this._frontendManager.sendEvent('removeAI', manager.position)
          this._ai--
          manager.removePlayerInformation()
          this.addClientToManager(name, team, false, socket)
          return
        }
      }
    }

    /**
     * Removes a player from a manager once they leave the game
     * @param {String} socketId - id of socket
     */
    // findClientThatLeft(id) {
    //   console.log('DEBUG: FINDING CLIENT TO REMOVE'.debug)
    //   for (let manager of this._playerManagers) {
    //     if (manager.id === id) {
    //       console.log(`INFO: PLAYER ${`${manager.name}`.warn} DISCONNECTED FROM ${'-->'.arrow} PLAYER MANAGER ${`${manager.position}`.warn}`.info)
    //       if (this._frontendManager !== null && !manager.isAI) {
    //         this._frontendManager.sendEvent('connectAI', manager.position)
    //       }
    //       this._currentlyConnectedClients--
    //       manager.removePlayerInformation()
    //       this.updateFrontendData()
    //       return
    //     }
    //   }

    //   if (this._frontendManager !== null && this._frontendManager.id === id) {
    //     console.log('INFO: SERVER FRONTEND DISCONNECTED'.info)
    //     this._frontendManager = null
    //   }
    // }

    /**
     * Determines who made a play, and then executes it
     * @param {String} id - socket id of player
     * @param {Array} board - board
     */
    // determineWhoMadePlay(id, board) {
    //   for (let manager of this._playerManagers) {
    //     if (manager.id === id) {
    //       console.log(`DEBUG: PLAYER ${`${manager.name}`.warn} ATTEMPTING TO MAKE A PLAY...`.debug)
    //       this._gameManager.play(board, manager, (response) => {
    //         if (response) {
    //           this.updateTurn(manager)
    //         }
    //       })
    //       return
    //     }
    //   }
    // }

    // determineWhoSwapped(id, letters) {
    //   for (let manager of this._playerManagers) {
    //     if (manager.id === id) {
    //       console.log(`INFO: PLAYER ${`${manager.name}`.warn} HAS SWAPPED TILES`.info)
    //       this._swaps++
    //       if (this._swaps === this._currentlyConnectedClients) {
    //         console.log(`INFO: ALL PLAYERS HAVE SWAPPED, GAME OVER`.info)
    //         for (let manager of this._playerManagers) {
    //           if (manager.id !== null) {
    //             manager.isTurn = false
    //             manager.sendEvent('dataUpdate')
    //           }
    //         }
    //         let finalScores = []
    //         let winner = null
    //         let highestScore = 0
    //         for (let manager of this._playerManagers) {
    //           if (manager.id !== null) {
    //             if (manager.score > highestScore) {
    //               highestScore = manager.score
    //               winner = manager.name
    //             }
    //             let data = {
    //               name: manager.name,
    //               score: manager.score
    //             }
    //             finalScores.push(data)
    //           }
    //         }
    //         io.emit('gameOver', {
    //           scores: finalScores,
    //           winner: winner,
    //           winningTeam: this._gameManager._yellowScore > this._gameManager._greenScore ? 'Yellow' : 'Green'
    //         })
    //         return
    //       }
    //       console.log(`DEBUG: ${JSON.stringify(letters, null, 4)}`)
    //       manager.manipulateHand(letters)
    //       this.updateTurn(manager, true)
    //     }
    //   }
    // }

    /**
     * Updates who's turn it is
     */
    // updateTurn(manager, swapped) {
    //   let position = manager.position
    //   console.log(`INFO: IT WAS PLAYER ${`${position}`.warn}'s TURN`.info)
    //   manager.isTurn = false
    //   do {
    //     position++
    //     if (position > 3) {
    //       position = 0
    //     }
    //   } while (this._playerManagers[position].id === null)
    //   this._playerManagers[position].isTurn = true
    //   if (!swapped) {
    //     this._swaps = 0
    //   }
    //   console.log(`INFO: IT IS NOW PLAYER ${`${position}`.warn}'s TURN`.info)

    //   this.updateClientData()
    //   this.updateFrontendData()
    // }

    /**
     * Updates clients' data
     */
    // updateClientData() {
    //   for (let manager of this._playerManagers) {
    //     if (manager.id !== null) {
    //       manager.sendEvent('dataUpdate')
    //     }
    //   }
    // }

    /**
     * Updates the frontend
     */
    updateFrontendData() {
      this._frontendManager.updateGameInformation(this._gameManager.board.sendableBoard(), this._playerManagers.map(player => {
        return player.sendableData()
      }))

      this._frontendManager.sendEvent('updateState')
    }
  }

  return ServerManager
}
