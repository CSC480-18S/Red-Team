/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()

/**
 * Route for the index page (default page because the route is just `/`)
 * @returns {HTML} - tells the user that the server is working
 */
router.get('/', function(req, res, next) {
  res.send('<h1>Server is working</h1>')
})

router.get('/test', function(req, res, next) {
  res.render('socketTest')
})

router.get('/testF', function(req, res, next) {
  res.render('serverTest')
})

router.get('/game', function(req, res, next) {
  res.render('frontend_play_area')
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
