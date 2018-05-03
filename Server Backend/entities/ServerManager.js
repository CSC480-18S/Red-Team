'use strict'

const dg = require('../helpers/Debug')(true)
const GameManager = require('./GameManager')
const SocketManager = require('./SocketManager')
const si = require('shortid')

function ServerManager(ws) {
  this.ws = ws
  this.gameManager = null
  this.socketManager = null
}

ServerManager.prototype.init = function() {
  this.socketManager = SocketManager()
  this.gameManager = GameManager(this.socketManager)
  this.listenForClients()
}

ServerManager.prototype.listenForClients = function() {
  this.ws.on('connection', socket => {
    socket.on('message', m => {
      let message = this.getMessage(m)
      switch (message.event) {
        case 'whoAmI':
          this.attemptChannelAdd(message.data.client, socket)
          break
      }
    })

    socket.on('close', () => {
      let client = this.socketManager.getClient(socket.id)
      this.socketManager.removeFromChannel(client.channel, socket.id)
      this.socketManager.broadcast('Queued', 'currentlyConnected', this.generateAmount(this.amountOfClients()))
    })
  })
}

ServerManager.prototype.attemptChannelAdd = function(client, socket) {
  let channel = null
  let id = si.generate()

  socket.id = id

  switch (client) {
    case 'AI':
      channel = 'AIs'
      break
    case 'CL':
      channel = 'Clients'
      break
    case 'SF':
      channel = 'SFs'
      break
    case 'Q':
      channel = 'Queued'
      break
    default:
      channel = 'Error'
  }

  let success = this.socketManager.addToChannel(channel, id, socket)

  this.checkChannelAdd(success, channel, id, socket)
}

ServerManager.prototype.checkChannelAdd = function(success, channel, id, socket) {
  if (success) {
    let event = null
    let data = null

    switch (channel) {
      case 'AIs':
        this.gameManager.addPlayer(id, socket, true)
        break
      case 'Clients':
        this.gameManager.addPlayer(id, socket, false)
        break
      case 'SFs':
        event = 'updateState'
        // TODO: Fix @Landon
        data = this.gameManager.latestData()
        break
      case 'Error':
        event = 'errorMessage'
        data = this.generateError('There seems to be an error.')
        break
    }
    // TODO: Update frontend @Landon
    event = 'currentlyConnected'
    data = this.generateAmount(this.amountOfClients())
    this.socketManager.broadcast('Queued', event, data)
  } else {
    channel = 'Error'
    this.socketManager.addToChannel(channel, id, socket)
    this.socketManager.emit(channel, id, 'errorMessage', this.generateError('There are already 4 players conencted.'))
  }

  dg(`id: ${id} -> connected to channel: (${channel})`, 'info')
}

ServerManager.prototype.amountOfClients = function() {
  return this.socketManager.channelClientAmount('Clients')
}

ServerManager.prototype.generateAmount = function(number) {
  let amount = {
    amount: number
  }

  return JSON.stringify(amount)
}

ServerManager.prototype.generateError = function(message) {
  let error = {
    error: message
  }

  return JSON.stringify(error)
}

ServerManager.prototype.getMessage = function(message) {
  return JSON.parse(message)
}

module.exports = (ws) => {
  return new ServerManager(ws)
}

//   // TODO: Check is user is playing already @Landon

//   killAI() {
//     for (let i = 0; i < this.players.length; i++) {
//       let p = this.players[i]
//       if (p === null && p.isAI) {
//         dg(`removing ai_${i}`)
//         this.removeAI(i)
//         this.clientDisconnect(p.name, p.position, p.oldDataSave())
//         return
//       }
//     }
//   }

//   /**
//    * Removes the diconnected client and saves their data
//    * @param {String} name - name
//    * @param {Number} position - position
//    * @param {Object} oldData - client old data
//    */
//   clientDisconnect(name, position, oldData) {
//     dg(`${name} disconnected`, 'debug')
//     let isAI = false
//     this.currentlyConnected--
//     this.addOldData(position, oldData)
//     if (this.players[position].isAI) {
//       isAI = true
//     }
//     this.players.splice(position, 1, null)
//     if (!isAI) {
//       this.emitPlayerLeft(name)
//       this.emitCurrentlyConnected()
//       this.addAI(position)
//     }
//   }

//   /**
//    * Asks frontend to add AI at position
//    * @param {Number} position - position
//    */
//   addAI(position) {
//     for (let frontend of this.frontends) {
//       frontend.addAI(position)
//     }
//   }

//   /**
//    * Asks frontend to remove AI at position
//    * @param {Number} position - position
//    */
//   removeAI(position) {
//     for (let frontend of this.frontends) {
//       frontend.removeAI(position)
//     }
//   }

//   latestData() {
//     let data = this.gameManager.latestData()
//     data.players = this.players

//     return data
//   }

//   /**
//      * Creates only one frontend manager instance
//      */
//   createFrontend(socket) {
//     let manager = new FrontendManager(socket, this.frontends.length, this.frontendDisconnect.bind(this))
//     // TODO: Update this to work with the latest @Landon
//     manager.updateState(this.latestData())
//     this.frontends.push(manager)
//   }

//   /**
//    * Removes the disconnected front end
//    * @param {Number} number - number
//    */
//   frontendDisconnect(number) {
//     this.frontends.splice(number, 1)
//     dg(`frontend ${number} disconnected`, 'debug')
//   }

//   /**
//    * Injects old data from a past connected player into a new player manager
//    * @param {Number} pos - position of the player
//    * @param {PlayerManager} p - manager
//    */
//   injectOldData(pos, p) {
//     let old = this.oldPlayerData[pos]
//     p.injectOldData(old.tiles, old.isTurn)
//     this.oldPlayerData.splice(pos, 1, null)
//   }

//   addOldData(pos, p) {
//     this.oldPlayerData.splice(pos, 1, {
//       tiles: p.tiles,
//       isTurn: p.isTurn,
//       score: p.score
//     })
//   }

//   addToQueue(socket) {
//     let ip = socket._socket.remoteAddress
//     this.queue.push({ip: ip, socket: socket})
//   }

//   removeFromQueue(ip) {
//     for (let i = 0; i < this.queue.length; i++) {
//       let queued = this.queue[i]
//       if (queued.ip === ip) {
//         this.queue.splice(i, 1)
//         return
//       }
//     }
//   }

//   updateFrontends() {
//     for (let frontend of this.frontends) {
//       frontend.updateState(this.latestData())
//     }
//   }

//   emitFrontendGameEvent(action) {
//     for (let frontend of this.frontends) {
//       frontend.gameEvent(action)
//     }
//   }

//   emitGameEvent(action) {
//     for (let player of this.players) {
//       if (player !== null) {
//         player.gameEvent(action)
//       }
//     }
//     this.emitFrontendGameEvent(action)
//   }

//   emitDataUpdate(board) {
//     for (let player of this.players) {
//       if (player !== null) {
//         player.dataUpdate(board)
//       }
//     }
//   }

//   emitCurrentlyConnected() {
//     let cc = {
//       event: 'currentPlayerCount',
//       data: {
//         amount: this.currentlyConnected
//       }
//     }
//     for (let queued of this.queue) {
//       queued.socket.send(JSON.stringify(cc))
//     }
//   }

//   emitPlayerConnected(name) {
//     for (let player of this.players) {
//       if (player !== null) {
//         player.gameEvent(`${name} has joined`)
//       }
//     }
//     this.updateFrontends()
//     this.emitFrontendGameEvent(`${name} has joined`)
//   }

//   emitPlayerLeft(name) {
//     for (let player of this.players) {
//       if (player !== null) {
//         player.gameEvent(`${name} has left`)
//       }
//     }
//     this.updateFrontends()
//     this.emitFrontendGameEvent(`${name} has left`)
//   }

//   /**
//    * Emits error message to specified player
//    * @param {Object} socket - socket
//    * @param {String} message - message
//    */
//   emitErrorMessage(socket, message) {
//     let error = {
//       type: 'errorMessage',
//       data: {
//         error: message
//       }
//     }
//     socket.send(JSON.stringify(error))
//   }

//   changeTurn(position) {
//     this.players[position].isTurn = false
//     do {
//       position++
//       if (position > 3) {
//         position = 0
//       }
//     } while (this.players[position] === null)
//     this.players[position].isTurn = true
//     dg(`it is now ${this.players[position].name}'s turn`, 'debug')
//     this.emitDataUpdate(this.gameManager.board.sendableBoard())
//     this.gameManager.afterTurn()
//     this.gameManager.playTimer(true)
//     this.gameManager.playTimer(false, this.players[position])
//   }

//   gameOverEvent() {
//     let finalScores = []
//     let winner = null
//     let highestScore = 0
//     for (let player of this.players) {
//       if (player !== null) {
//         if (player.score > highestScore) {
//           highestScore = player.score
//           winner = player.name
//         }
//         let data = {
//           name: player.name,
//           score: player.score
//         }
//         finalScores.push(data)
//       }
//     }
//     let latestData = this.latestData()
//     let goldWin = latestData.gold > latestData.green
//     db.updateWin('Gold', this._goldScore, goldWin)
//     db.updateWin('Green', this._greenScore, !goldWin)

//     let gameOverData = {
//       scores: finalScores,
//       winner: winner === null ? 'No one!' : winner,
//       winningTeam: goldWin ? 'Gold' : 'Green'
//     }

//     for (let player of this.players) {
//       if (player !== null) {
//         player.isTurn = false
//         player.dataUpdate(this.gameManager.board.sendableBoard())
//         player.gameOver(gameOverData)
//       }
//     }

//     for (let frontend of this.frontends) {
//       frontend.gameOver(gameOverData)
//     }

//     let timeUntil = 5
//     // TODO: See if this can be moved to game event? @Landon
//     let timer = setInterval(() => {
//       if (timeUntil !== 0) {
//         dg(`${timeUntil}`, 'debug')
//         for (let player of this.players) {
//           if (player !== null) {
//             this.emitGameEvent(`New game starts in ${timeUntil}`)
//           }
//         }
//         timeUntil--
//       } else {
//         clearInterval(timer)
//         dg('new game started!', 'info')
//         for (let frontend of this.frontends) {
//           frontend.sendEvent('newGame')
//           this.startNewGame()
//         }
//       }
//     }, 1000)
//   }

//   /**
//  * Starts a new game by resetting everything
//  */
//   startNewGame() {
//     this.resetPlayers()
//     this.resetGameboard()
//     this.players[0].isTurn = true
//     this.emitDataUpdate(this.gameManager.board.sendableBoard())
//     this.updateFrontends()
//     this.emitGameEvent('New game started')
//   }

// return ServerManager
