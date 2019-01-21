import * as mongoose from "mongoose";
import { Timestamp, Double } from "bson";
import { LocationModel } from "./Location";
import { UserModel } from "./User";

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

const Run = mongoose.model("Run", runSchema);

Run.createIndexes();

export default Run;