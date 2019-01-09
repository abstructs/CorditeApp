import * as mongoose from "mongoose";

export type UserModel = mongoose.Document & {
    email: string,
    password: string
};

const userSchema = new mongoose.Schema({
    email: { 
        type: String,
        required: true,
        unique: true,
        minlength: 1,
        maxlength: 50
    },
    password: {
        type: String,
        required: true,
        minlength: 6,
        maxlength: 30
    }
});

const User = mongoose.model("User", userSchema);

export default User;