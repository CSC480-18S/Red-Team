'use strict'

class FrontendManager {
  constructor(socket) {
    this._socket = socket
  }

  askForAI(position) {
    this._socket.emit('connectAI', {
      position: position
    })
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FrontendManager
