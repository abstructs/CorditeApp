"use strict";
exports.__esModule = true;
var mongoose = require("mongoose");
var bcrypt = require("bcrypt");
var saltRounds = 10;
var userSchema = new mongoose.Schema({
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
    },
    firstName: {
        type: String,
        required: true,
        minlength: 1,
        maxlength: 50
    },
    lastName: {
        type: String,
        required: true,
        minlength: 1,
        maxlength: 50
    },
    runs: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Run' }]
}, {
    timestamps: true
});
userSchema.pre("save", function (next) {
    var _this = this;
    bcrypt.hash(this.password, saltRounds, function (err, hash) {
        if (err)
            throw err;
        _this.password = hash;
        next();
    });
});
var User = mongoose.model("User", userSchema);
User.createIndexes();
exports["default"] = User;
