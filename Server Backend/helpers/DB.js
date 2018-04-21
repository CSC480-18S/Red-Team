'use strict'

const axios = require('axios')
const _ = require('lodash')

/**
 * Helper function for checking
 * whether a username is already
 * taken in the DB
 *
 * @param {String} mac - mac
 */
function checkIfUserExists(mac) {
  return axios.get('http://localhost:8091/players/search/findByMacAddr?mac=' + mac, {
  })
    .then(function(response) {
      return response.data
    })
    .catch(function(e) {
      console.log(e)
    })
}

function pruneResults(res) {
  return _.isEmpty(res)
}

/**
 * Adds user the the DB
 * @param {String} username - username
 * @param {String} team - team
 */
function addUser(username, team, mac) {
  return axios.post('http://localhost:8091/players/', {
    username: username,
    team: team === 'Gold' ? 'http://localhost:8091/teams/1' : 'http://localhost:8091/teams/2',
    macAddr: mac
  })
    .then(function(response) {
      return true
    })
    .catch(function(e) {
      console.log(e)
      return false
    })
}

/**
 * Helper function for getting
 * a user's URL in order to
 * update their total score
 * @param {String} username - username
 */
function getUserURL(mac) {
  checkIfUserExists(mac).then(response => {
    return response.data._embedded.players[0]._links.self.href
  })
    .catch(function(e) {
      throw e
    })
}

/**
 * Helper function for getting the
 * team URL to be called
 * @param {String} username - username
 */
function getTeamLink(username) {
  return axios.get('players/search/findByUsername?username=' + username, {
  })
    .then(function(response) {
      return response.data._embedded.players[0]._links.team.href
    })
    .catch(function(e) {
      throw e
    })
}

/**
 * Gets all users in the DB
 */
function getAllUsers() {
  axios.get('players', {
  })
    .then(function(response) {
      return response.data._embedded.players
    })
    .catch(e => {
      console.log(e)
    })
}

// function updatePlayerScore(){
//     /**
//  * Route that updates a user's score
//  */
// router.patch('/updateScore', function(req, res, next) {
//     /**
//        * The player score to be updated
//        */
//     const user = req.body.username
//     const totalScore = req.body.totalScore

//     /**
//        * Update score on the database
//        */
//     getUserURL(user, res).then(response => {
//       const userUrl = new URL(response)
//       const path = userUrl.pathname.substr(1)
//       axios.patch(path, {
//         totalScore: totalScore
//       })
//         .then(function(response) {
//           res.status(200).json({code: 'U2', title: 'Score updated', desc: 'Total score updated'})
//         })
//         .catch(function(e) {
//           res.status(400).json({code: 'U4', title: 'Server error', desc: 'Something went wrong'})
//         })
//     })
//       .catch(function(e) {
//         res.status(300).json({code: 'U3', title: 'User error', desc: 'User was not found'})
//       })
//   })
// }

module.exports = {
  checkIfUserExists,
  addUser,
  getUserURL,
  getTeamLink,
  getAllUsers,
  pruneResults
}
