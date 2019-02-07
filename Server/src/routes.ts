import * as userController from './controllers/user';
import * as runController from './controllers/run';
import * as reportController from './controllers/report';
import * as express from 'express';
import * as bodyParser from 'body-parser';
import * as morgan from 'morgan';
import * as jwt from 'jsonwebtoken';

const routes = express.Router();

const tokenSecret: jwt.Secret = "fake_secret";

const authenticateUser = (req: express.Request, res: express.Response, next: express.NextFunction) => {
    const token = req.get('Authorization');

    if(!token) {
        res.status(400).send({ errors: { auth: "No credentials sent." }}).end();
        return;
    }

    try {
        const decoded = jwt.verify(token, tokenSecret);

        if(decoded) {
            next();
        }
    } catch(err) {
        res.status(401).send({ errors: { auth: "Invalid credential." }}).end();
        return;
    }
}

routes.use(morgan('tiny'));
routes.use(bodyParser.urlencoded({ extended: true }));
routes.use(bodyParser.json());


routes.get('/users', userController.getUsers);
routes.post('/users/emailtaken', userController.emailTaken);
routes.post('/users/signup', userController.signup);
routes.post('/users/login', userController.login);

routes.post('/runs', authenticateUser, runController.saveRun);
routes.post('/runs/myRuns', authenticateUser, runController.getRuns);

routes.post('/reports', authenticateUser, reportController.saveReport);
routes.post('/reports/aroundMe', authenticateUser, reportController.getReports);
routes.post('/reports/report', authenticateUser, reportController.getReportDistance);

export default routes;