'use strict'

/**
 * Imports files
 */
const ld = require('../helpers/LetterDistributor')
const dg = require('../helpers/Debug')(true)
const Player = require('./Player')

function PlayerManager(socketManager, determineEvent) {
  this.players = {}
  this.oldData = []
  this.firstTurnSet = false
  this.socketManager = socketManager
  this.determineEvent = determineEvent
}

PlayerManager.prototype.getAmountOfPlayers = function() {
  return Object.keys(this.players).length
}

PlayerManager.prototype.updatePostitions = function() {
  Object.keys(this.players).forEach((player, index) => {
    this.players[player].position = index
  })

  dg('positions updated', 'debug')
}

PlayerManager.prototype.createPlayer = function(id, socket, isAI) {
  // TODO: Distinguish AI from clients @Landon
  let player = Player(id, isAI, this.getAmountOfPlayers())
  if (!this.firstTurnSet) {
    player.isTurn = true
    this.firstTurnSet = true
  }

  this.players[id] = player
  this.listenForGameActions(socket)

  dg(`Player created -> ${id}`, 'debug')

  // TODO: DB stuff @Landon
  // return this.getPlayerInfo(id).then(success => {
  if (true) {
    if (!this.injectOldData(id)) {
      this.injectTiles(id)
    }
    return player
  } else {
    // TODO: Tell player they need to registerI @Landon
    return false
  }
  // })
}

PlayerManager.prototype.listenForGameActions = function(socket) {
  socket.on('close', () => {
    let player = this.players[socket.id]
    this.removePlayer(player.id)
    this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`${player.name} has left the game`))
  })

  socket.on('message', data => {
    let event = JSON.parse(data)
    this.determineEvent(event, socket.id)
  })
}

PlayerManager.prototype.generateGameEvent = function(action) {
  return {
    action: action
  }
}

PlayerManager.prototype.getPlayer = function(id) {
  return this.players[id]
}

PlayerManager.prototype.getAllPlayers = function() {
  let players = Object.keys(this.players).map(id => {
    return this.players[id]
  })

  return players
}

PlayerManager.prototype.removePlayer = function(id) {
  // TODO: Need to be able to distinguish clients from AI @Landon
  // TODO: Tell other players through game event
  this.grabOldData(id)
  delete this.players[id]
  this.updatePostitions()

  dg(`Player removed -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.updateScore = function(id, score) {
  let player = this.players[id]

  player.updateScore(score)
}

PlayerManager.prototype.grabOldData = function(id) {
  let player = this.players[id]

  this.oldData.push({
    tiles: player.tiles,
    isTurn: player.isTurn
  })

  dg(`Data saved -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.injectOldData = function(id) {
  if (this.oldData.length > 0) {
    let player = this.players[id]
    player.injectData(this.oldData[0])
    this.oldData.splice(0, 1)

    dg(`Data injected -> ${id}`, 'debug')

    return true
  }

  dg(`Data not injected -> ${id}`, 'debug')

  return false
}

PlayerManager.prototype.updateTiles = function(id, tilesToBeRemoved) {
  if (tilesToBeRemoved === undefined) {
    this.removeTiles(id, this.players[id].tiles)
  } else {
    this.removeTiles(id, tilesToBeRemoved)
  }
  this.injectTiles(id)

  dg(`Tiles updated -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.injectTiles = function(id) {
  let player = this.players[id]

  let tiles = []
  let lettersToGenerate = 7 - player.tiles.length

  // generate the new letters
  for (let a = 0; a < lettersToGenerate; a++) {
    let index = Math.floor(Math.random() * ld.totalLetters)

    for (let i = 0; i < ld.intervals.length; i++) {
      if (index <= ld.intervals[i]) {
        tiles.push(ld.letters[i])
        break
      }
    }
  }

  player.addTiles(tiles)

  dg(`Tiles injected -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.removeTiles = function(id, tilesToBeRemoved) {
  let player = this.players[id]

  let currentHand = player.tiles

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

  player.setTiles(currentHand)

  dg(`Tiles removed -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.updateTurn = function(id, latestData) {
  let player = this.players[id]
  let position = player.position

  player.isTurn = false
  do {
    position++
    if (position > 3) {
      position = 0
    }
  } while (position >= this.getAmountOfPlayers())

  let players = Object.keys(this.players)

  let p
  for (let player of players) {
    p = this.players[player]
    if (p.position === position) {
      p.isTurn = true
    }
  }

  this.updatePlayers(latestData)

  return p.id
}

PlayerManager.prototype.updatePlayers = function(latestData) {
  let players = this.getAllPlayers()

  players.forEach(player => {
    let data = player.data()
    data.latestData = latestData
    this.socketManager.emit(player.id, 'dataUpdate', data)
  })
}

PlayerManager.prototype.reset = function(latestData) {
  Object.keys(this.players).forEach((id) => {
    let player = this.players[id]
    player.resetScore()
    player.isTurn = false
    this.updateTiles(player.id)
    let data = player.data()
    data.latestData = latestData
    this.socketManager.emit(id, 'dataUpdate', data)
  })
}

module.exports = function(socketManager, determineEvent) {
  return new PlayerManager(socketManager, determineEvent)
}
