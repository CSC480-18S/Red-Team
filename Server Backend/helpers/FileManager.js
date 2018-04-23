'use strict'
const fs = require('fs')

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
            console.log('Error while making dir, ', err)
          }
        })
      }
    })
  }
  
  /**
   * Writes the data to the save file.
   * @param data data to write to save file
   * @return true if save was successful, false otherwise
   */
  writeFile(data) {
    fs.writeFile(this._path, data, function(err) {
      if (err) {
        console.log('Error while writing file, ', err)
        return false
      } else {
        return true
      }
    })
  }

  /**
   * Reads data from the save file.
   * @return data loaded, or null if there was an error
   */
  readFile() {
    fs.readFile(this._path, (err, readData) => {
      if (err) {
        console.log('Error while reading file')
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
