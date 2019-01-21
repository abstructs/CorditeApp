"use strict";
exports.__esModule = true;
var userController = require("./controllers/user");
var runController = require("./controllers/run");
var express = require("express");
var bodyParser = require("body-parser");
var morgan = require("morgan");
var jwt = require("jsonwebtoken");
var routes = express.Router();
var tokenSecret = "fake_secret";
var authenticateUser = function (req, res, next) {
    var token = req.get('Authorization');
    if (!token) {
        res.status(400).send({ errors: { auth: "No credentials sent." } }).end();
        return;
    }
    try {
        var decoded = jwt.verify(token, tokenSecret);
        if (decoded) {
            next();
        }
    }
    catch (err) {
        res.status(401).send({ errors: { auth: "Invalid credential." } }).end();
        return;
    }
};
routes.use(morgan('tiny'));
routes.use(bodyParser.urlencoded({ extended: true }));
routes.use(bodyParser.json());
routes.get('/users', userController.getUsers);
routes.post('/users/emailtaken', userController.emailTaken);
routes.post('/users/signup', userController.signup);
routes.post('/users/login', userController.login);
routes.post('/runs', authenticateUser, runController.saveRun);
routes.post('/runs/myRuns', authenticateUser, runController.getRuns);
exports["default"] = routes;
