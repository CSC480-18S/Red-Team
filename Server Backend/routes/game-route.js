/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const VerifyToken = require('../helpers/VerifyTokens')
const GM = require('../entities/GameManager')

let g = new GM()

/**
 * Returns the game board to the user
 */
// router.get('/gameBoard', VerifyToken, function(req, res, next) {
//   res.render('gameboard', {board: g._board})
// })

/**
 * This route will be changed, no documentation as of now
 */
router.post('/playWords', VerifyToken, function(req, res, next) {
  const words = req.body
  const user = req.username

  g.play(words, res, user)
})

/**
 * Returns information about a specific route
 */
// router.get('/tileInformation', VerifyToken, function(req, res, next) {
//   const x = req.query.x
//   const y = req.query.y

//   res.json(g.tileInformation(x, y))
// })

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
