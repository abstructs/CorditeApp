"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
var User_1 = require("../models/User");
var tokenSecret = "fake_secret";
var tokenOptions = {
    expiresIn: "7 days"
};
exports.getUsers = function (req, res) {
    res.status(500).send("not implemented").end();
};
exports.signup = function (req, res) {
    var user = req.body['user'];
    if (user == undefined) {
        res.status(400).end();
        return;
    }
    User_1["default"].create(user, function (err, user) {
        if (err) {
            console.error(err);
            res.status(400).end();
            return;
        }
        jwt.sign({ id: user._id }, tokenSecret, tokenOptions, function (err, token) {
            if (err)
                throw err;
            res.status(200).json({ token: token }).end();
        });
    });
};
exports.login = function (req, res) {
    var user = req.body['user'];
    if (user == undefined) {
        res.status(400).end();
        return;
    }
    User_1["default"].findOne({ email: user.email }, function (err, user) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        if (user == null) {
            res.status(400);
            return;
        }
        jwt.sign({ id: user._id }, tokenSecret, tokenOptions, function (err, token) {
            if (err)
                throw err;
            res.status(200).json({ token: token }).end();
        });
    });
};
exports.emailTaken = function (req, res) {
    var user = req.body['user'];
    if (user == undefined) {
        res.status(400).end();
        return;
    }
    User_1["default"].findOne({ email: user.email }, function (err, response) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        if (response == null) {
            res.status(400).json({ emailTaken: false });
        }
        else {
            res.status(200).json({ emailTaken: true });
        }
        res.end();
    });
};
