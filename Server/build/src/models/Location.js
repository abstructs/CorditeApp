"use strict";
exports.__esModule = true;
var mongoose = require("mongoose");
exports.LocationSchema = new mongoose.Schema({
    mAltitude: {
        type: Number
    },
    mBearing: {
        type: Number
    },
    mBearingAccuracyDegrees: {
        type: Number
    },
    mElapsedRealtimeNanos: {
        type: Number
    },
    mFieldsMask: {
        type: Number
    },
    mHorizontalAccuracyMeters: {
        type: Number
    },
    mLatitude: {
        type: Number,
        required: true
    },
    mLongitude: {
        type: Number,
        required: true
    },
    mProvider: {
        type: String,
        required: true
    },
    mSpeed: {
        type: Number
    },
    mSpeedAccuracyMetersPerSecond: {
        type: Number
    },
    mTime: {
        type: Number
    },
    mVerticalAccuracyMeters: {
        type: Number
    }
});
