'use strict'

const arp = require('node-arp')

function mac(ip, callback) {
  ip = getIP(ip)
  arp.getMAC(ip, function(err, mac) {
    if (! err) {
      callback(mac)
    } else {
      callback(1)
    }
  })
}

function getIP(ip) {
  return ip.split(':')[2]
}

module.exports = mac
