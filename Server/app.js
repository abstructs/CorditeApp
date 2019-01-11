"use strict";
exports.__esModule = true;
var express = require("express");
var mongoose = require("mongoose");
var routes_1 = require("./src/routes");
var mongoUrl = "mongodb://localhost:27017/cordite";
mongoose.set('useCreateIndex', true);
mongoose.connect(mongoUrl, { useNewUrlParser: true }, function (err) {
    if (err) {
        console.error("Having problems connecting to mongo.");
        process.exit();
    }
});
var PORT = 3000;
var app = express();
app.use(routes_1["default"]);
app.set("port", PORT);
app.listen(app.get("port"), function () { return console.log("Server is running on port=" + PORT); });
exports["default"] = app;
