import express from 'express';
import mustacheExpress from 'mustache-express';
import bodyParser from 'body-parser';
import { __dirname } from './dirname.js';
import boardRouter from './marketRouter.js';

const app = express();
app.set('views', __dirname + '/../views'); 
app.set('view engine', 'html');
app.engine('html', mustacheExpress());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(__dirname + '/../public'));
app.use('/', boardRouter);
app.listen(5000, () => console.log('Listening on port 5000!'));