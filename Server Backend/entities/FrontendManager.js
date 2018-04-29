'use strict'
const dg = require('../helpers/Debug')(true)

class FrontendManager {
  constructor(socket, number, disconnect) {
    this.socket = socket
    this.number = number
    this.disconnect = disconnect
    dg(`frontend ${number} added`, 'debug')
    this.listenForDisconnect()
  }

  listenForDisconnect() {
    this.socket.on('close', z => {
      this.disconnect(this.number)
    })
  }

  /**
   * Determines the event that needs to be sent
   * @param {String} event - name of event
   * @param {Object} data - data to be sent
   */
  sendEvent(event, data) {
    let eventData = {
      event: event,
      data: {}
    }

    switch (event) {
      case 'connectAI':
      case 'removeAI':
        this.eventData.data.position = data
        break
      case 'updateState':
        this.eventData.data = {
          board: data.board,
          players: data.players.map(p => {
            return p.sendableData()
          }),
          yellow: data.yellow,
          green: data.green
        }
        break
    }

    this.socket.send(JSON.stringify(eventData))
  }

  addAI(position) {
    this.sendEvent('connectAI', position)
  }

  removeAI(position) {
    this.sendEvent('removeAI', position)
  }

  updateState(data) {
    this.sendEvent('updateState', data)
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FrontendManager
