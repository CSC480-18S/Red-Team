/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')

let users = []

router.get('/', function(req, res, next) {
  let ip = req.ip
  ip = ip.split(':')[3]
  for (let i = 0; i < users.length; i++) {
    if (users[i].mac === mg(ip)) {
      res.render('login')
      return
    }
  }

  res.render('register')
})

router.get('/admin', function(req, res, next) {
  res.render('admin')
})

router.post('/register', function(req, res, next) {
  console.log(req.body)
  let ip = req.ip
  ip = ip.split(':')[3]
  let user = {
    username: req.body.username.trim(),
    team: req.body.team.trim(),
    mac: mg(ip)
  }

  users.push(user)

  res.json('success')
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
