'use strict'
const fs = require('fs')

const logsDir = './logs'
const logFile = 'applog.log'
const logPath = logsDir + '/' + logFile

/**
 * Appends data to the application log with a date/time stamp
 * and newline character.
 * @param data text to append
 */
function log(data) {
  let date = new Date() + ''
  let dateString = date.split(' GMT')[0]
  let totalString = dateString + ': ' + data + '\n'

  fs.appendFile(logPath, totalString, function(err) {
    if (err) {
      console.log('Error while logging:', err)
    }
  })
}

module.exports = log
