'use strict'

const axios = require('axios')
const rm = require('./RedundancyManager')

const DICTIONARY_CHECK = 'http://localhost:8090/dictionary/validate?words='
const FIND_BY_MAC = 'http://localhost:8091/players/search/findByMacAddr?mac='
const PLAYERS = 'http://localhost:8091/players/'
const PLAYED_WORDS = 'http://localhost:8091/playedWords'

const GOLD = 'http://localhost:8091/teams/1'
const GREEN = 'http://localhost:8091/teams/2'

/**
 * Helper function for checking
 * whether a username is already
 * taken in the DB
 *
 * @param {String} mac - mac
 */
function checkIfUserExists(mac) {
  return axios.get(FIND_BY_MAC + mac, {
  })
    .then(function(response) {
      return response.data
    })
    .catch(function(e) {
      console.log(e)
    })
}

function pruneResults(res) {
  return res === undefined
}

function dictionaryCheck(search) {
  return axios.get(DICTIONARY_CHECK + search)
    .then(r => {
      return r.data
    })
    .catch(e => {
      console.log(e)
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
    .catch(e => {
      console.log(e)
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

module.exports = {
  checkIfUserExists,
  dictionaryCheck,
  addUser,
  getAllUsers,
  pruneResults,
  updatePlayerScore
}
