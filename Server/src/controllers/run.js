"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
var Run_1 = require("../models/Run");
exports.getRuns = function (req, res) {
    var token = req.get("Authorization");
    if (!token) {
        res.status(401).end();
        return;
    }
    var decoded = jwt.decode(token);
    var user_id = decoded["id"];
    Run_1["default"].find({ user: user_id }, function (err, runs) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        console.log(runs);
        res.status(200).json({ runs: runs }).end();
    });
};
exports.saveRun = function (req, res) {
    var token = req.get("Authorization");
    if (!token) {
        res.status(401).end();
        return;
    }
    var decoded = jwt.decode(token);
    var user_id = decoded["id"];
    var run = req.body;
    run.user = user_id;
    Run_1["default"].create(run, function (err, run) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        res.status(200).end();
    });
};
