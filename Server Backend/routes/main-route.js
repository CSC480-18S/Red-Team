/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')
const session = require('express-session')

let ses = session({
  name: 'login-session',
  secret: 'testlol'
})

module.exports = (socket) => {
/* eslint no-new:0 */
  const GameManager = require('../entities/GameManager')(socket)
  const g = new GameManager()

  let users = []

  router.get('/', ses, function(req, res, next) {
    let ip = req.ip
    ip = ip.split(':')[3]
    for (let i = 0; i < users.length; i++) {
      if (users[i].mac === mg(ip)) {
        req.session.user = users[i].username
        res.render('login', {user: users[i], error: req.session.error})
        return
      }
    }

    res.render('register', {error: req.session.error})
  })

  router.get('/admin', function(req, res, next) {
    res.render('admin')
  })

  router.get('/game', ses, function(req, res, next) {
    console.log(req.session)
    if (!req.session.user) {
      req.session.error = 'Please register/login first.'
      res.redirect('/')
    } else {
      res.render('gameboard')
      req.session.destroy()
    }
  })

  router.post('/register', ses, function(req, res, next) {
    let ip = req.ip
    ip = ip.split(':')[3]
    let user = {
      username: req.body.username.trim(),
      team: req.body.team.trim(),
      mac: mg(ip)
    }

    users.push(user)
    req.session.error = undefined

    res.redirect('/')
  })

  function gateCheck(req, res, next) {
    console.log(req.bob === true)
    next()
  }

  return router
}
