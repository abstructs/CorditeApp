"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
var Run_1 = require("../models/Run");
var Report_1 = require("../models/Report");
exports.saveReport = function (req, res) {
    var token = req.get("Authorization");
    if (!token) {
        res.status(401).end();
        return;
    }
    var decoded = jwt.decode(token);
    var user_id = decoded["id"];
    var report = req.body;
    report.user = user_id;
    Report_1["default"].create(report, function (err, report) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        res.status(200).json({ report: report }).end();
    });
};
exports.getReports = function (req, res) {
    var location = req.body;
    var token = req.get("Authorization");
    if (!token) {
        res.status(401).end();
        return;
    }
    Report_1["default"].find({}, function (err, reports) {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        res.status(200).json(reports).end();
    });
};
exports.getReportDistance = function (req, res) {
    var location1 = req.body[0];
    var location2 = req.body[1];
    var token = req.get("Authorization");
    if (!token) {
        res.status(401).end();
        return;
    }
    ;
    console.log(res.status(200).json({ distance: Run_1.getDistance(location1, location2) }).end());
};
