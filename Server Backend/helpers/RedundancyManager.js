'use strict'
const fs = require('fs')
const axios = require('axios')
const FileManager = require('./FileManager')

const logsDir = './logs'
const logFile = 'redundancyManager.log'

class RedundancyManager {
  
  constructor() {
    this._savedData = []
    this._rawData = ''
    this._fm = new FileManager(logsDir, logFile)
    this._fm.readFile()
  }
  
  /**
   * Adds an item to be resent later.
   */
  saveForLater(url, data) {
    this._savedData.push({
      url: url,
      data: data
    })
    
    this._fm.writeFile(JSON.stringify(this._savedData))
  }
  
  /**
   * Loads the log.
   */
  loadLog() {
    this._savedData = JSON.parse(this._fm.data)
  }
  
  /**
   * Attempts to resend the saved data.
   */
  resend() {
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
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = RedundancyManager
