'use strict'
const fs = require('fs')
const logger = require('./Logger')

/**
 * Creates a directory.
 * @param dirPath directory path to create
 * @return true if there was an error, false otherwise
 */
function makeDir(callback, dirPath) {
  fs.mkdir(dirPath, function(err) {
    if (err) {
      logger('Error while creating dir "' + dirPath + '", err: ' + err)
    }
    
    callback(err)
  })
}

/**
 * Reads a file.
 * @param filePath filepath to read from
 * @return data read from the file, null if there was an error
 */
function readFile(callback, filePath) {
  fs.readFile(filePath, (err, readData) => {
    if (err) {
      logger('Error while reading file "' + filePath + '", err: ' + err)
      return null
    } else {
      callback(readData)
    }
  })
}

/**
 * Writes to a file.
 * @param filePath filepath to write to
 * @return true if there was an error, false otherwise
 */
function writeFile(callback, filePath, data) {
  fs.writeFile(filePath, data, function(err) {
    if (err) {
      logger('Error while writing file "' + filePath + '", err: ' + err)
    }
    
    callback(err)
  })
}

/**
 * Appends to a file.
 * @param filePath filepath to append to
 * @return true if there was an error, false otherwise
 */
function appendFile(callback, filePath, data) {
  fs.appendFile(filePath, data, function(err) {
    if (err) {
      logger('Error while writing file "' + filePath + '", err: ' + err)
    }
    
    callback(err)
  })
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = {
  makeDir,
  readFile,
  writeFile,
  appendFile
}
