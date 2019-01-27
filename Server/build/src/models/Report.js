"use strict";
exports.__esModule = true;
var mongoose = require("mongoose");
var Location_1 = require("./Location");
var ReportType;
(function (ReportType) {
    ReportType["trailClosed"] = "trailClosed";
    ReportType["photo"] = "photo";
    ReportType["construction"] = "construction";
    ReportType["coolPlace"] = "coolPlace";
    ReportType["beCareful"] = "beCareful";
    ReportType["waterFountain"] = "waterFountain";
})(ReportType || (ReportType = {}));
var reportSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    location: {
        type: Location_1.LocationSchema,
        required: true
    },
    type: {
        type: String,
        "enum": [ReportType.trailClosed, ReportType.photo, ReportType.construction,
            ReportType.coolPlace, ReportType.beCareful, ReportType.waterFountain],
        required: true
    }
}, {
    timestamps: true
});
var Report = mongoose.model("Report", reportSchema);
exports["default"] = Report;
