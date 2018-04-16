/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()

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

  g.play(words, res, user)
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
