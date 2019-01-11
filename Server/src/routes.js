"use strict";
exports.__esModule = true;
var userController = require("./controllers/user");
var express = require("express");
var bodyParser = require("body-parser");
var routes = express.Router();
routes.use(bodyParser.urlencoded({ extended: true }));
routes.use(bodyParser.json());
routes.get('/users', userController.getUsers);
routes.post('/users/emailtaken', userController.emailTaken);
routes.post('/users/signup', userController.signup);
exports["default"] = routes;
