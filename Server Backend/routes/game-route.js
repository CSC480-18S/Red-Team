/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const GB = require('../helpers/Gameboard')

const g = new GB(15)
g.init()
g.placeWord({ x: 2, y: 2 }, { x: 7, y: 2 }, 'oswego')
g.placeWord({ x: 7, y: 1 }, { x: 7, y: 4 }, 'doge')

router.get('/gameBoard', function(req, res, next) {
  res.render('gameboard', {board: g._board})
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
