/**
 * Imports the `express` and `router` modules.
 */
const express = require('express')
const router = express.Router()

/**
 * This endpoint gets all the stats for the given team.
 */
router.get('/team/:team', function(req, res, next) {

})

/**
 * This endpoint gets all the stats for the given player.
 */
router.get('/player/:player', function(req, res, next) {

})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
