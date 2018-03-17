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
const gr = require('../helpers/GameboardResponse')

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
    /**
     * TODO: We may not want to have this here so that we don't have to create a brand new board object
     * everytime a new game is made, we can just use the one we already created. @Landon
     */
    if (this._initialized) {
      return true
    }

    for (let i = 0; i < this._board.length; i++) {
      this._board[i] = new Array(this._size)

      for (let j = 0; j < this._board[0].length; j++) {
        this._board[i][j] = new Tile(j, i, '1')
      }
    }

    this.initialized = true
  }

  /**
   * Consumes the input given by the frontend
   * @param {Array} words - words given by the frontend
   * @param {Object} res - result object
   */
  consumeInput(words, res) {
    let sanitized = this.trimWords(words)

    this.wordsAreValid(sanitized).then(response => {
      console.log('The board now has an answer')
      let placement
      if (response === true) {
        placement = this.placeWords(sanitized)
      } else {
        return gr(this.error, response, res)
      }
      return gr(this.error, placement, res)
    }).catch(e => {
      return res.json(e)
    })
    console.log('The board is thinking')
  }

  /**
   * Removes whitespace from words
   * @param {Array} words - words to trimmed
   */
  trimWords(words) {
    return words.map(w => {
      let word = w.word
      w['word'] = word.trim()
      return w
    })
  }

  /**
   * Method that checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordsAreValid(words) {
    let search = words.map(s => s.word).join(',')

    return axios.get('http://localhost:8090/dictionary/validate?words=' + search)
      .then(res => {
        return this.pruneResults(res.data)
      }).catch((e) => {
        console.log(e)
      })
  }

  /**
   * Prunes the data sent back from the DB to check if any words are either invalid or bad words
   * @param {Array} response - word data sent back from DB
   */
  pruneResults(response) {
    for (let word of response) {
      if (word.bad) {
        this.error = 6
        return word.word
      }
      if (!word.valid) {
        this.error = 1
        return word.word
      }
    }

    return true
  }

  /**
   * Method that places words on the baord
   * @param {Array} words - array of words to place
   */
  placeWords(words) {
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

          tempBoard[j][i].letter = wordLetter
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

      /**
       * Check to see if the word was played over already played words
       */
      if (!validWordPlacement) {
        this.error = 5
        return word.word
      }
    }

    this.board = tempBoard
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
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = Gameboard
