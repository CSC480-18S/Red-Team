/**
 * Imports the `express` and `router` modules
 */
const express = require("express");
const router = express.Router();

/**
 * Users array
 */
var users = [];

/**
 * Route that creates a user
 */
router.post("/createUser", function (req, res, next) {
    /**
     * TODO generate shortID and JWT
     */

    /**
     * Create new user object
     */
     const newUser = {
        userName: req.body.userName
    }

    /** 
     * Add new user object to users array
     */
    users.push(newUser);
    
    /**
     * Logs to the console what user was added
     */
    console.log("User: " + newUser.userName + " added.")

    /**
     * Returns to the user a JSON object containing the new user
     */
    res.json(newUser);
});

router.put("/updateScore", function (req, res, next) {
    /**
     * The word score to be updated
     */
    const wscore = req.body.user;

    /**
     * The player score to be updated
     */
    const wscore = req.body.user;
    
    /**
     * TODO update scores on the database
     */
    

    /**
     * TODO Returns a success/fail boolean
     */
    
});

router.get("/getUser", function (req, res, next) {
    /**
     * The JWT of the user to be retreived
     */
    const userToken = req.body.user;

    /**
     * TODO look up the user on the databse
     */

    
    /**
     * TODO returns a JSON containing user information
     */
    res.json();
});

router.get("/currentTeam", function (req, res, next) {
    /**
     * The JWT of the user to be retreived
     */
    const userToken = req.body.user;

    /**
     * TODO look up the user on the databse
     */

    
    /**
     * TODO returns a String for the user's team
     */
    
});

router.put("/setTeam", function (req, res, next) {
    /**
     * The JWT of the user to be retreived
     */
    const userToken = req.body.user;

    /**
     * The team the user will be assigned to
     */
    const team = req.body.user;
    
    /**
     * TODO update user's team on the database
     */
    

    /**
     * TODO Returns a success/fail boolean
     */
    
});

router.put("/addUser", function (req, res, next) {
    /**
     * The JWT of the user to be added
     */
    const userToken = req.body.user;
    
    /**
     * TODO update the databse to reflect the user that joined
     */
    

    /**
     * TODO Returns a success/fail boolean
     */
    
});

/**
 * Route that deletes a user
 */
router.delete("/deleteUser", function(req, res, next){
    /**
     * The index of the user to be deleted
     */
    const index = req.body.user;
    /**
     * The user to be deleted
     */
    const user = users[index];

    /**
     * Removes the user from the list
     */
    users.splice(index, 1);

    /**
     * Logs to the console what user was deleted
     */
    console.log("User: "+ user.userName + " deleted.");

    /**
     * Sends back a status code of whether or not a user was successfully deleted or not
     */
    res.sendStatus(users[index] !== user ? 200 : 400);
});

/**
 * Route that returns all of the users
 */
router.get("/allUsers", function (req, res, next) {

    /**
     * Sends the user a JSON object containing all of the users
     */
	res.json({
		all_users: users
	});
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;