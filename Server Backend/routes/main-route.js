/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')
const db = require('../helpers/DB')

module.exports = (socket) => {
/* eslint no-new:0 */
  const GameManager = require('../entities/GameManager')(socket)
  new GameManager()

  router.get('/', function(req, res, next) {
    checkUserExists(req, res)
  })

  router.get('/admin', function(req, res, next) {
    res.render('admin')
  })

  router.get('/game', function(req, res, next) {
    gateCheck(req, res)
  })

  router.post('/register', function(req, res, next) {
    let user = {
      username: req.body.username.trim(),
      team: req.body.team.trim()
    }

    addUser(req, res, user)
  })

  function addUser(req, res, user) {
    mg(req.ip, (mac) => {
      console.log(mac)
      user.mac = mac
      db.addUser(user.username, user.team, user.mac).then(t => {
        req.session.user = user
        req.session.error = undefined
        res.redirect('/')
      })
    })
  }

  function checkUserExists(req, res) {
    mg(req.ip, (mac) => {
      db.checkIfUserExists(mac)
        .then(r => {
          if (!db.pruneResults(r)) {
            req.session.user = {
              username: r.username,
              team: r.team === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green'
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
  }

  function gateCheck(req, res) {
    if (req.session.check) {
      req.session.error = undefined
      req.session.check = false
      res.render('gameboard')
    } else {
      req.session.error = 'Please login/register first.'
      res.redirect('/')
    }
  }

  return router
}
