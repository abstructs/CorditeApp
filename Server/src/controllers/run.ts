import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';

import Run, { RunModel } from '../models/Run';

export const getRuns = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if(!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    Run.find({ user: user_id }, (err, runs) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        console.log(runs);
        //recives null 
        res.status(200).json(runs).end();
    });
};

export const saveRun = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if(!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    const run: RunModel = req.body

    run.user = user_id;

    Run.create(run, (err, run) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        //somehow our run is saved here as a null
        res.status(200).json(run).end();
    });
};
