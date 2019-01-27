import * as mongoose from "mongoose";

export type LocationModel = mongoose.Document & {
    mAltitude: Number,
    mBearing: Number,
    mBearingAccuracyDegrees: Number,
    mElapsedRealtimeNanos: Number,
    mFieldsMask: Number,
    mHorizontalAccuracyMeters: Number,
    mLatitude: Number,
    mLongitude: Number,
    mProvider: String,
    mSpeed: Number,
    mSpeedAccuracyMetersPerSecond: Number,
    mTime: Number,
    mVerticalAccuracyMeters: Number,
};

export const LocationSchema = new mongoose.Schema({
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
    },
});