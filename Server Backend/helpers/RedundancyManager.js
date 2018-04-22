'use strict'
const fs = require('fs')
const axios = require('axios')

const logsDir = './logs'
const logFile = logsDir + '/redundancyManager.log'

let savedData = []

/**
 * Adds an item to be resent later.
 */
function saveForLater(url, data) {
  savedData.push({
    url: url,
    data: data
  })

  writeFile()
}

/**
 * Creates the log dir if necessary and loads the log.
 */
function init() {
  fs.exists(logsDir, function(exists) {
    if (!exists) {
      fs.mkdir(logsDir, function(err) {
        if (err) {
          console.log('Error while making dir, ', err)
        }
      })
    }
  })

  fs.readFile(logFile, (err, data) => {
    if (err) {
      console.log('Error while reading file')
    } else {
      savedData = JSON.parse(data)
      resend()
    }
  })
}

/**
 * Attempts to resend the saved data.
 */
function resend() {
  let changes = false

  for (let i = 0; i < savedData.length; i++) {
    axios.post(savedData[i].url, savedData[i].data)
      .then(function(response) {
        savedData.splice(i, 1)
        i--
        changes = true
      })
      .catch(function(e) {
      // failed, do nothing
      })
  }

  if (changes) {
    writeFile()
  }
}

/**
 * Writes the savedData array to the log file.
 */
function writeFile() {
  fs.writeFile(logFile, JSON.stringify(savedData), function(err) {
    if (err) {
      console.log('Error while writing file, ', err)
    } else {
      console.log('Data saved')
    }
  })
}

module.exports = {
  saveForLater,
  init
}
