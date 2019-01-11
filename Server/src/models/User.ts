import * as mongoose from "mongoose";
import * as bcrypt from "bcrypt";

const saltRounds = 10;

export type UserModel = mongoose.Document & {
    email: string,
    password: string
};

const userSchema = new mongoose.Schema({
    email: { 
        type: String,
        required: true,
        index: true,
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

userSchema.pre<UserModel>("save", function(next) {
    bcrypt.hash(this.password, saltRounds, (err, hash) => {
        if(err) throw err;

        this.password = hash;
        next();
    }); 
});

const User = mongoose.model("User", userSchema);

User.createIndexes();

export default User;