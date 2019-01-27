"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
var bcrypt = require("bcrypt");
var User_1 = require("../models/User");
var tokenSecret = "fake_secret";
var tokenOptions = {
    expiresIn: "7 days"
};
exports.getUsers = function (req, res) {
    res.status(500).send("not implemented").end();
};
exports.signup = function (req, res) {
    console.debug(req.body);
    var _a = req.body, email = _a.email, password = _a.password, firstName = _a.firstName, lastName = _a.lastName;
    var userParams = { email: email, password: password, firstName: firstName, lastName: lastName };
    User_1["default"].create(userParams, function (err, user) {
        if (err) {
            console.debug(err);
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
    var _a = req.body, email = _a.email, password = _a.password;
    User_1["default"].findOne({ email: email }, function (err, user) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        if (user == null) {
            res.status(404).end();
            return;
        }
        bcrypt.compare(password, user.password, function (err, isValid) {
            if (isValid) {
                jwt.sign({ id: user._id }, tokenSecret, tokenOptions, function (err, token) {
                    if (err)
                        throw err;
                    res.status(200).json({ token: token }).end();
                });
            }
            else {
                res.status(401).end();
            }
        });
    });
};
exports.emailTaken = function (req, res) {
    console.debug(req.body);
    var user = req.body;
    User_1["default"].findOne({ email: user.email }, function (err, response) {
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
