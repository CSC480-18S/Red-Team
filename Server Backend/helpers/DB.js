'use strict'

const axios = require('axios')
const rm = require('./RedundancyManager')

const DICTIONARY_CHECK = 'http://localhost:8090/dictionary/validate?words='
const FIND_BY_MAC = 'http://localhost:8091/players/search/findByMacAddr?mac='
const FIND_BY_USERNAME = 'http://localhost:8091/players/search/findByUsername?username='
const PLAYERS = 'http://localhost:8091/players/'
const TEAMS = 'http://localhost:8091/teams/'
const PLAYED_WORDS = 'http://localhost:8091/playedWords'

const GOLD = 'http://localhost:8091/teams/1'
const GREEN = 'http://localhost:8091/teams/2'

/**
 * Search DB for user by mac address
 * @param {String} mac - mac address
 */
function checkIfUserExists(mac) {
  return axios.get(FIND_BY_MAC + mac, {
  })
    .then(function(response) {
      return response.data._embedded.players
    })
}

/**
 * Search DB for user by username
 * @param {String} username - username
 */
function checkIfUserNameExists(username) {
  return axios.get(FIND_BY_USERNAME + username, {
  })
    .then(function(response) {
      return response.data._embedded.players
    })
}

/**
 * Check if array of users is equal to 1
 * @param {Array} data - array of users
 */
function pruneResults(data) {
  return data.length === 1
}

/**
 * Checks the dictionary DB to see if a word is in it
 * @param {String} search - words
 */
function dictionaryCheck(search) {
  return axios.get(DICTIONARY_CHECK + search)
    .then(r => {
      return r.data
    })
}

/**
 * Adds user the the DB
 * @param {String} username - username
 * @param {String} team - team
 */
function addUser(username, team, mac) {
  let data = {
    username: username,
    team: team === 'Gold' ? GOLD : GREEN,
    macAddr: mac
  }

  return axios.post(PLAYERS, data)
    .then(function(response) {
      return true
    })
    .catch(function(e) {
      rm.saveForLater(PLAYERS, data)
      return false
    })
}

/**
 * Gets all users in the DB
 */
function getAllUsers() {
  return axios.get(PLAYERS, {
  })
    .then(function(response) {
      return response.data._embedded.players
    })
}

/**
 * Updates a player's score based on the play they just made
 * @param {Object} player - player object
 * @param {Object} score - score object
 */
function updatePlayerScore(player, words) {
  if (words.length > 0) {
    if (!player.isAI) {
      axios.post(PLAYED_WORDS, {
        word: words[0].word,
        value: words[0].score,
        dirty: false,
        special: false,
        player: player.link
      }).then(r => {
        words.splice(0, 1)
        this.updatePlayerScore(player, words)
      })
        .catch(e => {
          rm.saveForLater(PLAYERS, words)
        })
    }
  }
}

/**
 * Checks to see if the teams were instantiated on the DB, and if not, make requests to make them
 */
function checkForTeams() {
  axios.get(TEAMS)
    .then(r => {
      if (r.data._embedded.teams.length < 2) {
        axios.post(TEAMS, {
          name: 'Gold'
        }).then(r => {
          axios.post(TEAMS, {
            name: 'Green'
          })
        })
          .catch(e => {
            rm.saveForLater(TEAMS, {name: 'Green'})
          })
      }
    })
    .catch(e => {
      rm.saveForLater(TEAMS, {name: 'Gold'})
      rm.saveForLater(TEAMS, {name: 'Green'})
    })
}

/**
 * Gets the player's team based on their mac address
 * @param {String} mac - mac address
 */
function getTeamURL(mac) {
  return axios.get(FIND_BY_MAC + mac, {
  })
    .then(function(response) {
      return axios.get(response.data._embedded.players[0]._links.team.href, {
      })
        .then(function(response) {
          return response.data._links.team.href
        })
    })
}

module.exports = {
  checkIfUserExists,
  checkIfUserNameExists,
  dictionaryCheck,
  addUser,
  getAllUsers,
  pruneResults,
  updatePlayerScore,
  checkForTeams,
  getTeamURL
}
