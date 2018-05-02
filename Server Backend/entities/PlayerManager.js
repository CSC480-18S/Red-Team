'use strict'

/**
 * Imports files
 */
const ld = require('../helpers/LetterDistributor')
const dg = require('../helpers/Debug')(true)
const db = require('../helpers/DB')
const mg = require('../helpers/MacGrabber')
const Player = require('./Player')

function PlayerManager(gameManager) {
  this.players = {}
  this.oldData = {}
  this.gameManager = gameManager
}

PlayerManager.prototype.getAmountOfPlayers = function() {
  return Object.keys(this.players).length
}

PlayerManager.prototype.updatePostitions = function() {
  Object.keys(this.players).forEach((player, index) => {
    player.position = index
  })
}

PlayerManager.prototype.createPlayer = function(id, socket, isAI) {
  // TODO: Distinguish AI from clients @Landon
  let player = Player(id, socket, isAI, this.getAmountOfPlayers())

  this.players[id] = player

  dg(`Player created -> ${id}`, 'debug')

  // TODO: DB stuff @Landon
  // return this.getPlayerInfo(id).then(success => {
  if (true) {
    if (!this.injectOldData(player.position, id)) {
      this.injectTiles(id)
      return true
    }
  } else {
    // TODO: Tell player they need to registerI @Landon
    return false
  }
  // })
}

PlayerManager.prototype.removePlayer = function(id) {
  // TODO: Need to be able to distinguish clients from AI @Landon
  delete this.players[id]
  this.updatePostitions()

  dg(`Player removed -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.addOldData = function(position, id) {
  let player = this.players[id]

  this.oldData[position] = {
    tiles: player.tiles,
    isTurn: player.isTurn
  }

  dg(`Data saved -> ${id}`, 'debug')

  return true
}

PlayerManager.prototype.injectOldData = function(position, id) {
  if (position in this.oldData) {
    let player = this.players[id]
    player.injectData(this.oldData[position])
    delete this.oldData[position]

    dg(`Data injected -> ${id}`, 'debug')

    return true
  }

  dg(`Data not injected -> ${id}`, 'debug')

  return false
}

PlayerManager.prototype.updateTiles = function(id, tilesToBeRemoved) {
  this.removeTiles(id, tilesToBeRemoved)
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

  player.setTiles(tiles)

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

PlayerManager.prototype.getPlayerInfo = function(id) {
  return mg(this.socket._socket.remoteAddress, (mac) => {
    return db.checkIfUserExists(mac)
      .then(r => {
        if (db.pruneResults(r)) {
          db.getTeamURL(mac)
            .then(r2 => {
              let name = r[0].username
              let team = {
                link: r2,
                name: r2 === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green'
              }
              let link = r[0]._links.self.href

              let player = this.players[id]

              player.addInformation(name, team, link)

              dg(`DB data grabbed -> ${id}`, 'debug')

              return true
            })
        } else {
          dg(`DB data not retrieved -> ${id}`, 'debug')

          return false
        }
      })
      .catch(e => {
        console.log(e)
      })
  })
}

module.exports = function() {
  return new PlayerManager()
}
