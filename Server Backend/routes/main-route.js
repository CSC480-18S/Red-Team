/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()

/**
 * The actual game area
 */
router.get('/game', function(req, res, next) {
  res.render('gameboard')
})

router.get('/login', function(req, res, next) {
  res.render('login')
})

router.get('/register', function(req, res, next) {
  res.render('register')
})

router.get('/admin', function(req, res, next) {
  res.render('admin')
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
