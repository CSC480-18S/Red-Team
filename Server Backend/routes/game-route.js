/**
 * Imports the `express` and `router` modules
 */
const express = require('express')
const router = express.Router()
const GB = require('../helpers/Gameboard')

const g = new GB(13)
g.init()

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
