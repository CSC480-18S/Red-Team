'use strict'
/**
 * Imports lodash
 */
const _ = require('lodash')

/**
 * Imports the Tile and GameboardResponse class
 */
const Tile = require('./Tile')

function Gameboard() {
  this.size = 11
  this.board = new Array(this.size)
  this.initialized = false
  this.firstPlay = true
  this.init()
}

/**
 * Creates a double array of Tiles
 * If this method has already been run, it returns before it can recreate the double array
 */
Gameboard.prototype.init = function() {
  if (this.initialized) {
    return true
  }

  for (let i = 0; i < this.board.length; i++) {
    this.board[i] = new Array(this.size)

    for (let j = 0; j < this.board[0].length; j++) {
      let tile = '' + i + j
      switch (tile) {
        case '00': case '07': case '24': case '37': case '310': case '42': case '68': case '70': case '73': case '86': case '103': case '1010':
          this.board[i][j] = new Tile(j, i, 'word', 2)
          break
        case '03': case '010': case '26': case '30': case '33': case '48': case '62': case '77': case '710': case '84': case '100': case '107':
          this.board[i][j] = new Tile(j, i, 'word', 3)
          break
        default:
          this.board[i][j] = new Tile(j, i, null, null)
          break
      }
    }
  }

  this.initialized = true
}

/**
   * Creates a board of just characters to be sent to the clients
   */
Gameboard.prototype.sendableBoard = function() {
  let board = new Array(this.size)

  for (let i = 0; i < this.board.length; i++) {
    board[i] = new Array(this.size)

    for (let j = 0; j < this.board[0].length; j++) {
      board[i][j] = this.board[i][j].letter
    }
  }

  return board
}

/**
 * Method that places words on the baord
 * @param {Array} words - array of words to place
 */
Gameboard.prototype.placeWords = function(words) {
  const tempBoard = _.cloneDeep(this.board)

  /**
   * For word placement validation
   */
  let validWordPlacement = false
  let invalidWord = null
  let firstPlayBypass = false
  for (let w of words.words) {
    let word = this.createWordObject(w)
    let wordActual = word.word

    for (let i = word.sX; i <= word.eX; i++) {
      for (let j = word.sY; j <= word.eY; j++) {
        /**
       * Check to see if the word was somehow placed out of bounds
       */
        if (tempBoard[j][i] === undefined) {
          return this.validatorDispatcher(false, 2, wordActual)
        }
        let l = tempBoard[j][i].letter
        let wordLetter = w.h ? word.word[i - word.sX].toUpperCase() : word.word[j - word.sY].toUpperCase()

        /**
       * Validate if the letter placed can be placed there
       */
        if (!this.validatePosition(l, wordLetter)) {
          return this.validatorDispatcher(false, 3, wordActual)
        }

        /**
         * Check whether or not the letter to be placed is being placed over a letter already there
         */
        if (!validWordPlacement && tempBoard[j][i].letterPlaced) {
          validWordPlacement = true
        }

        this.tileSetter(tempBoard[j][i], wordLetter)
      }
    }

    /**
     * Check to see if center tile was played over on the first play
     */
    if (this.firstPlay) {
      if (!this.validateCenterTile(tempBoard)) {
        return this.validatorDispatcher(false, 4, wordActual)
      }
      firstPlayBypass = true
      continue
    }

    if (!validWordPlacement) {
      invalidWord = wordActual
    }
  }
  /**
   * Make sure that the entire play has valid placement
   */
  if (!firstPlayBypass && !validWordPlacement) {
    return this.validatorDispatcher(false, 5, invalidWord)
  }
  this.board = tempBoard
  return this.validatorDispatcher(true, 0, words.words)
}

/**
 * Dispatches error codes and words from the validatior
 * @param {Number} v - valid
 * @param {Number} e - error code
 * @param {String/Array} w - word(s)
 */
Gameboard.prototype.validatorDispatcher = function(v, e, w) {
  return {
    valid: v,
    error: e,
    word: w
  }
}

/**
   * Helper method that sets properties of a tile
   * @param {Object} tile - tile to work with
   * @param {String} letter - letter to be set in the tile
   */
Gameboard.prototype.tileSetter = function(tile, letter) {
  tile.letter = letter
}

/**
 * Creates an object that holds word data
 * @param {Object} w - word object
 */
Gameboard.prototype.createWordObject = function(w) {
  return {
    word: w.word,
    sX: w.x,
    sY: w.y,
    eX: w.h ? w.x + w.word.length - 1 : w.x,
    eY: w.h ? w.y : w.y + w.word.length - 1
  }
}

/**
 * Validates whether the first played word was played over the center tile
 * @param {Array} tempBoard - temp board
 */
Gameboard.prototype.validateCenterTile = function(tempBoard) {
  const c = Math.floor(this.size / 2)
  if (!tempBoard[c][c].letterPlaced) {
    return false
  } else {
    this.firstPlay = false
    return true
  }
}

/**
 * Validates whether a letter can be placed in the current position
 * @param {String} currentLetter - the letter that is currently on the board
 * @param {String} toBePlacedLetter - the letter that is to be placed on the board
 */
Gameboard.prototype.validatePosition = function(currentLetter, toBePlacedLetter) {
  if (currentLetter === null) {
    return true
  } else if (currentLetter === toBePlacedLetter) {
    return true
  }

  return false
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = function() {
  return new Gameboard()
}
