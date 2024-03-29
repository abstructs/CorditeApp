import * as express from 'express';
import * as mongoose from "mongoose";
import routes from './src/routes';

const mongoUrl = "mongodb://localhost:27017/cordite";

mongoose.set('useCreateIndex', true);

mongoose.connect(mongoUrl, { useNewUrlParser: true }, (err) => {
    if(err) {
        console.error("Having problems connecting to mongo.");
        process.exit();
    }
});

const PORT = process.env.PORT || 3000;

const app = express();

app.use(routes);
app.set("port", PORT);

app.listen(app.get("port"), () => console.log(`Server is running on port=${PORT}`));

export default app;