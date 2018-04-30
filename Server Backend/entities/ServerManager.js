'use strict'

const dg = require('../helpers/Debug')(true)
const PlayerManager = require('./PlayerManager')
const GameManager = require('./GameManager')
const FrontendManager = require('./FrontendManager')
const db = require('../helpers/DB')

module.exports = (webSocket) => {
  class ServerManager {
    constructor() {
      this.frontends = []
      this.currentlyConnected = 0
      this.gameManager = new GameManager(this.emitDataUpdate.bind(this), this.emitGameEvent.bind(this), this.updateFrontends.bind(this), this.changeTurn.bind(this), this.gameOverEvent.bind(this))
      this.players = new Array(4).fill(null)
      this.oldPlayerData = new Array(4).fill(null)
      this.queue = []
      this.firstTurnSet = false
      this.listenForClients()
    }

    /**
     * Listens to socket events that happen
     */
    listenForClients() {
      webSocket.on('connection', socket => {
        socket.on('message', response => {
          let r = JSON.parse(response)
          switch (r.event) {
            case 'whoAmI':
              this.determineClientType(r.data.client, socket)
              break
          }
        })

        socket.on('close', z => {
          // TODO: Fix @Landon
          this.removeFromQueue(socket._socket.remoteAddress)
        })
      })
    }

    determineClientType(client, socket) {
      switch (client) {
        case 'SF':
          this.createFrontend(socket)
          break
        case 'AI':
          this.createManager(socket, true)
          break
        case 'CL':
          this.createManager(socket, false)
          break
        case 'Q':
          this.addToQueue(socket)
          break
      }
    }

    // TODO: Check is user is playing already @Landon

    /**
     * Creates a player manager for a connected player
     * @param {String} name - name of the player
     * @param {String} team - player team
     * @param {Boolean} ai - is ai
     * @param {Object} socket - socket object
     */
    createManager(socket, isAI) {
      for (let i = 0; i < this.players.length; i++) {
        let p = this.players[i]
        if (p === null) {
          let board = this.gameManager.board.sendableBoard()
          let player = new PlayerManager(i, socket, isAI, this.clientDisconnect.bind(this), this.gameManager.determineEvent.bind(this.gameManager))
          if (!this.firstTurnSet) {
            player.isTurn = true
            this.firstTurnSet = true
          }
          if (this.oldPlayerData[i] !== null) {
            this.injectOldData(i, player)
          }
          this.players.splice(i, 1, player)
          if (!isAI) {
            this.currentlyConnected++
            player.retrieveDBInfo(this.emitPlayerConnected.bind(this), board)
            this.emitCurrentlyConnected()
          } else {
            player.injectAIData(i, this.emitPlayerConnected.bind(this), board)
          }

          return
        }
      }
      // change to error message
      this.emitErrorMessage(socket, 'too many players connected')
    }

    /**
     * Removes the diconnected client and saves their data
     * @param {String} name - name
     * @param {Number} position - position
     * @param {Object} oldData - client old data
     */
    clientDisconnect(name, position, oldData) {
      dg(`${name} disconnected`, 'debug')
      let isAI = false
      this.currentlyConnected--
      this.addOldData(position, oldData)
      if (this.players[position].isAI) {
        isAI = true
      }
      this.players.splice(position, 1, null)
      if (!isAI) {
        this.emitPlayerLeft(name)
        this.emitCurrentlyConnected()
        this.addAI(position)
      }
    }

    /**
     * Asks frontend to add AI at position
     * @param {Number} position - position
     */
    addAI(position) {
      for (let frontend of this.frontends) {
        frontend.addAI(position)
      }
    }

    /**
     * Asks frontend to remove AI at position
     * @param {Number} position - position
     */
    removeAI(position) {
      for (let frontend of this.frontends) {
        frontend.removeAI(position)
      }
    }

    latestData() {
      let data = this.gameManager.latestData()
      data.players = this.players

      return data
    }

    /**
       * Creates only one frontend manager instance
       */
    createFrontend(socket) {
      let manager = new FrontendManager(socket, this.frontends.length, this.frontendDisconnect.bind(this))
      // TODO: Update this to work with the latest @Landon
      manager.updateState(this.latestData())
      this.frontends.push(manager)
    }

    /**
     * Removes the disconnected front end
     * @param {Number} number - number
     */
    frontendDisconnect(number) {
      this.frontends.splice(number, 1)
      dg(`frontend ${number} disconnected`, 'debug')
    }

    /**
     * Injects old data from a past connected player into a new player manager
     * @param {Number} pos - position of the player
     * @param {PlayerManager} p - manager
     */
    injectOldData(pos, p) {
      let old = this.oldPlayerData[pos]
      p.injectOldData(old.tiles, old.isTurn)
      this.oldPlayerData.splice(pos, 1, null)
    }

    addOldData(pos, p) {
      this.oldPlayerData.splice(pos, 1, {
        tiles: p.tiles,
        isTurn: p.isTurn,
        score: p.score
      })
    }

    addToQueue(socket) {
      let ip = socket._socket.remoteAddress
      this.queue.push({ip: ip, socket: socket})
    }

    removeFromQueue(ip) {
      for (let i = 0; i < this.queue.length; i++) {
        let queued = this.queue[i]
        if (queued.ip === ip) {
          this.queue.splice(i, 1)
          return
        }
      }
    }

    updateFrontends() {
      for (let frontend of this.frontends) {
        frontend.updateState(this.latestData())
      }
    }

    emitFrontendGameEvent(action) {
      for (let frontend of this.frontends) {
        frontend.gameEvent(action)
      }
    }

    emitGameEvent(action) {
      for (let player of this.players) {
        if (player !== null) {
          player.gameEvent(action)
        }
      }
      this.emitFrontendGameEvent(action)
    }

    emitDataUpdate(board) {
      for (let player of this.players) {
        if (player !== null) {
          player.dataUpdate(board)
        }
      }
    }

    emitCurrentlyConnected() {
      let cc = {
        event: 'currentPlayerCount',
        data: {
          amount: this.currentlyConnected
        }
      }
      for (let queued of this.queue) {
        queued.socket.send(JSON.stringify(cc))
      }
    }

    emitPlayerConnected(name) {
      for (let player of this.players) {
        if (player !== null) {
          player.gameEvent(`${name} has joined`)
        }
      }
      this.updateFrontends()
      this.emitFrontendGameEvent(`${name} has joined`)
    }

    emitPlayerLeft(name) {
      for (let player of this.players) {
        if (player !== null) {
          player.gameEvent(`${name} has left`)
        }
      }
      this.updateFrontends()
      this.emitFrontendGameEvent(`${name} has left`)
    }

    /**
     * Emits error message to specified player
     * @param {Object} socket - socket
     * @param {String} message - message
     */
    emitErrorMessage(socket, message) {
      let error = {
        type: 'errorMessage',
        data: {
          error: message
        }
      }
      socket.send(JSON.stringify(error))
    }

    changeTurn(position) {
      this.players[position].isTurn = false
      do {
        position++
        if (position > 3) {
          position = 0
        }
      } while (this.players[position] === null)
      this.players[position].isTurn = true
      dg(`it is now ${this.players[position].name}'s turn`, 'debug')
      this.emitDataUpdate(this.gameManager.board.sendableBoard())
      this.gameManager.afterTurn()
    }

    gameOverEvent() {
      let finalScores = []
      let winner = null
      let highestScore = 0
      for (let manager of this.players) {
        if (manager.id !== null) {
          if (manager.score > highestScore) {
            highestScore = manager.score
            winner = manager.name
          }
          let data = {
            name: manager.name,
            score: manager.score
          }
          finalScores.push(data)
        }
      }
      let goldWin = this._goldScore > this._greenScore
      db.updateWin('Gold', this._goldScore, goldWin)
      db.updateWin('Green', this._greenScore, !goldWin)

      let gameOverData = {
        scores: finalScores,
        winner: winner === null ? 'No one!' : winner,
        winningTeam: goldWin ? 'Gold' : 'Green'
      }

      for (let player of this.players) {
        if (player !== null) {
          player.isTurn = false
          player.dataUpdate(this.gameManager.board.sendableBoard())
          player.gameOver(gameOverData)
        }
      }

      for (let frontend of this.frontends) {
        frontend.gameOver(gameOverData)
      }

      let timeUntil = 5
      // TODO: See if this can be moved to game event? @Landon
      let timer = setInterval(() => {
        if (timeUntil !== 0) {
          dg(`${timeUntil}`, 'debug')
          for (let player of this.players) {
            if (player !== null) {
              this.emitGameEvent(`New game starts in ${timeUntil}`)
            }
          }
          timeUntil--
        } else {
          clearInterval(timer)
          dg('new game started!', 'info')
          for (let frontend of this.frontends) {
            frontend.sendEvent('newGame')
            this.startNewGame()
          }
        }
      }, 1000)
    }

    /**
   * Starts a new game by resetting everything
   */
    startNewGame() {
      this.resetPlayers()
      this.resetGameboard()
      this.players[0].isTurn = true
      this.emitDataUpdate(this.gameManager.board.sendableBoard())
      this.updateFrontends()
      this.emitGameEvent('New game started')
    }

    /**
   * Creates a new gameboard and initializes it
   */
    resetGameboard() {
      this.gameManager.resetGameboard()
    }

    /**
   * Resets all players' scores
   */
    resetPlayers() {
      this.players.map(p => {
        p.resetScore()
        p.updateHand(p.tiles)
      })
    }
  }

  return ServerManager
}
