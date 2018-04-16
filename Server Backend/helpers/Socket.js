var socketio = require('socket.io')
function socket(server) {
  var io = socketio(server)
  return io
}

module.exports = socket
