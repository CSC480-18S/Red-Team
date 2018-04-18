/**
 * Import modules.
 */
const express = require('express')
const router = express.Router()
const axios = require('axios')

const databasePort = 8091
const databasePath = 'http://localhost:' + databasePort + '/'

/**
 * This endpoint gets all the stats for the given team.
 */
router.get('/teams/:team', function(req, res, next) {
  /* Requested teams's name */
  const team = req.params.team

  /* Path to access the database */
  const path = databasePath + 'teams/search/findByName?name=' + team

  returnTeam(res, path)
})

/**
 * This endpoint gets all the stats for the given player.
 */
router.get('/players/:player', function(req, res, next) {
  /* Requested player's name */
  const name = req.params.player

  /* Path to access the database */
  const path = databasePath + 'players/search/findByUsername?username=' + name

  returnPlayer(res, path)
})

/**
 * Sends the team JSON for the given path. If there is
 * no such team, an empty JSON is sent.
 * @param {*} res response to the GUI
 * @param {String} path path for the database endpoint
 */
function returnTeam(res, path) {
  /* Request team from database */
  axios.get(path).then((response) => {
    /* get returned team array */
    // console.log(response.data + '\n')
    const teams = response.data._embedded.teams

    /* if the array is empty (if there is no such team) send empty JSON
       else return the team */
    if (teams.length === 0) {
      res.json('{}')
    } else {
      res.send(teams[0])
    }
  }).catch(error => {
    console.log('Error: ' + error)
  })
}

/**
 * Sends the player JSON for the given path. If there is
 * no such player, an empty JSON is sent.
 * @param {*} res response to the GUI
 * @param {String} path path for the database endpoint
 */
function returnPlayer(res, path) {
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
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
