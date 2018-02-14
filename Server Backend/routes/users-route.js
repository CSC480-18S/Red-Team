/**
 * Imports the `express` and `router` modules
 */
const express = require("express");
const router = express.Router();

/**
 * Users array
 */
var users = [];

router.get("/allusers", function (req, res, next) {
	res.json({
		all_users: users
	});
});
/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = router;