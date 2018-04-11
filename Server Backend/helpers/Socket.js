'use strict'
const socketio = require('socket.io')
module.exports = (server) => { return socketio(server) }
