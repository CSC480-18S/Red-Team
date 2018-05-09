/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const mg = require('../helpers/MacGrabber')
const db = require('../helpers/DB')

router.get('/', function(req, res, next) {
  // if (!req.session.user) {
  checkUserExists(req, res)
  // res.render('register', {error: req.session.error})
  // } else {
  //   res.render('login', {user: req.session.user, error: req.session.error})
  // }
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
    user.mac = mac
    console.log(user)
    db.checkIfUserNameExists(user.username).then(r => {
      if (db.pruneResults(r)) {
        req.session.error = 'Username is already taken'
        res.redirect('/')
      } else {
        db.addUser(user.username, user.team, user.mac).then(t => {
          req.session.user = user
          req.session.error = undefined
          res.redirect('/')
        })
      }
    })
  })
}

function checkUserExists(req, res) {
  mg(req.ip, (mac) => {
    db.checkIfUserExists(mac)
      .then(r => {
        if (db.pruneResults(r)) {
          db.getTeamURL(mac)
            .then(r2 => {
              req.session.user = {
                username: r[0].username,
                team: r2 === 'http://localhost:8091/teams/1' ? 'Gold' : 'Green'
              }
              req.session.check = true
              res.render('login', {user: req.session.user, error: req.session.error})
            })
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

module.exports = router
