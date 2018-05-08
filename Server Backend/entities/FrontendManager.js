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
        eventData.data.position = data
        break
      case 'updateState':
        eventData.data = {
          board: data.board,
          players: data.players.map(p => {
            if (p !== null) {
              return p.sendableData()
            }

            return null
          }),
          gold: data.gold,
          green: data.green
        }
        break
      case 'gameOver':
        eventData.data = data
        break
      case 'gameEvent':
        eventData.data = {
          action: data,
          bonus: false
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

  gameOver(data) {
    this.sendEvent('gameOver', data)
  }

  gameEvent(data) {
    this.sendEvent('gameEvent', data)
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = FrontendManager
