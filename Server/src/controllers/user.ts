import { Request, Response } from 'express';
import { UserModel } from '../models/User';

export let getUsers = (req: Request, res: Response) => {
    res.status(200).send("Hello world").end();
};

export let signup = (req: Request, res: Response) => {

    res.status(200).send("signup").end();
};