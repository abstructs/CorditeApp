import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';

import User, { UserModel } from '../models/User';

const tokenSecret: jwt.Secret = "fake_secret";

const tokenOptions: jwt.SignOptions = {
    expiresIn: "7 days"
};

export const getUsers = (req: Request, res: Response) => {
    res.status(500).send("not implemented").end();
};

export const signup = (req: Request, res: Response) => {
    const user: UserModel = req.body['user'];
    
    if(user == undefined) {
        res.status(400).end();
        return;
    }
    
    User.create(user, (err, user: UserModel) => {
        if(err) {
            console.error(err);
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
    const user: UserModel = req.body['user'];

    if(user == undefined) {
        res.status(400).end();
        return;
    }

    User.findOne({ email: user.email }, (err, user: UserModel) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        
        if(user == null) {
            res.status(400);
            return;
        }

        jwt.sign({ id: user._id }, tokenSecret, tokenOptions, (err, token) => {
            if(err) throw err;

            res.status(200).json({ token }).end();
        });
    })
};

export const emailTaken = (req: Request, res: Response) => {
    const user = req.body['user'];

    if(user == undefined) {
        res.status(400).end();
        return;
    }

    User.findOne({ email: user.email }, (err, response) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        if(response == null) {
            res.status(400).json({ emailTaken: false });
        } else {
            res.status(200).json({ emailTaken: true });
        }

        res.end();
    });
};