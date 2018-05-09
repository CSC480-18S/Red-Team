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

function GameManager(socketManager) {
  this.gameboard = Gameboard()
  this.greenScore = 0
  this.goldScore = 0
  this.swaps = 0
  this.currentPlay = null
  this.names = ['AI_Bill', 'AI_Tara', 'AI_Richard', 'AI_Xia']
  this.teams = ['Green', 'Gold', 'Green', 'Gold']
  this.playerManager = PlayerManager(socketManager, this)
  this.socketManager = socketManager
  this.timer = null
}

GameManager.prototype.getGameBoard = function() {
  return this.gameboard.board
}

GameManager.prototype.latestData = function() {
  return {
    board: this.gameboard.sendableBoard(),
    gold: this.goldScore,
    green: this.greenScore
  }
}

GameManager.prototype.updateStateData = function() {
  let data = this.latestData()
  data.players = this.playerManager.getAllPlayers()
  data.bonus = this.currentPlay === null ? false : this.currentPlay.bonus
  return data
}

GameManager.prototype.addPlayer = function(id, socket, isAI, data) {
  let player = this.playerManager.createPlayer(id, socket, isAI)
  if (isAI) {
    player.addInformation(this.generateAIData())
  } else {
    player.addInformation(data)
  }

  let latestData = player.data()
  latestData.latestData = this.latestData()
  this.socketManager.emit(id, 'dataUpdate', latestData)
  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`${player.name} has joined the game`))
  this.socketManager.broadcast('SFs', 'updateState', this.updateStateData())
}

GameManager.prototype.aiNames = function(aiName) {
  if (aiName === undefined) {
    return this.names.shift()
  } else {
    this.names.push(aiName)
  }
}

GameManager.prototype.aiTeams = function(aiTeam) {
  if (aiTeam === undefined) {
    return this.teams.shift()
  } else {
    this.teams.push(aiTeam)
  }
}

GameManager.prototype.generateAIData = function(player) {
  return {
    name: this.aiNames(),
    team: {
      name: this.aiTeams()
    }
  }
}

GameManager.prototype.determineEvent = function(event, id) {
  switch (event.event) {
    case 'playWord':
      this.attemptPlay(event.data.play, id)
      break
    case 'swap':
      this.swap(id)
      break
    default:
      console.log('something else')
      console.log(event.data)
  }
}

GameManager.prototype.attemptPlay = function(newBoard, id) {
  let player = this.playerManager.getPlayer(id)
  const letters = ex.extractLetters(newBoard, this.getGameBoard(), player)

  if (letters.valid) {
    const words = ex.extractWords(letters.data, newBoard)
    this.currentPlay = words

    this.wordValidation(words, player)
      .then(r => {
        let play = null
        if (r.valid) {
          // if invalid type of play, gets the word that was invalid, else is undefined
          play = this.gameboard.placeWords(this.currentPlay, player)
          // if the board has attempted to play a word
          if (play.valid) {
            let ls = letters.data.map(l => l.letter)
            this.playerManager.updateTiles(id, ls)
          }
          let response = this.determineResponse(play)
          if (response.valid) {
            this.validPlay(id)
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
  const search = words.words.map(s => s.word).join(',')

  dg('checking words against database', 'debug')
  return db.dictionaryCheck(search).then(r => {
    return this.pruneResults(r, player)
  })
}

GameManager.prototype.pruneResults = function(response, player) {
  for (let word of response) {
    if (word.bad) {
      db.updatePlayerDirty(player, word.word)
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
      db.updatePlayerSpecial(player, word.word)
      this.currentPlay.bonus = true
    } else {
      this.currentPlay.bonus = false
    }

    return {
      valid: true
    }
  }
}

GameManager.prototype.turnTimer = function(id) {
  clearInterval(this.timer)
  let time = 60
  this.timer = setInterval(() => {
    if (time === 10) {
      dg('Turn time is expiring', 'verbose')
      this.socketManager.emit(id, 'gameEvent', this.generateGameEvent(`You have ${Math.ceil(time)} seconds left!`))
    }

    if (time > 0) {
      time--
      this.socketManager.emit(id, 'playTimer', this.generateTimeLeft(time), () => {
        clearInterval(this.timer)
      })
      dg(`play time left: ${time} -> ${id}`, 'debug')
    } else {
      clearInterval(this.timer)
      dg('Turn time expired', 'verbose')
      this.currentPlay = null
      let player = this.playerManager.getPlayer(id)
      this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`${player.name}'s time has expired`))

      this.updateTurn(id, this.latestData())

      this.swaps++
      if (this.isGameOver()) {
        this.gameOver()
      }
    }
  }, 1000)
}

GameManager.prototype.swap = function(id) {
  let player = this.playerManager.getPlayer(id)
  this.currentPlay = null
  this.swaps++

  if (this.isGameOver()) {
    clearInterval(this.timer)
    this.gameOver()
    return
  }

  this.playerManager.updateTiles(id)
  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`${player.name} has swapped tiles`))
  this.updateTurn(id, this.latestData())
  this.socketManager.broadcast('SFs', 'updateState', this.updateStateData())
}

GameManager.prototype.updateTurn = function(id, latestData) {
  let newId = this.playerManager.updateTurn(id, this.latestData())
  this.turnTimer(newId)
}

GameManager.prototype.isGameOver = function() {
  return this.swaps === 4
}

GameManager.prototype.gameOver = function() {
  dg('all players have swapped tiles, game over', 'info')

  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent('Game over!'))

  let finalScores = []
  let winner = null
  let highestScore = 0
  let players = this.playerManager.getAllPlayers()
  for (let player of players) {
    if (player.score > highestScore) {
      highestScore = player.score
      winner = player.name
    }
    let data = {
      name: player.name,
      score: player.score
    }
    finalScores.push(data)
  }
  let goldWin = this._goldScore > this._greenScore
  db.updateWin('Gold', this.goldScore, goldWin)
  db.updateWin('Green', this.greenScore, !goldWin)

  let data = {
    scores: finalScores,
    winner: winner === null ? 'No one!' : winner,
    winningTeam: goldWin ? 'Gold' : 'Green'
  }

  this.reset()
  this.playerManager.updatePlayers(this.latestData())
  this.socketManager.broadcastAll('gameOver', data)

  let timeUntil = 30
  let timer = setInterval(() => {
    if (timeUntil > 0) {
      dg(`${timeUntil}`, 'debug')
      this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(`New game starts in ${timeUntil}`))
      timeUntil--
    } else {
      clearInterval(timer)
      dg('new game started!', 'info')
      this.newGame()
    }
  }, 1000)
}

GameManager.prototype.reset = function() {
  this.currentPlay = null
  this.swaps = 0
  this.goldScore = 0
  this.greenScore = 0
  this.gameboard = Gameboard()

  this.playerManager.reset(this.latestData())
  this.socketManager.broadcast('SFs', 'updateState', this.updateStateData())
}

GameManager.prototype.newGame = function() {
  if (this.playerManager.getAllPlayers().length === 0) {
    this.playerManager.firstTurnSet = false
  } else {
    this.playerManager.getAllPlayers()[0].isTurn = true
  }
  this.playerManager.updatePlayers(this.latestData())
  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent('New game started!'))
  this.socketManager.broadcast('SFs', 'newGame')
  this.socketManager.broadcast('SFs', 'updateState', this.updateStateData())
}

GameManager.prototype.generateGameEvent = function(action) {
  return {
    action: action
  }
}

GameManager.prototype.generateTimeLeft = function(time) {
  return {
    time: time
  }
}

GameManager.prototype.determineResponse = function(play) {
  let reason = null
  let valid = false

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
      valid = true
  }

  return {
    valid,
    reason
  }
}

GameManager.prototype.validPlay = function(id) {
  dg(`${id} -> valid play`, 'info')
  this.swaps = 0

  let player = this.playerManager.getPlayer(id)

  let score = this.calculateScore()
  this.addScore(id, score)

  let words = this.currentPlay.words.map(w => w.word)
  let action = `${player.name} played ${words} for ${score.totalScore} points`

  this.updateTurn(id, this.latestData())

  this.socketManager.broadcastAll('gameEvent', this.generateGameEvent(action))
  this.socketManager.broadcast('SFs', 'updateState', this.updateStateData())

  this.currentPlay = null
}

GameManager.prototype.invalidPlay = function(id, reason) {
  dg(`${id} -> invalid play`, 'info')
  this.currentPlay = null

  this.socketManager.emit(id, 'invalidPlay', () => {})
  this.socketManager.emit(id, 'gameEvent', this.generateGameEvent(reason), () => {})
  return true
}

GameManager.prototype.calculateScore = function() {
  return sc(this.currentPlay.words, this.getGameBoard())
}

GameManager.prototype.addScore = function(id, score) {
  let player = this.playerManager.getPlayer(id)
  this.playerManager.updateScore(id, score.totalScore)

  if (!player.isAI) {
    db.updatePlayer(player, score.words)
  }

  switch (player.team) {
    case 'Green':
      this.greenScore += score.totalScore
      break
    case 'Gold':
      this.goldScore += score.totalScore
      break
  }
  return true
}

module.exports = function(socketManager) {
  return new GameManager(socketManager)
}
