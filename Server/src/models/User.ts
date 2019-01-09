import * as mongoose from "mongoose";

export type UserModel = mongoose.Document & {
    email: string,
    password: string
}