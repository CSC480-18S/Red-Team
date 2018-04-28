'use strict'

/**
 * Imports files
 */
const dg = require('./Debug')
const db = require('./DB')

/**
 * Handles responses going out to users
 * @param {Object} data - data object
 * @param {Object} player - player object
 * @param {Objecr} gm - game manager object
 */
module.exports = (data, player, gm) => {
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
  if (data.error === 6) {
    db.updatePlayerDirty(player, data.data)
  }
  if (result.invalid) {
    result['reason'] = reason.toUpperCase()
    result['data'] = data.data
    player.sendEvent('play', result)
    player.sendEvent('gameEvent', reason)
    return false
  }
  let score = gm.calculateScore(player, data.data)

  dg('sending out word played event', 'debug')
  gm.boardUpdate()
  dg('sending out game event event', 'debug')
  let words = data.data.map(w => w.word)
  let action = `${player.name} played ${words} for ${score.totalScore} points`
  dg(action, 'info')
  // TODO: Need to flag whether or not this is a bonus play or not @Landon
  const search = words.map(s => s).join(',')
  db.dictionaryCheck(search).then(r => {
    let bonus = false
    for (let word of r) {
      if (word.special) {
        bonus = true
      }
    }
    gm.emitGameEvent(action, bonus)
    return true
  }).catch(e => {
    console.log(e)
  })
}
