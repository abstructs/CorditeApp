"use strict";
exports.__esModule = true;
var User_1 = require("../models/User");
exports.getUsers = function (req, res) {
    res.status(200).send("Hello world").end();
};
exports.signup = function (req, res) {
    var user = req.body['user'];
    if (user == undefined) {
        res.status(400).end();
        return;
    }
    User_1["default"].create(user, function (err, response) {
        if (err) {
            console.error(err);
            res.status(400).end();
            return;
        }
        res.status(200).end();
    });
};
exports.emailTaken = function (req, res) {
    var user = req.body['user'];
    if (user == undefined) {
        res.status(400).end();
        return;
    }
    User_1["default"].findOne({ email: user['email'] }, function (err, response) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        if (response == null) {
            res.status(200).json({ emailTaken: false });
        }
        else {
            res.status(200).json({ emailTaken: true });
        }
        res.end();
    });
};
