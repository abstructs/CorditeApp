import * as express from 'express';
import routes from './src/routes';

const PORT = 3000;

const app = express();

app.use(routes);

app.listen(PORT, () => console.log(`Server is running on port=${PORT}`));