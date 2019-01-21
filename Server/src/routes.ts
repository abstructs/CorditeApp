import * as userController from './controllers/user';
import * as express from 'express';
import * as bodyParser from 'body-parser';
import * as morgan from 'morgan';

const routes = express.Router();

routes.use(morgan('tiny'));
routes.use(bodyParser.urlencoded({ extended: true }));
routes.use(bodyParser.json());


routes.get('/users', userController.getUsers);
routes.post('/users/emailtaken', userController.emailTaken);
routes.post('/users/signup', userController.signup);
routes.post('/users/login', userController.login);

export default routes;