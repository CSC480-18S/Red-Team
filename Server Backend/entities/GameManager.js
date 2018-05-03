'use strict'
/**
 * Imports files
 */
const Gameboard = require('./Gameboard')
const sc = require('../helpers/ScoreCalculator')
const ex = require('../helpers/Extractor')
const dg = require('../helpers/Debug')(true)
const db = require('../helpers/DB')
const PlayerManager = require('./PlayerManager')
const PlayCounter = require('timedown')

let timer = PlayCounter()
let t = timer.ns('playTimer', '10s')

function GameManager(socketManager) {
  this.gameboard = new Gameboard()
  this.greenScore = 0
  this.goldScore = 0
  this.swaps = 0
  this.currentPlay = null
  this.playerManager = PlayerManager(this.determineEvent.bind(this), this.updatePlayers.bind(this))
  this.socketManager = socketManager
  // this.turnTimer()
}

GameManager.prototype.getGameBoard = function() {
  return this.gameboard.board
}

GameManager.prototype.latestData = function() {
  return {
    board: this.board.sendableBoard(),
    gold: this.goldScore,
    green: this.greenScore
  }
}

GameManager.prototype.addPlayer = function(id, socket, isAI) {
  let player = this.playerManager.createPlayer(id, socket, isAI)
  this.socketManager.emit(id, 'dataUpdate', player.data())
}

GameManager.prototype.determineEvent = function(event) {
  console.log(event)
}

GameManager.prototype.attemptPlay = function(newBoard, id) {
  let player = this.playerManager.getPlayer(id)
  const letters = ex.extractLetters(newBoard, this._gameBoard.board, player)

  if (letters.valid) {
    const words = ex.extractWords(letters.data, newBoard)
    this.currentPlay = words

    this.wordValidation(words, player)
      .then(r => {
        let play = null
        if (r.valid === true) {
          // if invalid type of play, gets the word that was invalid, else is undefined
          play = this._gameBoard.placeWords(this.currentPlay, player)
          // if the board has attempted to play a word
          if (play.valid) {
            let ls = letters.data.map(l => l.letter)
            player.updateHand(ls)
          }
          let response = this.determineResponse(play)
          if (response.valid) {
            this.validPlay(id, this.currentPlay)
          } else {
            this.invalidPlay(id, response.reason)
          }
        } else {
          // if the word is invalid
          let response = this.determineResponse(r)
          return this.invalidPlay(id, response.reason)
        }
      })
      .catch(e => {
        dg(`${e}`, 'error')
      })
  } else {

  }
}

GameManager.prototype.wordValidation = function(words, player) {
  const search = words.data.map(s => s.word).join(',')

  dg('checking words against database', 'debug')
  return db.dictionaryCheck(search).then(r => {
    return this.pruneResults(r, player)
  })
}

GameManager.prototype.pruneResults = function(response, player) {
  for (let word of response) {
    if (word.bad) {
      db.updatePlayerDirty(player, word)
      return {
        valid: false,
        error: 6,
        word: word.word
      }
    }
    if (!word.valid) {
      return {
        valid: false,
        error: 1,
        word: word.word
      }
    }
    if (word.special) {
      db.updatePlayerSpecial(player, word)
    }

    return {
      valid: true
    }
  }
}

GameManager.prototype.turnTimer = function(id) {
  t.restart('60000ms', {refresh: '1000ms'})
  timer.start('playTimer')

  // Listen to timer counter events
  timer.on('tick', function(time) {
    dg(`play time left: ${time.ms} -> ${id}`, 'debug')
    // TODO: Send to client @Landon
  })

  timer.on('ending', function(time) {
    dg('Turn time is expiring', 'verbose')
    this.socketManager.emit(id, 'gameEvent', this.generateGameEvent(`You have ${Math.ceil(time.ms / 1000)} seconds left!`))
  })

  timer.on('stop', function(time) {
    dg('Play made', 'verbose')
    // TODO: When the player made a play in the time window
  })

  timer.on('end', function() {
    dg('Turn time expired', 'verbose')
    this.currentPlay = null
    let player = this.playerManager.getPlayer(id)
    this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`${player.name}'s time has expired`))

    this.playerManager.updateTurn(id)

    this.swaps++
    if (this.isGameOver()) {
      this.gameOver()
      return true // game is over, dont send out data update because it is sent out later
    }

    return false // send out data update
  })
}

GameManager.prototype.swap = function(id) {
  this.currentPlay = null
  this._swaps++
  this.playerManager.updateTiles(id)
  this.playerManager.updateTurn(id)

  if (this.isGameOver()) {
    this.gameOver()
  }

  // TODO: Alert players with dataUpdate @Landon
  // TODO: Alert players with gameEvent @Landon
  // TODO: Alert frontends with updateState and gameEvent @Landon
}

GameManager.prototype.isGameOver = function() {
  return this.swaps === 4
}

GameManager.prototype.gameOver = function() {
  dg('all players have swapped tiles, game over', 'info')

  // TODO: Send game event telling game is over @Landon
  // TODO: Send game over event
}

GameManager.prototype.reset = function() {
  this.currentPlay = null
  this._swaps = 0
  this.goldScore = 0
  this.greenScore = 0

  this.playerManager.reset()

  this.gameboard = new Gameboard()
}

GameManager.prototype.newGame = function() {
  this.reset()
  this.playerManager.getAllPlayers()[0].isTurn = true
  // this.emitDataUpdate(this.gameManager.board.sendableBoard())
  this.updateFrontends()
  this.emitGameEvent('New game started')
}

GameManager.prototype.generateGameEvent = function(action) {
  return {
    action: action
  }
}

GameManager.prototype.updatePlayers = function() {
  let players = this.playerManager.getAllPlayers()

  players.forEach(p => {
    let data = p.data
    data.board = this.gameboard.sendableBoard()
    this.serverManager.emit(p.id, 'dataUpdate', p.data)
  })
}

GameManager.prototype.determineResponse = function(play) {
  let reason = null
  let invalid = true

  switch (play.error) {
    case 1:
      reason = `${play.word} is not a valid word`
      break
    case 2:
      reason = `${play.word} Placed out of the bounds of the board`
      break
    case 3:
      reason = `${play.word} placed in invalid position`
      break
    case 4:
      reason = `${play.word} was not played over the center tile`
      break
    case 5:
      reason = `${play.word} not connected to played tiles`
      break
    case 6:
      reason = `${play.word} is a bad word`
      break
    case 7:
      reason = 'You tried to cheat :)'
      break
    default:
      invalid = false

      return {
        invalid,
        reason
      }
  }
}

GameManager.prototype.invalidPlay = function(id, reason) {
  dg(`${id} -> invalid play`, 'info')

  this.socketManager.emit(id, 'invalidPlay')
  this.socketManager.emit(id, 'gameEvent', this.generateGameEvent(reason))
  return true
}

GameManager.prototype.validPlay = function(id, play) {
  dg(`${id} -> valid play`, 'info')

  let player = this.playerManager.getPlayer(id)

  let score = this.calculateScore(play)

  let words = play.map(w => w.word)
  let action = `${player.name} played ${words} for ${score.totalScore} points`

  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(action))

  this.playerManager.updateTurn(id)
  this.updatePlayers()

  // TODO: Somehow figure out how to check if words in play are bonus words...we are getting the list of words from the score so we can do something with that @Landon
}

GameManager.prototype.calculateScore = function(words) {
  return sc(words, this.getGameBoard)
}

GameManager.prototype.addScore = function(id, score) {
  let player = this.playerManager.getPlayer(id)
  player.updateScore(score)

  if (!player.isAI) {
    db.updatePlayer(player, score.words)
  }

  switch (player.team) {
    case 'Green':
      this.greenScore += score
      break
    case 'Gold':
      this.goldScore += score
      break
  }

  return true
}

module.exports = function(socketManager) {
  return new GameManager(socketManager)
}
