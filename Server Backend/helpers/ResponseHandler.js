'use strict'
/**
 * Handles reponses going to players
 * @param {Number} er - error number
 * @param {Object} play - play object
 * @param {Object} player - player oject
 * @param {Object} io - socket object
 */
module.exports = (data, player, sm) => {
  const result = {
    invalid: true
  }
  let reason = null

  switch (data.error) {
    case 1:
      reason = 'Not a valid word'
      break
    case 2:
      reason = 'Placed out of the bounds of the board'
      break
    case 3:
      reason = 'Invalid placement'
      break
    case 4:
      reason = 'Word was not played over the center tile'
      break
    case 5:
      reason = 'Word not connected to played tiles'
      break
    case 6:
      reason = 'Word is a bad word'
      break
    case 7:
      reason = 'This player has cheated'
      break
    default:
      result.invalid = false
  }
  if (result.invalid) {
    result['reason'] = reason.toUpperCase()
    result['data'] = data.data
    player.sendEvent('play', result)
    return false
  }
  let score = sm.calculateScore(player, data.data)

  console.log('DEBUG: SENDING OUT WORD PLAYED EVENT'.debug)
  sm._io.emit('boardUpdate', {
    board: sm._gameBoard.sendableBoard()
  })
  console.log('DEBUG: SENDING OUT GAME EVENT EVENT'.debug)
  let action = `${player.name} played ${data.data.map(w => w.word)} for ${score} points`
  console.log(`INFO: ${action}`.toUpperCase().info)
  sm._io.emit('gameEvent', {
    action: action
  })
  return true
}
