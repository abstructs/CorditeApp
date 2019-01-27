import * as mongoose from "mongoose";
import { LocationModel, LocationSchema } from "./Location";
import { UserModel } from "./User";

export type ReportModel = mongoose.Document & {
    user: UserModel | Number,
    location: LocationModel,
    type: ReportType
};

enum ReportType {
    trailClosed = "trailClosed",
    photo = "photo",
    construction = "construction",
    coolPlace = "coolPlace",
    beCareful = "beCareful",
    waterFountain = "waterFountain"
}

const reportSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    location: {
        type: LocationSchema,
        required: true
    },
    type: { 
        type: String,
        enum: [ReportType.trailClosed, ReportType.photo, ReportType.construction,
            ReportType.coolPlace, ReportType.beCareful, ReportType.waterFountain],
        required: true
    }
}, {
    timestamps: true
});

const Report = mongoose.model("Report", reportSchema);

export default Report;