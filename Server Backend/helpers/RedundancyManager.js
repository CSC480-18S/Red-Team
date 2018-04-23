'use strict'
const axios = require('axios')
const fs = require('fs')
const logger = require('./Logger')

const logPath = './logs/redundancies.log'

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
  let json = JSON.stringify(obj) + '\n'
  
  fs.appendFile(logPath, json, function(err) {
    if (err) {
      logger('Error while logging redundancy:', err)
    }
  })
}

function resend() {
  logger('Attempting resends (RedundancyManager)')

  let changes = false

  for (let i = 0; i < this._savedData.length; i++) {
    axios.post(this._savedData[i].url, this._savedData[i].data)
      .then(function(response) {
        this._savedData.splice(i, 1)
        i--
        changes = true
      })
      .catch(function(e) {
        // failed, do nothing
      })
  }

  if (changes) {
    let json = JSON.stringify(this._savedData)
    this._fm.writeFile(json)
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = {
  saveForLater,
  resend
}
