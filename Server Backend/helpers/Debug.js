'use strict'
/**
 * Imports the console colors package
 */
require('colors')
const development = true

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = (message, type) => {
  message = message.toUpperCase()
  if (development) {
    switch (type) {
      case 'verbose':
        console.log(`VERBOSE: ${message}`.cyan)
        break
      case 'info':
        console.log(`INFO: ${message}`.green)
        break
      case 'data':
        console.log(`DATA: ${message}`.grey)
        break
      case 'warn':
        console.log(`WARN: ${message}`.yellow)
        break
      case 'debug':
        console.log(`DEBUG: ${message}`.blue)
        break
      case 'error':
        console.log(`ERROR: ${message}`.red.underline.bold)
        break
      default:
        console.log(`${message}`)
        break
    }
  } else {
  /**
   * Should log to file as well
   */
  }
}
