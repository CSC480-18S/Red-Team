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
 * Route that renders an HTML page for adding a user
 */
router.get("/addUserPage", function(req, res, next){

    /**
     * HTML page to be rendered
     */
    res.render("addUser");
});

/**
 * Route that renders an HTML page for viewing all users
 */
router.get("/usersPage", function(req, res, next){

    /**
     * HTML page to be rendered
     */
    res.render("allUsers");
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
 * Route that creates a user
 */
router.post("/createUser", function (req, res, next) {
    /**
     * Create new user object
     */
    const newUser = {
        userName: req.body.userName,
        timeCreated: new Date()
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
     * Logs to the console what user was added
     */
    console.log("User: "+ user.userName + " deleted.");

    /**
     * Sends back a status code of whether or not a user was successfully deleted or not
     */
    res.sendStatus(users[index] !== user ? 200 : 400);
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;
