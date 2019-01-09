import { Request, Response } from 'express';

import User from '../models/User';

export let getUsers = (req: Request, res: Response) => {
    res.status(200).send("Hello world").end();
};

export let signup = (req: Request, res: Response) => {
    const user = req.body['user'];
    
    if(user == undefined) {
        res.status(400).end();
        return;
    }

    const promise: Promise<any> = User.create(user);

    promise
        .then(() => {
            res.status(200).end();
        })
        .catch((err) => {
            res.status(500).end();
        });
};