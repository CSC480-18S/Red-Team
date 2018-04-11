'use strict'

class FrontendManager {
  constructor(socket) {
    this._socket = socket
    this._socketId = socket.id
  }

  get id() {
    return this._socketId
  }

  /**
   * Determines the event that needs to be sent
   * @param {String} event - name of event
   * @param {Object} data - data to be sent
   */
  sendEvent(event, data) {
    console.log(`DEBUG: SENDING SERVER FRONTEND ${event.toUpperCase()} EVENT`.debug)
    switch (event) {
      case 'connectAI':
        this._socket.emit(event, {
          position: data
        })
        break
      case 'removeAI':
        this._socket.emit(event, {
          position: data
        })
        break
      case 'updateState':
        this._socket.emit(event, {
          board: data.board,
          players: data.players
        })
        break
    }
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FrontendManager
