"use strict";
exports.__esModule = true;
var mongoose = require("mongoose");
var runSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    locations: {
        type: Array(),
        required: true
    },
    averageSpeed: {
        type: Number
    },
    timeElapsed: {
        type: Number
    },
    distanceTravelled: {
        type: Number
    },
    rating: {
        type: Number
    }
}, {
    timestamps: true
});
var Run = mongoose.model("Run", runSchema);
Run.createIndexes();
exports["default"] = Run;
