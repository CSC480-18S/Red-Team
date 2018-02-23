/**
 * Imports the `express` and `router` modules.
 */
const express = require('express')
const router = express.Router()

/**
 * This endpoint gets the highest valued word played by the repsective team.
 */
router.get('/team/topValueWord', function(req, res, next) {

})

/**
 * This endpoint gets the score value of the highest valued word played by the respective team.
 */
router.get('/team/highestValue', function(req, res, next) {

})

/**
 * This endpoint gets the longest word played by the respective team.
 */
router.get('/team/longestWord', function(req, res, next) {

})

/**
 * This endpoint gets the highest single game score for the respective team.
 */
router.get('/team/highestSingleGameScore', function(req, res, next) {

})

/**
 * This endpoint returns the most frequently played word by the respective team.
 */
router.get('/team/freqPlayedWord', function(req, res, next) {

})

/**
 * This endpoint gets the number of bonuses cumulatively played by the respective team.
 */
router.get('/team/amountBonusesUsed', function(req, res, next) {

})

/**
 * This endpoint gets the cumulative game score for the respective team.
 */
router.get('/team/cumulativeScore', function(req, res, next) {

})

/**
 * This endpoint gets the total number of wins for the respective team.
 */
router.get('/team/winCount', function(req, res, next) {

})

/**
 * This endpoint gets the highest valued word played by the respective player.
 */
router.get('/player/topValueWord', function(req, res, next) {

})

/**
 * This endpoint gets the score value of the highest valued word played by the respective player.
 */
router.get('/player/highestValue', function(req, res, next) {

})

/**
 * This endpoint gets the longest word played by the respective player.
 */
router.get('/player/longestWord', function(req, res, next) {

})

/**
 * This endpoint gets the highest single game score for the respective player.
 */
router.get('/player/highestSingleGameScore', function(req, res, next) {

})

/**
 * This endpoint returns the most frequently played word by the respective player.
 */
router.get('/player/freqPlayedWord', function(req, res, next) {

})

/**
 * This endpoint gets the number of bonuses cumulatively played by the respective player.
 */
router.get('/player/amountBonusesUsed', function(req, res, next) {

})

/**
 * This endpoint gets the cumulative score for the respective player.
 */
router.get('/player/totalScore', function(req, res, next) {

})

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router
