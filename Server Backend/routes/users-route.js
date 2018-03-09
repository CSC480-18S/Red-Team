/**
 * Imports the `express`, `router` and other modules
 * for using JWT, cookies, and axios
 */
const express = require('express')
const router = express.Router()
const jwt = require('jsonwebtoken')
const Axios = require('axios')
const config = require('../config')
const VerifyToken = require('../helpers/VerifyTokens')
const cookieParser = require('cookie-parser')
const {URL} = require('url')

router.use(cookieParser())

const axios = Axios.create({baseURL: 'http://localhost:8091/'})

var error = 0

/**
 * Route that creates a user
 */
router.post('/createUser', function(req, res, next) {
  const newUser = {
    username: req.body.username,
    team: req.body.team
  }

  checkUserExists(newUser).then(response => {
    if (response.length > 0) {
      error = 1
      return handleErrors(newUser.username)
    }
    return addUser(newUser)
  }).then(response => {
    // create a token
    if (error === 0) {
      let token = jwt.sign({ id: newUser._id }, config.secret, {
        expiresIn: '1 year' // expires in 1 year
      })
      // save token as cookie
      res.cookie('token', token, { expires: new Date(253402300000000) })
      res.json({ username: newUser.username, team: newUser.team })
      return
    }
  }
  if (!newUser) {
    res.status(500).json(
      {code: 'U2', title: 'User error', desc: 'There was a problem registering the user.'})
    return
  }
  // create a token
  var token = jwt.sign({ username: newUser.username }, config.secret, {
    expiresIn: '1 year' // expires in 1 year
  })
})

/**
 * Helper function for adding
 * players to the DB
 */
function addUser(newUser) {
  /**
 * Take GUIs team name and
 * translate it to the proper
 * URL for database
 */
  var teamURL
  if (newUser.team === 'Gold') {
    teamURL = 'http://localhost:8091/teams/1'
  } else if (newUser.team === 'Green') {
    teamURL = 'http://localhost:8091/teams/2'
  } else throw error

  return axios.post('players', {
    username: newUser.username,
    team: teamURL
  })
    .then(function(response) {
      console.log('player added')
    })
    .catch(function(e) {
      error = 2
    })
}

/**
 * Helper function for checking
 * whether a username is already
 * taken in the DB
 */
function checkUserExists(newUser) {
  return axios.get('players/search/findByUsername?username=' + newUser.username, {
  })
    .then(function(response) {
      return response.data._embedded.players
    })
    .catch(function(e) {
      error = 1
    })
}

/**
 * Helper function for handling
 * error messages when creating
 * users
 */
function handleErrors(username) {
  const result = {
    invalid: true
  }
  let reason = null

  result['username'] = username.toUpperCase()

  switch (error) {
    case 1:
      reason = 'Username is already taken'
      break
    case 2:
      reason = 'There was a problem registering the user'
      break
    default:
      result.invalid = false
      return result
  }

  result['reason'] = reason.toUpperCase()

  return result
}

/**
 * Route that updates a user's score
 */
router.put('/updateScore', VerifyToken, function(req, res, next) {
  /**
     * The player score to be updated
     */
  const pscore = req.body.user

  /**
     * TODO update scores on the database
     */

  /**
     * TODO Returns a success/fail boolean
     */
})

/**
 * Route that gets a user's data
 */
router.get('/getUser', VerifyToken, function(req, res, next) {
  /**
     * Looks up the user on the database
     */
  axios.get('players/search/findByUsername?username=' + req.query.username, {
  })
    .then(function(response) {
      if (response.data._embedded.players[0]) {
        console.log('You have been logged in')
        res.json(response.data._embedded.players[0])
      } else {
        throw error
      }
    })
    .catch(function(e) {
      res.status(400).json({code: 'U3', title: 'User error', desc: 'User was not found'})
    })
})

/**
 * Route that returns the user's team
 */
router.get('/currentTeam', VerifyToken, function(req, res, next) {
  /**
     * Looks up the user on the database
     */
  // axios.get('players/search/findByUsername?username=' + req.query.username, {
  // })
  //   .then(function(response) {
  //     if (response.data._embedded.players[0]._links.team.href) {
  //       res.json(response.data._embedded.players[0]._links.team.href)
  //     } else {
  //       throw error
  //     }
  //   })
  //   .catch(function(e) {
  //     res.status(400).json({code: 'U4', title: 'Server error', desc: 'Something went wrong'})
  //   })

  getTeamLink(req.query.username, res).then(response => {
    const teamUrl = new URL(response)
    const path = teamUrl.pathname.substr(1)
    axios.get(path, {
    })
      .then(function(response) {
        res.json(response.data)
      })
      .catch(function(e) {
        res.status(400).json({code: 'U4', title: 'Server error', desc: 'Something went wrong'})
      })
  })
    .catch(function(e) {
      res.status(400).json({code: 'U4', title: 'Server error', desc: 'Something went wrong'})
    })
})

/**
 * Helper function for checking
 * whether a username is already
 * taken in the DB
 */
function getTeamLink(username, res) {
  return axios.get('players/search/findByUsername?username=' + username, {
  })
    .then(function(response) {
      return response.data._embedded.players[0]._links.team.href
    })
    .catch(function(e) {
      res.status(400).json({code: 'U3', title: 'User error', desc: 'User was not found'})
    })
}

/**
 * Route that adds the user to the current game
 */
router.put('/addUser', VerifyToken, function(req, res, next) {

  /**
     * TODO update the databse to reflect the user that joined
     */

  /**
     * TODO Returns a success/fail boolean
     */
})

/**
 * Route that returns all of the users
 */
router.get('/allUsers', function(req, res, next) {
  /**
     * Sends the user a JSON object containing all of the users
     */
  axios.get('players', {
  })
    .then(function(response) {
      res.json(response.data._embedded.players)
    })
    .catch(function(e) {
      res.status(500).json({code: 'U4', title: 'Server error', desc: 'Something went wrong'})
    })
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
