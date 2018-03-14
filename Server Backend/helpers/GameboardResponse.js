
/**
   * This method handles all types of responses that the gameboard can send back to a user.
   * @param {String} word - word to be piped into the error message
   */
function handleResponse(error, word, res) {
  const result = {
    invalid: true
  }
  let reason = null

  switch (error) {
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
    default:
      result.invalid = false
  }
  if (result.invalid) {
    result['reason'] = reason.toUpperCase()
    result['word'] = word.toUpperCase()
  }

  return res.json(result)
}

module.exports = {
  hr: handleResponse
}
