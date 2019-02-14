"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
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
    console.log(req.body);
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
