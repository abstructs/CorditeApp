"use strict";
exports.__esModule = true;
var jwt = require("jsonwebtoken");
var tokenSecret = "fake_secret";
var tokenOptions = {
    expiresIn: "7 days"
};
// export const getUsers = (req: Request, res: Response) => {
//     res.status(500).send("not implemented").end();
// };
exports.saveRun = function (req, res) {
    console.debug(req.headers);
    var token = req.get("Authorization");
    if (!token) {
        res.status(400).end();
        return;
    }
    var decoded = jwt.decode(token);
    console.log(decoded);
    var run = req.body;
    res.status(200).json({ success: true }).end();
};
