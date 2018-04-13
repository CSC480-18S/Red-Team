'use strict'
// letter distribution, alphabetically
const letterDist = [9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1]
const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
let totalLetters = 0
let intervals = [];

(() => {
  // set up intervals
  // push first interval
  intervals.push(letterDist[0])
  totalLetters += letterDist[0]
  // add the rest of the intervals
  for (let i = 1; i < letterDist.length; ++i) {
    intervals.push(intervals[i - 1] + letterDist[i])
    totalLetters += letterDist[i]
  }

  return intervals
})()

module.exports = {
  letters,
  totalLetters,
  intervals
}
