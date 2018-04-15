'use strict'

class FrontendManager {
  constructor() {
    this._socket = null
    this._socketId = null
    this._boardInformation = null
    this._playersInformation = null
    this.listenForEvents()
  }

  get id() {
    return this._socketId
  }

  updateGameInformation(board, players) {
    this._boardInformation = board
    this._playersInformation = players
  }

  /**
   * Listens for events coming from the frontend
   */
  listenForEvents() {
    if (this._socketId !== null) {
      this._socket.on('disconnect', () => {
        this.removeFrontendInformation()
      })
    }
  }

  /**
   * Creates a connection to the frontend
   * @param {Object} socket - socket object
   */
  createHandshakeWithFrontend(socket) {
    this._socket = socket
    this._socketId = socket.id
  }

  /**
   * Removes frontend information
   */
  removeFrontendInformation() {
    console.log('INFO: SERVER FRONTEND DISCONNECTED'.info)
    this._socket = null
    this._socketId = null
  }

  /**
   * Determines the event that needs to be sent
   * @param {String} event - name of event
   * @param {Object} data - data to be sent
   */
  sendEvent(event, data) {
    if (this._socketId !== null) {
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
            board: this._boardInformation,
            players: this._playersInformation
          })
          break
      }
    }
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FrontendManager
