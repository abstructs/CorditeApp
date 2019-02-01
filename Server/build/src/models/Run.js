"use strict";
exports.__esModule = true;
var mongoose = require("mongoose");
var getDistance = function (location1, location2) {
    var radius = 6371.009;
    var radians = (Math.PI / 180);
    var point1 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location1.mLongitude.valueOf() * radians
    };
    var point2 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location2.mLongitude.valueOf() * radians
    };
    var deltaLat = (point2.latitude - point1.latitude);
    var deltaLong = (point2.longitude - point1.longitude);
    var deltaMean = (point1.latitude + point2.latitude) / 2;
    var square = (Math.pow((deltaLat), 2)) + (Math.pow(Math.cos(deltaMean) * deltaLong, 2));
    var distanceOfPoints = radius * Math.sqrt(square);
    var roundedDist = distanceOfPoints.toPrecision(1);
    distanceOfPoints = Number(roundedDist);
    return distanceOfPoints;
};
var calculateRunStats = function (locations) {
    if (locations.length == 0)
        return;
    var prevLocation = locations[0];
    var totalDistance = 0;
    var totalSpeed = prevLocation.mSpeed.valueOf();
    for (var i = 1; i < locations.length; i++) {
        var location_1 = locations[i];
        totalDistance += getDistance(prevLocation, location_1).valueOf();
        totalSpeed += location_1.mSpeed.valueOf();
        prevLocation = locations[i];
    }
    var averageSpeed = totalSpeed / locations.length;
    var roundedAverageSpeed = averageSpeed.toPrecision(1);
    averageSpeed = Number(roundedAverageSpeed);
    var timeElapsed = (locations[locations.length - 1]["mTime"].valueOf()) - (locations[0]["mTime"].valueOf());
    console.log("Distance: " + totalDistance);
    return { averageSpeed: averageSpeed, totalDistance: totalDistance, timeElapsed: timeElapsed };
};
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
runSchema.pre("save", function (next) {
    var runnerStats = calculateRunStats(this.locations);
    this.averageSpeed = runnerStats.averageSpeed;
    this.distanceTravelled = runnerStats.totalDistance;
    this.timeElapsed = runnerStats.timeElapsed;
    next();
});
var Run = mongoose.model("Run", runSchema);
Run.createIndexes();
exports["default"] = Run;
