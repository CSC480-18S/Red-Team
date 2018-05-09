'use strict'

const dg = require('../helpers/Debug')(true)
const GameManager = require('./GameManager')
const SocketManager = require('./SocketManager')
const si = require('shortid')
const db = require('../helpers/DB')
const mg = require('../helpers/MacGrabber')

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

  if (channel === 'Clients') {
    this.grabClientInfo(socket, (result) => {
      if (!result) {
        channel = 'Error'
      } else {
        let success = this.socketManager.addToChannel(channel, id, socket)

        this.checkChannelAdd(success, channel, id, socket, result)
      }
    })
  } else {
    let success = this.socketManager.addToChannel(channel, id, socket)

    this.checkChannelAdd(success, channel, id, socket)
  }
}

ServerManager.prototype.checkChannelAdd = function(success, channel, id, socket, data) {
  if (success) {
    let event = null
    let message = null

    switch (channel) {
      case 'AIs':
        this.gameManager.addPlayer(id, socket, true)
        break
      case 'Clients':
        this.gameManager.addPlayer(id, socket, false, data)
        break
      case 'SFs':
        event = 'updateState'
        message = this.gameManager.updateStateData()
        this.socketManager.connectAI(true)
        break
      case 'Error':
        event = 'errorMessage'
        message = this.generateError('There seems to be an error.')
        this.socketManager.emit(id, event, message)
        return
    }
    if (['AIs', 'Clients', 'Queued'].includes(channel)) {
      this.socketManager.broadcast('Queued', 'currentlyConnected', this.generateAmount(this.amountOfClients()))
    }
    this.socketManager.broadcast('SFs', event, message)
  } else {
    channel = 'Error'
    let message = this.generateError('There are already 4 players conencted.')
    this.socketManager.addToChannel(channel, id, socket)
    this.socketManager.emit(id, 'errorMessage', message)
  }

  dg(`id: ${id} -> connected to channel: (${channel})`, 'info')
}

ServerManager.prototype.grabClientInfo = function(socket, callback) {
  mg(socket._socket.remoteAddress, (mac) => {
    db.checkIfUserExists(mac)
      .then(r => {
        if (db.pruneResults(r)) {
          db.getTeamURL(mac)
            .then(r2 => {
              let dbData = {
                name: r[0].username,
                team: {
                  link: r2,
                  name: r2 === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green'
                },
                link: r[0]._links.self.href
              }

              dg(`DB data grabbed -> ${socket.id}`, 'debug')

              callback(dbData)
            })
        } else {
          dg(`Player does not exist in the DB -> ${socket.id}`, 'debug')

          callback(false)
        }
      })
      .catch(e => {
        console.log(e)
      })
  })
}

ServerManager.prototype.amountOfClients = function() {
  return this.socketManager.channelClientAmount('Clients')
}
// TODO: Make sure that payload is generated, but not stringified, the Socket Manager does the stringify @Landon
ServerManager.prototype.generateAmount = function(number) {
  let amount = {
    amount: number
  }

  return amount
}

ServerManager.prototype.generateError = function(message) {
  let error = {
    error: message
  }

  return error
}

ServerManager.prototype.getMessage = function(message) {
  return JSON.parse(message)
}

module.exports = (ws) => {
  return new ServerManager(ws)
}
