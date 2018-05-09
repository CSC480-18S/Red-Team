'use strict'
const axios = require('axios')
const fm = require('./FileManager')
const logger = require('./Logger')

const TEAMS = 'http://localhost:8091/teams/'
const PLAYED_WORDS = 'http://localhost:8091/playedWords'
const LOG_PATH = './logs/redundancies.log'

/**
 * Appends data to the redundancy log.
 * @param url url to contact
 * @param data data to send
 */
function saveForLater(url, data) {
  // read file
  fm.readFile((readData) => {
    let redundancies = JSON.parse(readData)

    // add new redundancy
    redundancies.push({
      url: url,
      data: data
    })

    // write new log
    let json = JSON.stringify(redundancies)
    fm.writeFile((err) => {
      if (err) {
        logger('Failed to write redundancy log')
      }
    }, LOG_PATH, json)
  }, LOG_PATH)
}

/**
 * Attempts to resend saved redundancies to the database.
 */
function resend() {
  // read the redundancy log
  fm.readFile((readData) => {
    let redundancies = JSON.parse(readData)
    let sentIndices = []

    // attempt resends
    for (let i = 0; i < redundancies.length; ++i) {
      let url = redundancies[i].url
      let data = redundancies[i].data

      if (url === PLAYED_WORDS) {
        if (data.type === 0) {
          updatePlayer(data.player, data.words)
        } else if (data.type === 1) {
          updatePlayerDirty(data.player, data.words)
        } else if (data.type === 2) {
          updatePlayerSpecial(data.player, data.words)
        }
        sentIndices.push(i)
      } else if (url === TEAMS) {
        checkForTeams()
        sentIndices.push(i)
      } else {
        // attempt resend
        axios.post(url, data)
          // if successful, add index to successful indicies
          .then(function(response) {
            sentIndices.push(i)
          })
          .catch(function(e) {
            logger('Failed to resend redundancy [' + url + ', ' + data + ']')
          })
      }
    }

    // if any resends were successful, update log
    if (sentIndices.length > 0) {
      let newRedundancies = []

      // construct new array without successfully sent items
      for (let i = 0; i < redundancies.length; ++i) {
        // if the resend for this index wasn't successful, add it to the array
        if (!sentIndices.includes(i)) {
          newRedundancies.push(redundancies[i])
        }
      }

      // write file with new array
      let json = JSON.stringify(newRedundancies)
      fm.writeFile((err) => {
        if (err) {
          logger('Failed to write redundancy log')
        }
      }, LOG_PATH, json)
    }
  }, LOG_PATH)
}

/**
 * Updates a player's score based on the play they just made
 * @param {Object} player - player object
 * @param {Array} words - words array
 */
function updatePlayer(player, words) {
  if (words.length > 0) {
    axios.post(PLAYED_WORDS, {
      word: words[0].word,
      value: words[0].score,
      dirty: false,
      special: false,
      player: player.link
    }).then(r => {
      words.splice(0, 1)
      this.updatePlayer(player, words)
    })
      .catch(e => {
        saveForLater(PLAYED_WORDS, {
          player: player,
          words: words,
          type: 0
        })
      })
  }
}

/**
 * Updates a player's dirty word count
 * @param {Object} player - player object
 * @param {String} word - dirty word
 */
function updatePlayerDirty(player, word) {
  let play = {
    word: word,
    value: 0,
    dirty: true,
    special: false,
    player: player.link
  }
  axios.post(PLAYED_WORDS, play)
    .catch(e => {
      saveForLater(PLAYED_WORDS, {
        player: player,
        words: word,
        type: 1
      })
    })
}

/**
 * Updates a player's special word count
 * @param {Object} player - player object
 * @param {String} word - special word
 */
function updatePlayerSpecial(player, word) {
  if (!player.isAI) {
    axios.post(PLAYED_WORDS, {
      word: word,
      value: word,
      dirty: false,
      special: true,
      player: player.link
    })
      .catch(e => {
        saveForLater(PLAYED_WORDS, {
          player: player,
          words: word,
          type: 2
        })
      })
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
            saveForLater(TEAMS, {name: 'Green'})
            logger('failed checkForTeams() in DB.js: ' + e)
          })
      }
    })
    .catch(e => {
      saveForLater(TEAMS, {name: 'Gold'})
      saveForLater(TEAMS, {name: 'Green'})
      logger('failed checkForTeams() in DB.js: ' + e)
    })
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = {
  saveForLater,
  resend
}
