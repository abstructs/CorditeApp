import * as express from 'express';

// console.log("Hello");

const PORT = 3000;

const app = express();

app.get("/", (req, res) => {
    res.status(200).send("hello world").end();
});

app.listen(PORT, () => console.log(`Server is running on port=${PORT}`));