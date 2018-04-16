'use strict'

/**
 * Imports arp module
 */
const arp = require('node-arp')

/**
 * Gets the mac address of the client
 * @param {String} ip - ip address
 */
module.exports = (ip) => {
  arp.getMAC(ip, (error, mac) => {
    if (!error) {
      return mac
    }
  })
}
