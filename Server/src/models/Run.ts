import * as mongoose from "mongoose";
import { Timestamp } from "bson";
import { LocationModel } from "./Location";
import { UserModel } from "./User";

const getDistance = (location1: LocationModel, location2: LocationModel): Number => {
    const radius = 6371.009;
    const radians = (Math.PI / 180);

    const point1 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location1.mLongitude.valueOf() * radians
    }

    const point2 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location2.mLongitude.valueOf() * radians
    }

    const deltaLat = (point2.latitude - point1.latitude);
    const deltaLong = (point2.longitude - point1.longitude);
    const deltaMean = (point1.latitude + point2.latitude) / 2;
    const square = (Math.pow((deltaLat), 2)) + (Math.pow(Math.cos(deltaMean) * deltaLong, 2));
   
    let distanceOfPoints = Number((radius * Math.sqrt(square)).toFixed(3));

    return distanceOfPoints;
}

const calculateRunStats = (locations: Array<LocationModel>): { averageSpeed: Number, totalDistance: Number, timeElapsed: Number } => {
    if (locations.length == 0) return;

    let prevLocation: LocationModel = locations[0];
    let totalDistance: number = 0;

    let totalSpeed: number = prevLocation.mSpeed.valueOf();

    for (let i = 1; i < locations.length; i++) {
        const location = locations[i];

        totalDistance += getDistance(prevLocation, location).valueOf();
        totalSpeed += location.mSpeed.valueOf();

        prevLocation = locations[i];
    }

    let averageSpeed = Number((totalSpeed / locations.length).toFixed(1));

    const timeElapsed: number = (locations[locations.length - 1]["mTime"].valueOf()) - (locations[0]["mTime"].valueOf());

    return { averageSpeed, totalDistance, timeElapsed };
}

export type RunModel = mongoose.Document & {
    locations: Array<LocationModel>,
    averageSpeed: Number,
    timeElapsed: Number,
    distanceTravelled: Number,
    timestamp: Timestamp,
    rating: Number,
    user: UserModel | Number
};

const runSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    locations: {
        type: Array<LocationModel>(),
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

runSchema.pre<RunModel>("save", function (next) {
    let runnerStats = calculateRunStats(this.locations);

    this.averageSpeed = runnerStats.averageSpeed;
    this.distanceTravelled = runnerStats.totalDistance;
    this.timeElapsed = runnerStats.timeElapsed;
    
    next();
});

const Run = mongoose.model("Run", runSchema);

Run.createIndexes();
export default Run;