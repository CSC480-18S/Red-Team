/**
 * Imports the `express`, `router` and other modules
 * for using JWT and cookies
 */
const express = require('express')
const router = express.Router()
const jwt = require('jsonwebtoken')
const config = require('../config')
const cookieParser = require('cookie-parser')

router.use(cookieParser())

/**
 * Middleware function to authorize a user
 * based on the token
 */
function verifyToken(req, res, next) {
  var token = req.cookies.token
  if (!token) { return res.status(403).send({ auth: false, message: 'No token provided.' }) }

  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) { return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' }) }

    // if everything good, save to request for use in other routes
    req.userId = decoded.id
    next()
  })
}

module.exports = verifyToken
