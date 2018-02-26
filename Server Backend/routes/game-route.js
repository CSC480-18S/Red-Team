/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const GB = require('../helpers/Gameboard')

const g = new GB(11)
g.init()
// g.placeWord({ x: 2, y: 2 }, { x: 7, y: 2 }, 'oswego')
// g.placeWord({ x: 7, y: 1 }, { x: 7, y: 4 }, 'doge')

/**
 * Returns the game board to the user
 */
router.get('/gameBoard', function(req, res, next) {
  res.render('gameboard', {board: g._board})
})

/**
 * This route will be changed, no documentation as of now
 */
router.post('/playWord', function(req, res, next) {
  const r = req.body
  const x = r.x
  const y = r.y
  const h = r.h
  const w = r.word

  const valid = g.consumeInput(x, y, h, w)

  // TODO: Fix this so the route is not doing the work @Landon
  if (valid === true) {
    res.json({invalid: false})
  } else {
    res.json({
      invalid: true,
      reason: valid.reason.toUpperCase(),
      word: valid.word.toUpperCase()
    })
  }
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
