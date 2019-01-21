import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';

import Run, { RunModel } from '../models/Run';

const tokenSecret: jwt.Secret = "fake_secret";

const tokenOptions: jwt.SignOptions = {
    expiresIn: "7 days"
};

export const saveRun = (req: Request, res: Response) => {
    console.debug(req.headers);

    const token = req.get("Authorization");

    if(!token) {
        res.status(400).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];
    
    console.log(decoded);

    const run: RunModel = req.body;

    


    res.status(200).json({ success: true }).end();
};
