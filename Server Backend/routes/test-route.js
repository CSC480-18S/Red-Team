/**
 * Imports the `express` and `router`
 */
const express = require('express')
const router = express.Router()

router.get('/login', function(req, res, next) {
  res.render('login/login')
})

router.get('/teamselection', function(req, res, next) {
  res.render('login/teamselection')
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
