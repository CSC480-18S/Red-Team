/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')

module.exports = (socket) => {
/* eslint no-new:0 */
  const GameManager = require('../entities/GameManager')(socket)
  new GameManager()

  let users = []

  router.get('/', function(req, res, next) {
    let ip = req.ip
    ip = ip.split(':')[3]
    for (let i = 0; i < users.length; i++) {
      if (users[i].mac === mg(ip)) {
        res.render('login', {user: users[i]})
        return
      }
    }

    res.render('register')
  })

  router.get('/admin', function(req, res, next) {
    res.render('admin')
  })

  router.get('/game', function(req, res, next) {
    res.render('gameboard')
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

    res.redirect('/')
  })

  return router
}
