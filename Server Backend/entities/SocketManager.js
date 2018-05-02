'use strict'

function SocketManager() {
  this.channels = {}
  this.clients = {}
  this.registerChannel('AIs')
  this.registerChannel('Clients')
  this.registerChannel('SFs')
  this.registerChannel('Queued')
  this.registerChannel('Error')
}

SocketManager.prototype.registerChannel = function(name) {
  this.channels[name] = {clients: {}}
}

SocketManager.prototype.channelList = function() {
  return Object.keys(this.channels)
}

SocketManager.prototype.channelClientAmount = function(channel) {
  return Object.keys(this.channels[channel].clients).length
}

SocketManager.prototype.clientAmount = function() {
  return Object.keys(this.clients).length
}

SocketManager.prototype.addClient = function(channel, id, socket) {
  this.clients[id] = socket
}

SocketManager.prototype.addToChannel = function(channel, id, socket) {
  if (channel === 'Clients') {
    if (this.channelClientAmount(channel) === 4) {
      return false
    }
  }

  this.channels[channel].clients[id] = socket
  this.addClient(channel, id, socket)
  return true
}

SocketManager.prototype.getClient = function(channel, id) {
  let c = this.channels[channel]
  let clients = Object.keys(c.clients)

  for (let client of clients) {
    if (id === client) {
      return this.channels[channel].clients[id]
    }
  }

  return false
}

SocketManager.prototype.removeFromChannel = function(channel, id) {
  let c = this.channels[channel]
  let clients = Object.keys(c.clients)

  for (let client of clients) {
    if (id === client) {
      delete c.clients[id]
      return true
    }
  }

  return false
}

SocketManager.prototype.broadcast = function(channel, event, data) {
  switch (event) {
    case 'updateState':
    case 'currentlyConnected':
      break
    default:
      return false
  }

  let payload = this.generatePayload(event, data)

  let c = this.channels[channel]
  let clients = Object.keys(c.clients)

  clients.forEach((client) => {
    c.clients[client].send(payload)
  })
}

SocketManager.prototype.broadcastAll = function(event, data) {
  switch (event) {
    case 'gameEvent':
    case 'gameOver':
    case 'newGame':
    case 'errorMessage':
      break
    default:
      return false
  }

  let payload = this.generatePayload(event, data)

  Object.keys(this.channels).forEach((channel) => {
    let c = this.channels[channel]
    Object.keys(channel).forEach((client) => {
      c.clients[client].send(payload)
    })
  })
}

SocketManager.prototype.emit = function(channel, id, event, data) {
  switch (event) {
    case 'gameEvent':
    case 'dataUpdate':
    case 'errorMessage':
    case 'updateState':
    case 'currentlyConnected':
      break
    default:
      return false
  }

  let payload = this.generatePayload(event, data)

  let c = this.channels[channel]
  let clients = Object.keys(c.clients)

  for (let client of clients) {
    if (id === client) {
      this.clients[id].send(payload)
      return true
    }
  }

  return false
}

SocketManager.prototype.generatePayload = function(message, data) {
  let payload = {
    type: message,
    data: data
  }

  return JSON.stringify(payload)
}

module.exports = function() {
  return new SocketManager()
}
