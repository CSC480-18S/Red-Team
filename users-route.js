/**
 * Imports the `express` and `router` modules
 */
const express = require("express");
const router = express.Router();

/**
 * Users array
 */
var users = [];

router.post("/createUser", function (req, res, next) {
    // Create new user object
    const newUser = {
        "userName": req.body.userName,
        "timeCreated": new Date()
    }

    // Add new user object to users array
	users.push(newUser);
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;