/**
 * Imports the `express`, `router` and other modules
 * for using JWT, cookies
 */
const express = require('express')
const router = express.Router()
const jwt = require('jsonwebtoken')
const config = require('../config')
const VerifyToken = require('../helpers/VerifyTokens')
const cookieParser = require('cookie-parser')

router.use(cookieParser())

/**
 * Users array
 */
var users = []

/**
 * Route that creates a user
 */
router.post('/createUser', function(req, res, next) {
  /**
     * Create new user object
     */
  const newUser = {
    username: req.body.username
  }

  /**
     * TODO check the database doesn't
     * contain the requested username
     */

  /**
     * Take the user's parameters and create a
     * token for them
     */
  for (let i = 0; i < users.length; i++) {
    if (newUser.username === users[i].username) {
      res.status(500).json({code: 'U1', title: 'User error', desc: 'Username already taken'})
      return
    }
  }
  if (!newUser) {
    res.status(500).json(
      {code: 'U2', title: 'User error', desc: 'There was a problem registering the user.'})
    return
  }
  // create a token
  var token = jwt.sign({ id: newUser._id }, config.secret, {
    expiresIn: '1 year' // expires in 1 year
  })
  res.cookie('token', token, {expires: new Date(253402300000000)})

  /**
     * TODO add created user to database
     */
  users.push(newUser)

  /**
     * Returns to the user a JSON object containing the new user
     */
  res.json({username: newUser.username})
})

/**
 * Route that updates a user's score
 */
router.put('/updateScore', VerifyToken, function(req, res, next) {
  /**
     * The word score to be updated
     */
  const wscore = req.body.user

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
     * TODO look up the user on the databse
     */
  var found = null
  for (var i = 0; i < users.length; i++) {
    if (users[i].username === req.query.username) {
      found = users[i]
    }
  }

  /**
     * TODO returns a JSON containing better user information
     */
  if (!found) res.status(400).json({code: 'U3', title: 'User error', desc: 'User was not found'})
  else {
    res.json({success: 'You have been logged in!'})
  }
})

/**
 * Route that returns the user's team
 */
router.get('/currentTeam', VerifyToken, function(req, res, next) {

  /**
     * TODO look up the user on the databse
     */

  /**
     * TODO returns a String for the user's team
     */
})

/**
 * Route that sets the user's team manually
 */
router.put('/setTeam', VerifyToken, function(req, res, next) {
  /**
     * The team the user will be assigned to
     */
  const team = req.body.user

  /**
     * TODO update user's team on the database
     */

  /**
     * TODO Returns a success/fail boolean
     */
})

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
  res.json({
    all_users: users
  })
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
