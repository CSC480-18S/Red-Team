/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()

/**
 * The actual game area
 */
router.get('/game', function(req, res, next) {
  res.render('frontend_play_area')
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
