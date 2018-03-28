'use strict'
/**
 * Imports lodash and axios
 */
const _ = require('lodash')
const axios = require('axios')

/**
 * Imports the Tile and GameboardResponse class
 */
const Tile = require('./Tile')

class Gameboard {
  /**
   * @param {Number} size - the size of the board for both height and width
   */
  constructor() {
    this._size = 11
    this._board = new Array(this._size)
    this._initialized = false
    this._firstPlay = true
    this._error = 0
  }

  /**
   * Sets the board
   */
  set board(board) {
    this._board = board
  }

  /**
   * Sets initialized
   */
  set initialized(initialized) {
    this._initialized = initialized
  }

  /**
   * Sets first play
   */
  set firstPlay(firstPlay) {
    this._firstPlay = firstPlay
  }

  /**
   * Sets error
   */
  set error(error) {
    this._error = error
  }

  /**
   * Size getter
   */
  get size() {
    return this._size
  }

  /**
   * Board getter
   */
  get board() {
    return this._board
  }

  /**
   * Initialized getter
   */
  get initialized() {
    return this._initialized
  }

  /**
   * First play getter
   */
  get firstPlay() {
    return this._firstPlay
  }

  /**
   * Error getter
   */
  get error() {
    return this._error
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
        this._board[i][j] = new Tile(j, i, '1')
      }
    }

    this._initialized = true
  }

  /**
   * Method that places words on the baord
   * @param {Array} words - array of words to place
   */
  placeWords(words, user) {
    this.error = 0
    const tempBoard = _.cloneDeep(this.board)

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
            this.error = 2
            return word.word
          }
          let l = tempBoard[j][i].letter
          let wordLetter = w.h ? word.word[i - word.sX].toUpperCase() : word.word[j - word.sY].toUpperCase()

          /**
         * Validate if the letter placed can be placed there
         */
          if (!this.validatePosition(l, wordLetter)) {
            this.error = 3
            return word.word
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
      if (this.firstPlay) {
        if (!this.validateCenterTile(tempBoard)) {
          return word.word
        }
        continue
      }

      if (!validWordPlacement) {
        this.error = 5
        return word.word
      }
    }

    this.board = tempBoard
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
   * Validates whetehr the first played word was played over the center tile
   * @param {Array} tempBoard - temp board
   */
  validateCenterTile(tempBoard) {
    const c = Math.floor(this.size / 2)
    if (!tempBoard[c][c].letterPlaced) {
      this.error = 4
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
  validatePosition(currentLetter, toBePlacedLetter) {
    if (currentLetter === '.') {
      return true
    } else if (currentLetter === toBePlacedLetter) {
      return true
    }

    return false
  }

  /**
   * Pulls information about a sepcific tile
   * X and Y need to be reversed because of the way 2D arrays are created
   * @param {Number} x - x coordinate
   * @param {Number} y - y coordinate
   */
  tileInformation(x, y) {
    return this._board[y][x]
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
