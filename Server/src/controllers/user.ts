import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';
import * as bcrypt from 'bcrypt';

import User, { UserModel } from '../models/User';

const tokenSecret: jwt.Secret = "fake_secret";

const tokenOptions: jwt.SignOptions = {
    expiresIn: "7 days"
};

export const getUsers = (req: Request, res: Response) => {
    res.status(500).send("not implemented").end();
};

export const signup = (req: Request, res: Response) => {
    console.debug(req.body);

    const { email, password, firstName, lastName } = req.body;

    const userParams = { email, password, firstName, lastName };
    
    User.create(userParams, (err, user: UserModel) => {
        if(err) {
            console.debug(err);
            res.status(400).end();
            return;
        }

        jwt.sign({ id: user._id }, tokenSecret, tokenOptions, (err, token) => {
            if(err) throw err;

            res.status(200).json({ token }).end();
        });
    });
};

export const login = (req: Request, res: Response) => {
    const { email, password } = req.body;

    User.findOne({ email }, (err, user: UserModel) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        
        if(user == null) {
            res.status(404).end();
            return;
        }

        bcrypt.compare(password, user.password, (err, isValid) => {
            if(isValid) {
                jwt.sign({ id: user._id }, tokenSecret, tokenOptions, (err, token) => {
                    if(err) throw err;
        
                    res.status(200).json({ token }).end();
                });
            } else {
                res.status(401).end();
            }
        });
    });
};

export const emailTaken = (req: Request, res: Response) => {
    console.debug(req.body);

    const user = req.body;

    User.findOne({ email: user.email }, (err, response) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        if(response == null) {
            res.status(200).json({ emailTaken: false });
        } else {
            res.status(200).json({ emailTaken: true });
        }

        res.end();
    });
};