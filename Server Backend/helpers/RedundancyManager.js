'use strict'
const axios = require('axios')
const fm = require('./FileManager')
const logger = require('./Logger')

const LOG_PATH = './logs/redundancies.log'

let redundancies = []

/**
 * Appends data to the redundancy log.
 * @param url url to contact
 * @param data data to send
 */
function saveForLater(url, data) {
  let obj = {
    url: url,
    data: data
  }
  redundancies.push(obj)
  
  let json = JSON.stringify(redundancies)
  fm.writeFile((err) => {
    if (err) {
      logger('Failed to save redundancy [' + url + ', ' + data + ']')
    }
  }, LOG_PATH, json)
}

/**
 * Attempts to resend saved redundancies to the database.
 */
function resend() {
  logger('Attempting RM resends')
  
  fm.readFile((readData) => {
    let logJSON = readData
    redundancies = JSON.parse(logJSON)
    
    let changes = false

    for (let i = 0; i < redundancies.length; ++i) {
      let url = redundancies[i].url
      let data = redundancies[i].data
      
      axios.post(url, data)
        .then(function(response) {
          redundancies.splice(i, 1)
          i--
          changes = true
        })
        .catch(function(e) {
          logger('Failed to resend redundancy [' + url + ', ' + data + ']')
        })
    }

    if (changes) {
      let json = JSON.stringify(redundancies)
      fm.writeFile(LOG_PATH, json)
    }
  }, LOG_PATH)
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = {
  saveForLater,
  resend
}
