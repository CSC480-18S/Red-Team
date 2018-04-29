'use strict'
const ws = require('ws')
module.exports = (server) => {
  return new ws.Server({
    server: server,
    autoAcceptConnections: true
  })
}
