import * as mongoose from "mongoose";
import { Timestamp, Double } from "bson";

export type LocationModel = mongoose.Document & {
    mAltitude: Number,
    mBearing: Number,
    mBearingAccuracyDegrees: Number,
    mElapsedRealtimeNanos: Timestamp,
    mFieldsMask: Number,
    mHorizontalAccuracyMeters: Number,
    mLatitude: Double,
    mLongitude: Double,
    mProvider: String,
    mSpeed: Number,
    mSpeedAccuracyMetersPerSecond: Number,
    mTime: Timestamp,
    mVerticalAccuracyMeters: Number,
};

// export type LatLng = { latitude: number, longitude: number };

// const locationSchema = new mongoose.Schema({

// });

// const Location = mongoose.model("Location", locationSchema);

// Location.createIndexes();

// export default Location;