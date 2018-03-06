/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const GB = require('../entities/Gameboard')
const VerifyToken = require('../helpers/VerifyTokens')

let g = new GB()
g.init()

/**
 * Returns the game board to the user
 */
router.get('/gameBoard', VerifyToken, function(req, res, next) {
  res.render('gameboard', {board: g._board})
})

/**
 * This route will be changed, no documentation as of now
 */
router.post('/playWord', VerifyToken, function(req, res, next) {
  const r = req.body

  g.consumeInput(r.x, r.y, r.h, r.word, res)
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
