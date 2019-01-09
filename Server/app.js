"use strict";
exports.__esModule = true;
var express = require("express");
var routes_1 = require("./src/routes");
var PORT = 3000;
var app = express();
app.use(routes_1["default"]);
app.listen(PORT, function () { return console.log("Server is running on port=" + PORT); });
