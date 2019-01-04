const express = require('express');
const volleyball = require('volleyball'); // logger api 
const cors = require('cors');
require('dotenv').config();

const bodyParser = require('body-parser');
const morgan = require('morgan');

const messages = require('./auth/db/messages');

const app = express();

app.use(morgan('tiny'));
app.use(cors());
app.use(bodyParser.json());

const middleware = require('./auth/middlewares');

const auth = require('./auth/index.js');

app.use(volleyball);
app.use(cors({
    origin: 'http://localhost:8080'
}));
app.use(express.json());
app.use(middleware.checkTokenSetUser);

app.get('/',(req, res) => {
    res.json({
        message: 'Hello world!',
        user: req.user,
    });
});


app.get('/messageboard', (req, res) => {
    res.json({
      message: 'full stack message board! ðŸŽ‰'
    });
  });

  app.get('/messages', (req, res) => {
    messages.getAll().then((messages) => {
      res.json(messages);
    });
  });
  
  app.post('/messages', (req, res) => {
    console.log(req.body);
    messages.create(req.body).then((message) => {
      res.json(message);
    }).catch((error) => {
      res.status(500);
      res.json(error);
    });
  });

app.use('/auth', auth);

function notFound(req, res, next) {
    res.status(404);
    const error = new Error('Not found - '+ req.originalUrl);
    next(error);
}

function errorHandler(err,req,res,next){
    res.status(res.statusCode || 500);
    res.json({
        message: err.message,
        stack: err.stack
    });
}

app.use(notFound);
app.use(errorHandler);

const port = process.env.port || 5000;
app.listen(port, () => {
    console.log('Listening on port ', port);
});