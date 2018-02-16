/**
 * Imports the `express` and `router` modules
 */
const express = require("express");
const router = express.Router();

/**
 * This endpoint gets the collective score for green team
 */
router.get("/greenScore", function(req, res, next){
    
});

/**
 * This endpoint gets the collective score for gold team
 */
router.get("/goldScore", function(req, res, next){

});

/**
 * This endpoint gets the largest word currently in play
 */
router.get("/largeWords", function(req, res, next){

});

/**
 * This endpoint provides a list of top users
 */
router.get("/topPlayers", function(req, res, next){
    
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;