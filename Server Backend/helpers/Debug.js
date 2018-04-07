/**
 * Imports the console colors package
 */
const colors = require('colors')

/**
 * Sets the theme of all of the colors to nicer names
 */
colors.setTheme({
  input: 'grey',
  verbose: 'cyan',
  prompt: 'grey',
  info: 'green',
  data: 'grey',
  help: 'cyan',
  warn: 'yellow',
  debug: 'blue',
  error: ['red', 'underline', 'bold'],
  arrow: ['bold', 'red']
})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = colors
