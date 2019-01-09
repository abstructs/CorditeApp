"use strict";
exports.__esModule = true;
var userController = require("./controllers/user");
var express = require("express");
var routes = express();
routes.get('/users', userController.getUsers);
exports["default"] = routes;
