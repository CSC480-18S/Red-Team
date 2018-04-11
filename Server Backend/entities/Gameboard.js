'use strict'
/**
 * Imports lodash and axios
 */
const _ = require('lodash')

/**
 * Imports the Tile and GameboardResponse class
 */
const Tile = require('./Tile')

class Gameboard {
  constructor() {
    this._size = 11
    this._board = new Array(this._size)
    this._initialized = false
    this._firstPlay = true
    this.init()
  }

  /**
   * Board setter
   */
  set board(board) {
    this._board = board
  }

  /**
   * Board getter
   */
  get board() {
    return this._board
  }

  /**
   * Creates a double array of Tiles
   * If this method has already been run, it returns before it can recreate the double array
   */
  init() {
    if (this._initialized) {
      return true
    }

    for (let i = 0; i < this._board.length; i++) {
      this._board[i] = new Array(this._size)

      for (let j = 0; j < this._board[0].length; j++) {
        let tile = '' + i + j
        switch (tile) {
          case '00': case '07': case '24': case '37': case '310': case '42': case '68': case '70': case '73': case '86': case '103': case '1010':
            this._board[i][j] = new Tile(j, i, 'word', 2)
            break
          case '03': case '010': case '26': case '30': case '33': case '48': case '62': case '77': case '710': case '84': case '100': case '107':
            this._board[i][j] = new Tile(j, i, 'letter', 2)
            break
          default:
            this._board[i][j] = new Tile(j, i, null, null)
            break
        }
      }
    }

    this._initialized = true
  }

  /**
   * Creates a board of just characters to be sent to the clients
   */
  sendableBoard() {
    let board = new Array(this._size)

    for (let i = 0; i < this._board.length; i++) {
      board[i] = new Array(this._size)

      for (let j = 0; j < this._board[0].length; j++) {
        board[i][j] = this._board[i][j].letter
      }
    }

    return board
  }

  /**
   * Method that places words on the baord
   * @param {Array} words - array of words to place
   */
  placeWords(words, user) {
    const tempBoard = _.cloneDeep(this._board)

    /**
     * For word placement validation
     */
    let validWordPlacement = false
    for (let w of words) {
      let word = this.createWordObject(w)

      for (let i = word.sX; i <= word.eX; i++) {
        for (let j = word.sY; j <= word.eY; j++) {
          /**
         * Check to see if the word was somehow placed out of bounds
         */
          if (tempBoard[j][i] === undefined) {
            return {
              error: 2,
              word: word.word
            }
          }
          let l = tempBoard[j][i].letter
          let wordLetter = w.h ? word.word[i - word.sX].toUpperCase() : word.word[j - word.sY].toUpperCase()

          /**
         * Validate if the letter placed can be placed there
         */
          if (!this.validatePosition(l, wordLetter)) {
            return {
              error: 3,
              word: word.word
            }
          }

          /**
           * Check whether or not the letter to be placed is being placed over a letter already there
           */
          if (!validWordPlacement && tempBoard[j][i].letterPlaced) {
            validWordPlacement = true
          }

          this.tileSetter(tempBoard[j][i], wordLetter, user)
        }
      }

      /**
       * Check to see if center tile was played over on the first play
       */
      if (this._firstPlay) {
        if (!this.validateCenterTile(tempBoard)) {
          return {
            error: 4,
            word: word.word
          }
        }
        continue
      }

      if (!validWordPlacement) {
        return {
          error: 5,
          word: word.word
        }
      }
    }
    this._board = tempBoard
    return {
      error: 0,
      words: words
    }
  }

  /**
   * Helper method that sets properties of a tile
   * @param {Object} tile - tile to work with
   * @param {String} letter - letter to be set in the tile
   * @param {String} user - user that played the letter
   */
  tileSetter(tile, letter, user) {
    tile.letter = letter
    tile.playedBy = user
    tile.timePlayedAt = new Date().getTime()
  }

  /**
   * Creates an object that holds word data
   * @param {Object} w - word object
   */
  createWordObject(w) {
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
  validateCenterTile(tempBoard) {
    const c = Math.floor(this._size / 2)
    if (!tempBoard[c][c].letterPlaced) {
      return false
    } else {
      this._firstPlay = false
      return true
    }
  }

  /**
   * Validates whether a letter can be placed in the current position
   * @param {String} currentLetter - the letter that is currently on the board
   * @param {String} toBePlacedLetter - the letter that is to be placed on the board
   */
  validatePosition(currentLetter, toBePlacedLetter) {
    if (currentLetter === null) {
      return true
    } else if (currentLetter === toBePlacedLetter) {
      return true
    }

    return false
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
