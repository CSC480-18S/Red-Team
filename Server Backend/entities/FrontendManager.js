'use strict'
const dg = require('../helpers/Debug')

class FrontendManager {
  constructor(socket) {
    this._socket = socket
    this._socketId = socket.id
  }

  get id() {
    return this._socketId
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
   * Determines the event that needs to be sent
   * @param {String} event - name of event
   * @param {Object} data - data to be sent
   */
  sendEvent(event, data) {
    if (this._socketId !== null) {
      dg(`sending server frontend ${event} event`, 'debug')
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
            }),
            yellow: data.yellow,
            green: data.green
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
