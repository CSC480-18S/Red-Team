'use strict'
/**
 * Exports this entity
 */

module.exports = (() => {
  const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
  const tiles = letters.map(t => {
    return {
      letter: t,
      score: extractTileValue(t)
    }
  })
  function extractTileValue(letter) {
    switch (letter) {
      case 'A':
      case 'E':
      case 'I':
      case 'O':
      case 'U':
      case 'L':
      case 'N':
      case 'S':
      case 'T':
      case 'R':
        return 1
      case 'D':
      case 'G':
        return 2
      case 'B':
      case 'C':
      case 'M':
      case 'P':
        return 3
      case 'F':
      case 'H':
      case 'V':
      case 'W':
      case 'Y':
        return 4
      case 'K':
        return 5
      case 'J':
      case 'X':
        return 8
      case 'Q':
      case 'Z':
        return 10
    }
  }
  return tiles
})()
