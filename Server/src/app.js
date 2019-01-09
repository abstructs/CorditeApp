"use strict";
exports.__esModule = true;
var express_1 = require("express");
// console.log("Hello");
var PORT = 3000;
var app = express_1["default"]();
app.get("/", function (req, res) {
    res.status(200).send("hello world").end();
});
app.listen(PORT, function () { return console.log("Server is running on port=" + PORT); });
