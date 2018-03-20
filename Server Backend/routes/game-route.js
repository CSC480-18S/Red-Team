/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const VerifyToken = require('../helpers/VerifyTokens')
const GM = require('../entities/GameManager')

let g = new GM()
g.startNewGame()

module.exports = function(io) {
  io.on('connection', function(socket) {
    socket.emit('board', g.board)
  })
  /**
 * Returns the game board to the user
 */
  router.get('/gameBoard', function(req, res, next) {
    res.render('gameboard')
  })

  /**
 * This route will be changed, no documentation as of now
 */
  router.get('/playWords', function(req, res, next) {
    res.render('playWord')
  })

  /**
 * This route will be changed, no documentation as of now
 */
  router.post('/playWords', function(req, res, next) {
    let words = req.body
    if (!(words instanceof Array)) {
      let newWord = {
        word: words.word,
        x: JSON.parse(words.x),
        y: JSON.parse(words.y),
        h: JSON.parse(words.h)
      }

      words = [newWord]
    }

    const user = req.username

    g.play(words, res, user, io)
  })

  /**
 * Returns information about a specific route
 */
  // router.get('/tileInformation', VerifyToken, function(req, res, next) {
  //   const x = req.query.x
  //   const y = req.query.y

  //   res.json(g.tileInformation(x, y))
  // })

  return router
}
