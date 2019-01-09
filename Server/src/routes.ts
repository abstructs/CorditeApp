import * as userController from './controllers/user';
import * as express from 'express';
import * as bodyParser from 'body-parser';



const routes = express.Router();

routes.use(bodyParser.urlencoded({ extended: true }));
routes.use(bodyParser.json());

routes.get('/users', userController.getUsers);
routes.post('/users/signup', userController.signup);

export default routes;