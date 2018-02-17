/**
 * Imports the `express` and `router` modules.
 */
const express = require("express");
const router = express.Router();

/**
 * This endpoint gets the collective score for green team.
 */
router.get("/gameScore", function(req, res, next) {
    
});

/**
 * This endpoint gets the <x amount> most valuable words ever played.
 */
router.get("/bestWords", function(req, res, next) {

});

/**
 * This endpoint provides a list of the top <x amount> users.   
 */
router.get("/topPlayers", function(req, res, next) {

});

/**
 * This endpoint provides the game score of the game containing the highest game score.
 */
router.get("/highestGameScore", function(req, res, next) {
    
});

/**
 * This endpoint provides the cumulative team scores.
 */
router.get("/cumulativeTeamScores", function(req, res, next) {
    
});

/**
 * This endpoint provides a list of the <x amount> most-played words.
 */
router.get("/mostPlayedWords", function(req, res, next) {
    
});

/**
 * This endpoint provides the total number of times each team has won.
 */
router.get("/totalTeamWins", function(req, res, next) {
    
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;