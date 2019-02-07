import { Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';
import distance, {getDistance} from '../models/Run'


import Report, { ReportModel } from '../models/Report';
import { LocationModel } from '../models/Location';

export const saveReport = (req: Request, res: Response) => {
    const token = req.get("Authorization");

    if(!token) {
        res.status(401).end();
        return;
    }

    const decoded = jwt.decode(token);

    const user_id = decoded["id"];

    const report: ReportModel = req.body;

    report.user = user_id;

    Report.create(report, (err, report) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        res.status(200).json({ report }).end();
    });
};

export const getReports = (req: Request, res: Response) => {
    const location: LocationModel = req.body;

    const token = req.get("Authorization");
    
    if(!token) {
        res.status(401).end();
        return;
    }

    Report.find({}, (err, reports) => {
        if(err) {
            console.error(err);
            res.status(500).end();
            return;
        }

        res.status(200).json(reports).end();
    });
};

export const getReportDistance = (req: Request, res: Response) => {
    const location1: LocationModel = req.body[0];
    const location2: LocationModel = req.body[1];
   
    const token = req.get("Authorization");
    
    if(!token) {
        res.status(401).end();
        return;
    }
   ;
    res.status(200).json({ distance: getDistance(location1,location2)}).end();
};