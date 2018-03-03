/**
 * Imports the `express` and `router` modules.
 */
const express = require('express')
const router = express.Router()
const axios = require('axios')
// const VerifyToken = require('../helpers/VerifyTokens')

/**
 * This endpoint gets all the stats for the given team.
 */
router.get('/teams/:team', function(req, res, next) {

})

/**
 * This endpoint gets all the stats for the given player.
 */
router.get('/players/:player', function(req, res, next) {
  /* Requested player's name */
  const name = req.params.player

  /* Path to access the database */
  const path = 'http://localhost:8080/players/search/findByUsername?username=' + name

  /* Request player from database */
  axios.get(path).then((response) => {
    /* get returned player array */
    const players = response.data._embedded.players

    /* if the array is empty (if there is no such player) send empty JSON
       else return the player */
    if (players.length === 0) {
      res.json('{}')
    } else {
      res.send(players[0])
    }
  }).catch(error => {
    console.log('Error: ' + error)
  })
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
