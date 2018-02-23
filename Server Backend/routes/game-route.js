/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const GB = require('../helpers/Gameboard')

const g = new GB(11)
g.init()
g.placeWord({ x: 2, y: 2 }, { x: 7, y: 2 }, 'oswego')
g.placeWord({ x: 7, y: 1 }, { x: 7, y: 4 }, 'doge')

router.get('/gameBoard', function(req, res, next) {
  res.render('gameboard', {board: g._board})
})

router.post('/playWord', function(req, res, next) {
  const r = req.body
  const x = r.x
  const y = r.y
  const h = r.h
  const w = r.word

  const valid = g.consumeInput(x, y, h, w)

  // TODO: Fix this so the route is not doing the work
  if (valid === true) {
    res.json({invalid: false})
  } else {
    res.json({
      invalid: true,
      reason: valid.reason.toUpperCase(),
      word: valid.word.toUpperCase()
    })
  }
  res.json(valid)
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
