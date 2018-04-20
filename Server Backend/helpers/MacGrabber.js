'use strict'

const arp = require('arpjs')

function mac(ip, callback) {
  arp.table((error, table) => {
    if (!error) {
      extractMac(table, getIP(ip), callback)
    }
  })
}

function extractMac(table, ip, callback) {
  for (let t of table) {
    if (t.ip === ip) {
      callback(t.mac)
      return
    }
  }
  let res = 1
  callback(res)
}

function getIP(ip) {
  return ip.split(':')[3]
}

module.exports = mac
