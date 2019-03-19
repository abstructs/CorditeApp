import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';
import Run, { RunModel } from '../models/Run';

enum TimeFrame {
    WEEK = "WEEK",
    MONTH = "MONTH",
    ALL = "ALL"
}

export const getRuns = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if (!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    Run.find({ user: user_id }, (err, runs) => {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        res.status(200).json(runs).end();

    });
};

export const graphRuns = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if (!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    const timeFrame: TimeFrame = req.body["timeFrame"];

    if (!timeFrame) {
        res.status(400).end();
        return;
    }

    const currentDate = new Date();

    switch (timeFrame) {
        case TimeFrame.WEEK:
            currentDate.setDate(currentDate.getDate() - 7);
            break;
        case TimeFrame.MONTH:
            currentDate.setDate(currentDate.getDate() - 30);
            break;
        case TimeFrame.ALL:
            // 10 years
            currentDate.setDate(currentDate.getDate() - 3650); 
            break;
        default:
            res.status(400).end();
            return;
    }

    const query = { user: user_id, createdAt: { $gte: currentDate } };

    Run.find(query, (err, runs) => {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        res.status(200).json(runs).end();
    });
};

export const saveRun = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if (!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    const run: RunModel = req.body

    run.user = user_id;

    Run.create(run, (err, run) => {
        if (err) {
            console.error(err);
            res.status(500).end();
            return;
        }
        res.status(200).json(run).end();
    });
};
