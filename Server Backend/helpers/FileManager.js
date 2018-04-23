'use strict'
const fs = require('fs')
const logger = require('./Logger')

class FileManager {
  
  /**
   * @param dir path to save directory (ex. "./dir1/dir2")
   * @param filename name of save file (ex. "savefile.txt")
   */
  constructor(dir, filename) {
    this._dir = dir
    this._filename = filename
    this._path = dir + '/' + filename
    this._data = null
    
    fs.exists(dir, function(exists) {
      if (!exists) {
        fs.mkdir(dir, function(err) {
          if (err) {
            logger('Error while making dir, ', err)
          } else {
            logger('Created log dir')
          }
        })
      } else {
        logger('Dir already exists')
      }
    })
  }
  
  /**
   * Writes data to the save file.
   * @param data data to write to save file
   */
  writeFile(data) {
    fs.writeFile(this._path, data, function(err) {
      if (err) {
        logger('Error while writing file, ', err)
      }
    })
  }
  
  /**
   * Appends data to the save file.
   * @param data data to append to save file
   */
  appendFile(data) {
    fs.appendFile(this._path, data, function (err) {
      if (err) {
        logger('Error while writing file, ', err)
      }
    })
  }
  
  /**
   * Reads data from the save file into local data.
   * Use data getter to retrieve.
   */
  readFile() {
    fs.readFile(this._path, (err, readData) => {
      if (err) {
        logger('Error while reading file')
      } else {
        this._data = readData
      }
    })
  }
  
  get data() {
    return this._data
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FileManager
