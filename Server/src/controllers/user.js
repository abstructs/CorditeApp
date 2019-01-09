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
    var promise = User_1["default"].create(user);
    promise
        .then(function () {
        console.log("gucci");
        res.status(200).end();
    })["catch"](function (err) {
        console.error(err);
        res.status(500).end();
    });
};
