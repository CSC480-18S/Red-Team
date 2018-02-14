/**
 * Imports the `express` and `router` modules
 */
const express = require("express");
const router = express.Router();

/**
 * Users array
 */
var users = [];


router.get("/allUsers", function (req, res, next) {
	res.json({
		all_users: users
	});
});

router.get("/createUser", function (req, res, next) {
    const newUser = {
        "userName": req.body.userName,
        "timeCreated": new Date()
    }

	users.push(newUser);
});

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;