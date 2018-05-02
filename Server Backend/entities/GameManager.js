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

function GameManager() {
  this.gameboard = null
  this.greenScore = 0
  this.goldScore = 0
  this.swaps = 0
  this.playerManager = PlayerManager()
}

GameManager.prototype.getGameBoard = function() {
  return this.gameboard.board
}

GameManager.prototype.addPlayer = function(id, socket, isAI) {
  this.playerManager.createPlayer(id, socket, isAI)
}

GameManager.prototype.attemptPlay = function(newBoard, player) {

}

module.exports = function() {
  return new GameManager()
}

// class GameManager {
//   play(newBoard, player) {
//     const letters = ex.extractLetters(newBoard, this._gameBoard.board, player)

//     if (letters.valid) {
//       const words = ex.extractWords(letters.data, newBoard)

//       this.wordValidation(words, player)
//         .then(r => {
//           let boardResponse = null
//           if (r.valid === true) {
//           // if invalid type of play, gets the word that was invalid, else is undefined
//             boardResponse = this._gameBoard.placeWords(words.data, player)
//             // if the board has attempted to play a word
//             if (boardResponse.valid) {
//               let ls = letters.data.map(l => l.letter)
//               player.updateHand(ls)
//             }
//             return this.respond(boardResponse.error, boardResponse.data, player)
//           } else {
//           // if the word is invalid
//             return this.respond(r.error, r.data, player)
//           }
//         })
//         .catch(e => {
//           dg(`${e}`, 'error')
//         })
//     } else {

//     }
//   }

//   /**
//    * Grabs the latest data of the game
//    */
//   latestData() {
//     return {
//       board: this.board.sendableBoard(),
//       gold: this._goldScore,
//       green: this._greenScore
//     }
//   }

//   swapMade(player) {
//     this._swaps++
//     if (this.checkGameOver()) {
//       this.gameOver()
//       return
//     }
//     player.updateHand(player.tiles)
//     this.gameEvent(`${player.name} swapped tiles`)
//     this.updateTurn(player, true)
//   }

//   /**
//    * Updates who's turn it is
//    */
//   updateTurn(player, swapped) {
//     this.changeTurn(player.position)
//     if (!swapped) {
//       this._swaps = 0
//     }
//   }

//   afterTurn() {
//     this.frontendsUpdate()
//   }

//   /**
//      * Timer for a player's turn
//      */
//   playTimer(reset, player) {
//     let time = 60
//     if (reset) {
//       if (this.timer !== null) {
//         clearInterval(this.timer)
//       }
//     } else {
//       this.timer = setInterval(() => {
//         if (time > 0) {
//           if (player !== null) {
//             time--
//             player.playTimer(time)
//           } else {
//             clearInterval(this.timer)
//           }
//         } else {
//           clearInterval(this.timer)
//           dg(`${player.name}'s time has expired`, 'info')
//           this.gameEvent(`${player.name}'s time has expired`)
//           this._swaps++
//           if (this.checkGameOver()) {
//             this.gameOver()
//             return
//           }
//           this.updateTurn(player, true)
//         }
//       }, 1000)
//     }
//   }

//   /**
//      * Checks to see if the game is over
//      */
//   checkGameOver() {
//     return this._swaps === 4
//   }

//   gameOver() {
//     dg('all players have swapped tiles, game over', 'info')
//     this.gameEvent('Game over!')
//     this.gameOverEvent()

//     this._swaps = 0
//   }

//   /**
//    * Sends out a boardUpdate event to all clients
//    */
//   boardUpdate() {
//     let boardUpdateData = {
//       event: 'boardUpdate',
//       data: {
//         board: this._gameBoard.sendableBoard(),
//         yellow: this._goldScore,
//         green: this._greenScore
//       }
//     }

//     this.ws.send(JSON.stringify(boardUpdateData))
//   }

//   /**
//    * Checks to see if word(s) are in the DB
//    * @param {Array} words - words to be checked against the DB
//    * @param {Object} player - player
//    */
//   wordValidation(words, player) {
//     const search = words.data.map(s => s.word).join(',')

//     dg('checking words against database', 'debug')
//     return db.dictionaryCheck(search).then(r => {
//       return this.pruneResults(r, player)
//     })
//   }

//   /**
//    * Prunes the data sent back from the DB to check if anywords are either invalid or bad words
//    * @param {Array} response - word data sent back from DB
//    * @param {Object} player - player
//    */
//   pruneResults(response, player) {
//     dg('pruning results of database response', 'debug')
//     for (let word of response) {
//       if (word.bad) {
//         return {
//           valid: false,
//           error: 6,
//           data: word.word
//         }
//       }
//       if (!word.valid) {
//         return {
//           valid: false,
//           error: 1,
//           data: word.word
//         }
//       }
//       if (word.special) {
//         db.updatePlayerSpecial(player, word)
//       }
//     }

//     return {
//       valid: true
//     }
//   }

//   /**
//    * Calculates the score of a play
//    * @param {Object} player - player to add score to
//    * @param {Array} words - array of words to calculate score for
//    */
//   calculateScore(player, words) {
//     let score = sc(words, this._gameBoard.board)

//     if (!player.isAI) {
//       db.updatePlayer(player, score.words)
//     }

//     this.addScore(player, score.totalScore)
//     this.updateTurn(player, false)
//     return score
//   }

//   /**
//    * Adds the score to the player's score and the team they are on
//    * @param {Object} player - player to add score to
//    * @param {Number} score - score
//    */
//   addScore(player, score) {
//     // Need to update DB as well
//     player.addScore(score)

//     switch (player.team) {
//       case 'Green':
//         this._greenScore += score
//         break
//       case 'Gold':
//         this._goldScore += score
//         break
//     }
//   }

//   /**
//    * Creates a new gameboard and initializes it
//    */
//   resetGameboard() {
//     this._greenScore = 0
//     this._goldScore = 0
//     this._gameBoard = new Gameboard()
//   }

//   /**
//    * Resets all players' scores
//    */
//   resetPlayers() {
//     this._greenScore = 0
//     this._goldScore = 0

//     this._playerManagers.map(p => {
//       p.resetScore()
//       p.updateHand(p.tiles)
//     })
//   }

//   respond(e, data, player) {
//     let reason = null
//     let invalid = true

//     switch (e) {
//       case 1:
//         reason = 'Not a valid word'
//         break
//       case 2:
//         reason = 'Placed out of the bounds of the board'
//         break
//       case 3:
//         reason = 'Invalid placement'
//         break
//       case 4:
//         reason = 'Word was not played over the center tile'
//         break
//       case 5:
//         reason = 'Word not connected to played tiles'
//         break
//       case 6:
//         reason = 'That is a bad word'
//         db.updatePlayerDirty(player, data)
//         break
//       case 7:
//         reason = 'You cheated'
//         break
//       default:
//         invalid = false
//     }

//     if (invalid) {
//       player.invalidPlay(reason)
//       return
//     }
//     let score = this.calculateScore(player, data)

//     dg('sending out game event event', 'debug')
//     let words = data.map(w => w.word)
//     let action = `${player.name} played ${words} for ${score.totalScore} points`
//     dg(action, 'info')
//     // TODO: Need to flag whether or not this is a bonus play or not @Landon
//     // const search = words.map(s => s).join(',')
//     // db.dictionaryCheck(search).then(r => {
//     //   let bonus = false
//     //   for (let word of r) {
//     //     if (word.special) {
//     //       bonus = true
//     //     }
//     //   }
//     this.gameEvent(action)
//     // }).catch(e => {
//     //   console.log(e)
//     // })
//   }
// }

// module.exports = GameManager
