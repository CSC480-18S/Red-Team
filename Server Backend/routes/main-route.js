/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')
const session = require('express-session')
const db = require('../helpers/DB')

let ses = session({
  name: 'login-session',
  secret: 'testlol',
  proxy: true,
  resave: true,
  saveUninitialized: true
})

module.exports = (socket) => {
/* eslint no-new:0 */
  const GameManager = require('../entities/GameManager')(socket)
  const g = new GameManager()

  router.get('/', ses, function(req, res, next) {
    mg(req.ip, (mac) => {
      db.checkIfUserExists(mac)
        .then(r => {
          if (!db.pruneResults(r)) {
            console.log(r.username)
            req.session.user = {
              username: r.username,
              team: r.team === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green',
              mac: r.macAddr
            }
            req.session.check = true
            res.render('login', {user: req.session.user, error: req.session.error})
          } else {
            res.render('register', {error: req.session.error})
          }
        }).catch(e => {
          console.log(e)
        })
    })
  })

  router.get('/admin', function(req, res, next) {
    res.render('admin')
  })

  router.get('/game', ses, function(req, res, next) {
    if (req.session.check) {
      req.session.check = false
      res.render('gameboard')
    } else {
      req.session.error = 'Please login/register first.'
      res.redirect('/')
    }
  })

  router.post('/register', ses, function(req, res, next) {
    let user = {
      username: req.body.username.trim(),
      team: req.body.team.trim()
    }

    mg(req.ip, (mac) => {
      console.log(mac)
      user.mac = mac
      db.addUser(user.username, user.team, user.mac).then(t => {
        req.session.user = user
        req.session.error = undefined
        res.redirect('/')
      })
    })
  })

  return router
}
