'use strict'
const dg = require('../helpers/Debug')

class FrontendManager {
  constructor() {
    this._socket = null
    this._socketId = null
  }

  get id() {
    return this._socketId
  }

  /**
   * Listens for events coming from the frontend
   */
  listenForEvents() {
    this._socket.on('disconnect', () => {
      this.removeFrontendInformation()
    })
  }

  /**
   * Creates a connection to the frontend
   * @param {Object} socket - socket object
   */
  createHandshakeWithFrontend(socket) {
    this._socket = socket
    this._socketId = socket.id
    this.listenForEvents()
  }

  /**
   * Removes frontend information
   */
  removeFrontendInformation() {
    dg('server frontend disconnected', 'info')
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
            board: data.board,
            players: data.players.map(p => {
              return p.sendableData()
            })
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
