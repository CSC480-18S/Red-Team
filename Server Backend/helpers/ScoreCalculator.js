'use strict'

/**
 * Imports files
 */
const tiles = require('../entities/Bag')
const dg = require('./Debug')

/**
 * Calculates the scor of a play
* @param {Array} words - array of words to calculate score for
* @param {Array} gameBoard - board
*/
module.exports = (words, gameBoard) => {
  dg('calculating score', 'debug')
  let cumulativeScore = 0
  let play = {
    words: []
  }
  words.map(w => {
    let x = 0
    let y = 0
    const wordArray = w.word.toUpperCase().split('')
    let wordBonus = 1
    let score = wordArray.map(l => {
      let letterBonus = 1
      for (let t of tiles) {
        if (t.letter === l) {
          let boardTile = gameBoard[w.h ? w.y : w.y + y++][w.h ? w.x + x++ : w.x]
          switch (boardTile.multiplierType) {
            case 'word':
              wordBonus = wordBonus * boardTile.multiplier
              break
            case 'letter':
              letterBonus = boardTile.multiplier
              break
            default:
              break
          }
          boardTile.multiplierType = null
          boardTile.multiplier = null
          return t.score * letterBonus
        }
      }
    }).reduce((prev, curr) => {
      return prev + curr
    })
    let wordScore = {
      word: w.word,
      score: score * wordBonus
    }
    play.words.push(wordScore)

    cumulativeScore += wordScore.score
  })
  play.totalScore = cumulativeScore
  return play
}
