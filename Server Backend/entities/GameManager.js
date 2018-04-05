'use strict'
/**
 * Imports lodash and axios
 */
const axios = require('axios')
const _ = require('lodash')
/**
 * Imports the Gameboard class
 */
const Gameboard = require('./Gameboard')

// remove this once there is a connection to the DB
const letters = 'abcdefghijklmnopqrstuvwxyz'.split('')
const tiles = letters.map(t => {
  return {
    letter: t,
    score: 1
  }
})

// letter distribution, alphabetically
const letterDist = [9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1]
let totalLetters = 0
let intervals = []

class GameManager {
  constructor(io) {
    this._board = new Gameboard()
    this._tileScores = []
    this._greenScore = 0
    this._error = 0
    this._yellowScore = 0

    // set up intervals
    // push first interval
    intervals.push(letterDist[0])
    totalLetters += letterDist[0]
    // add the rest of the intervals
    for (let i = 1; i < letterDist.length; ++i) {
      intervals.push(intervals[i - 1] + letterDist[i])
      totalLetters += letterDist[i]
    }
    this._io = io
  }

  /**
   * Board getter
   */
  get board() {
    return this._board.board
  }

  // play(newBoard, player) {
  //   newBoard = JSON.parse(newBoard)

  //   for (let i = 0; i < newBoard.length; i++) {
  //     for (let j = 0; j < newBoard[0].length; j++) {
  //       let newBoardLetter = JSON.parse(newBoard[j][i])
  //       this.board[j][i].letter = newBoardLetter
  //     }
  //   }
  //   this._io.emit('wordPlayed', this.board)
  // }

  play(newBoard, player) {
    // let sanitizedBoard = JSON.parse(newBoard)
    // console.log(newBoard)
    let letters = this.extractLetters(newBoard)
    let words = this.extractWords(letters, newBoard)
    this._board.placeWords(words, player) // need to validation after this, especially for words
    this._io.emit('wordPlayed', this._board.sendableBoard())

    // this.wordValidation(words)
    //   .then(response => {
    //     console.log('The board now has an answer')
    //     let placement
    //     if (response === true) {
    //       // if invalid type of play, gets the word that was invalid, else is undefined
    //       placement = this._board.placeWords(words, player)
    //     } else {
    //       // if the word is invalid
    //       return this.handleResponse(this._error, response, player)
    //     }
    //     this.handleResponse(this._board.error, placement, player)
    //     this._io.emit('wordPlayed', this._board)
    //   })
    //   .catch(e => {
    //     console.log({code: 'D1', title: 'Database Error', desc: e.code})
    //   })
    // console.log('The board is thinking')
  }

  /**
   * Extracts letters from the new board that were played by the user
   * @param {Array} newBoard - board given by the user
   */
  extractLetters(newBoard) {
    let letters = []
    for (let i = 0; i < newBoard.length; i++) {
      for (let j = 0; j < newBoard[0].length; j++) {
        let currentBoardLetter = this.board[j][i].letter
        let newBoardLetter = newBoard[j][i]

        if (newBoardLetter !== null) {
          if (currentBoardLetter !== newBoardLetter) {
            let tile = {
              letter: newBoardLetter,
              x: i,
              y: j
            }
            letters.push(tile)
          } else {
            console.log('same letters')
          }
        }
      }
    }
    return letters
  }

  /**
   * Extracts words out of the newboard
   * @param {Array} letters - array of letters
   * @param {Array} newBoard- board
   */
  extractWords(letters, newBoard) {
    let words = []

    letters.map(letterObject => {
      for (let i = 0; i < 2; i++) {
        let word = ''
        let startOfWord = false
        let endOfWord = false
        let startPosition = null
        let position = {
          x: letterObject.x,
          y: letterObject.y
        }

        while (!startOfWord) {
          if (position.x < 0 || position.y < 0 || newBoard[position.y][position.x] === null) {
            startOfWord = true
            position.x = i === 0 ? position.x : position.x + 1
            position.y = i === 0 ? position.y + 1 : position.y
          } else {
            position.x = i === 0 ? position.x : position.x - 1
            position.y = i === 0 ? position.y - 1 : position.y
          }
        }

        startPosition = _.cloneDeep(position)

        while (!endOfWord) {
          if (position.x >= newBoard.length || position.y >= newBoard.length || newBoard[position.y][position.x] === undefined || newBoard[position.y][position.x] === null) {
            endOfWord = true
          } else {
            word += newBoard[position.y][position.x]
            position.x = i === 0 ? position.x : position.x + 1
            position.y = i === 0 ? position.y + 1 : position.y
          }
        }

        if (word.length > 1) {
          let wordObject = {
            word: word,
            x: startPosition.x,
            y: startPosition.y,
            h: i === 1
          }

          words.push(wordObject)
        }
        word = ''
      }
    })

    return _.uniqWith(words, _.isEqual)
  }

  /**
   * Checks to see if word(s) are in the DB
   * @param {Array} words - words to be checked against the DB
   */
  wordValidation(words) {
    let search = words.map(s => s.word).join(',')

    return axios.get('http://localhost:8090/dictionary/validate?words=' + search)
      .then(res => {
        return this.pruneResults(res.data)
      })
  }

  /**
   * Prunes the data sent back from the DB to check if anywords are either invalid or bad words
   * @param {Array} response - word data sent back from DB
   */
  pruneResults(response) {
    for (let word of response) {
      if (word.bad) {
        this._error = 6
        return word.word
      }
      if (!word.valid) {
        this._error = 1
        return word.word
      }
    }

    return true
  }

  /**
   * Handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
  handleResponse(error, word, player) {
    const result = {
      invalid: true
    }
    let reason = null

    switch (error) {
      case 1:
        reason = 'Not a valid word'
        break
      case 2:
        reason = 'Placed out of the bounds of the board'
        break
      case 3:
        reason = 'Invalid placement'
        break
      case 4:
        reason = 'Word was not played over the center tile'
        break
      case 5:
        reason = 'Word not connected to played tiles'
        break
      case 6:
        reason = 'Word is a bad word'
        break
      default:
        result.invalid = false
    }
    if (result.invalid) {
      result['reason'] = reason.toUpperCase()
      result['word'] = word.toUpperCase()
    }

    this._error = 0
    player.socket.emit('play', result)
  }

  /**
   * Determines what new letters a player will get after they
   * play their turn.
   * @param {int} lettersUsed - number of letters to generate
   */
  getNewLetters(lettersUsed) {
    let newLetters = []

    // generate the new letters
    for (let a = 0; a < lettersUsed; ++a) {
      let index = Math.floor(Math.random() * totalLetters)

      for (let i = 0; i < intervals.length; ++i) {
        if (index <= intervals[i]) {
          newLetters.push(letters[i])
          break
        }
      }
    }

    return newLetters
  }

  /**
   * Calculates the score of a play
   * @param {Object} player - player to add score to
   * @param {Array} words - array of words to calculate score for
   * @param {Object} bonus - bonus to factor in
   */
  calculateScore(player, words, bonus) {
    let cumulativeScore = 0

    words.map(w => {
      let wordArray = w.toUpperCase().split('')

      let score = wordArray.map(l => {
        for (let t of tiles) {
          if (t.letter === l) {
            if (bonus.type === 'letter' && bonus.letter === l) {
              return t.score * bonus.bonus
            }
            return t.score
          }
        }
      }).reduce((prev, curr) => {
        return prev + curr
      })

      if (bonus.type === 'word') {
        score = score * bonus.bonus
      }

      cumulativeScore += score
    })

    this.addScore(player, cumulativeScore)
  }

  /**
   * Adds the score to the player's score and the team they are on
   * @param {Object} player - player to add score to
   * @param {Number} score - score
   */
  addScore(player, score) {
    // Need to update DB as well
    player.addScore(score)

    switch (player.team) {
      case 'Green':
        this._greenScore += score
        break
      case 'Yellow':
        this._yellowScore += score
        break
    }
  }

  /**
   * Starts a new game by resetting everything
   */
  startNewGame() {
    this.resetScores()
    this.resetGameboard()
  }

  /**
   * Creates a new gameboard and initializes it
   */
  resetGameboard() {
    this._board = new Gameboard()
  }

  /**
   * Resets all players' scores
   */
  resetScores() {
    this._greenScore = 0
    this._yellowScore = 0

    this._players.map(p => {
      p.resetScore()
    })
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = GameManager
