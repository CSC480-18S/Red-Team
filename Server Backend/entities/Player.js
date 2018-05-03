'use strict'

function Player(id, isAI, position) {
  this.id = id
  this.isAI = isAI
  this.position = position
  this.score = 0
  this.isTurn = false
  this.tiles = []
  this.link = null
  this.name = null
  this.team = null
  // this.listenForIncoming()
}

Player.prototype.addInformation = function(name, team, link) {
  this.name = name
  this.team = {
    name: team.name,
    link: team.link
  }
  this.link = link
  return true
}

Player.prototype.resetScore = function() {
  this.score = 0
}

Player.prototype.updateScore = function(score) {
  this.score += score
}

Player.prototype.injectData = function(data) {
  this.tiles = data.tiles
  this.isTurn = data.isTurn
}

Player.prototype.setTiles = function(tiles) {
  this.tiles = tiles
}

Player.prototype.data = function() {
  return {
    name: this.name,
    team: 'Green',
    // team: this.team.name,
    position: this.position,
    isTurn: this.isTurn,
    isAI: this.isAI,
    score: this.score,
    tiles: this.tiles

  }
}

module.exports = function(id, socket, isAI, position) {
  return new Player(id, socket, isAI, position)
}
