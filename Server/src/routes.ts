import * as userController from './controllers/user';
import * as express from 'express';

const routes = express();

routes.get('/users', userController.getUsers);
routes.post('/users/signup', userController.signup);

export default routes;