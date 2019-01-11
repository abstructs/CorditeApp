import { Request, Response } from 'express';

import User, { UserModel } from '../models/User';

export const getUsers = (req: Request, res: Response) => {
    res.status(500).send("not implemented").end();
};

export const signup = (req: Request, res: Response) => {
    const user: UserModel = req.body['user'];
    
    if(user == undefined) {
        res.status(400).end();
        return;
    }
    
    User.create(user, (err, response) => {
        if(err) {
            console.error(err);
            res.status(400).end();
            return;
        }

        res.status(200).end();
    });
};

export const emailTaken = (req: Request, res: Response) => {
    const user = req.body['user'];

    if(user == undefined) {
        res.status(400).end();
        return;
    }

    User.findOne({ email: user['email'] }, (err, response) => {
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