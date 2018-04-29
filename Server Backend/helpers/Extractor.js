'use strict'
/**
 * Imports files
 */
const _ = require('lodash')
const dg = require('../helpers/Debug')(true)

function extractLetters(newBoard, currentBoard, player) {
  const letters = []
  for (let i = 0; i < newBoard.length; i++) {
    for (let j = 0; j < newBoard[0].length; j++) {
      const currentBoardLetter = currentBoard[j][i].letter
      const newBoardLetter = newBoard[j][i] === null ? null : newBoard[j][i].toUpperCase()

      if (newBoardLetter !== null) {
        if (currentBoardLetter !== newBoardLetter) {
          const tile = {
            letter: newBoardLetter,
            x: i,
            y: j
          }
          letters.push(tile)
        }
      }
    }
  }
  dg('checking against player\'s hand', 'debug')

  for (let i = 0; i < letters.length; i++) {
    let inHand = player.tiles.indexOf(letters[i].letter)

    if (inHand === -1) {
      dg(`player ${player.name} has cheated`, 'info')
      return {
        valid: false,
        error: 7,
        data: letters[i].letter
      }
    }
  }

  return {
    valid: true,
    data: letters
  }
}

function extractWords(letters, newBoard) {
  const words = []

  letters.map(letterObject => {
    let inSpace = 0
    for (let i = 0; i < 2; i++) {
      let word = ''
      let startOfWord = false
      let endOfWord = false
      let startPosition = null
      let position = {
        x: letterObject.x,
        y: letterObject.y
      }

      let check = 0
      while (!startOfWord) {
        if (position.x < 0 || position.y < 0 || newBoard[position.y][position.x] === null) {
          startOfWord = true
          position.x = i === 0 ? position.x : position.x + 1
          position.y = i === 0 ? position.y + 1 : position.y
        } else {
          check++
          position.x = i === 0 ? position.x : position.x - 1
          position.y = i === 0 ? position.y - 1 : position.y
        }
      }
      if (check === 1) {
        inSpace++
      }

      startPosition = _.cloneDeep(position)

      while (!endOfWord) {
        if (position.x >= newBoard.length || position.y >= newBoard.length || newBoard[position.y][position.x] === undefined || newBoard[position.y][position.x] === null) {
          endOfWord = true
        } else {
          check++
          word += newBoard[position.y][position.x]
          position.x = i === 0 ? position.x : position.x + 1
          position.y = i === 0 ? position.y + 1 : position.y
        }
      }

      if (check === 2) {
        inSpace++
      }

      if (word.length > 1 || inSpace === 4) {
        let wordObject = {
          word: word,
          x: startPosition.x,
          y: startPosition.y,
          h: i === 1
        }
        words.push(wordObject)
      }
      word = ''
      check = 0
    }
  })

  let play = _.uniqWith(words, _.isEqual)
  return {
    valid: true,
    data: play
  }
}

module.exports = {
  extractLetters,
  extractWords
}
