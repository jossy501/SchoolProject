const express = require('express');
const Joi = require('joi'); // Joi for validation
const bcrypt = require('bcryptjs'); //bcrypt for password hashing
const jwt = require('jsonwebtoken');
const db = require('./db/connection.js');
const users = db.get('users');
//users.index('username')
users.createIndex('username', {unique: true });

const router = express.Router();

// Use Joi for validation
const schema = Joi.object().keys({
    username: Joi.string().regex(/^[a-zA-Z0-9]+$/).min(2).max(30).required(),
    password: Joi.string().trim().min(10).required()
    //username: Joi.string().alphanum().min(3).max(30).required(),
    //password: Joi.string().regex(/^[a-zA-Z0-9]{3,30}$/).required()
});
 
function createTokenSendResponse(user, res, next){
    const payload = {
        _id: user._id,
        username: user.username
    };
    console.log('Token secret is ' , process.env.TOKEN_SECRET,'Payload is ',payload);
    jwt.sign(payload, process.env.TOKEN_SECRET , {
        expiresIn: '2d'
    }, (err, token ) => {
        if(err){
            respondError422(res , next );
        }else{
            res.json({
                token
            });
        }
    });

}

// any route in here is pre-pended with /auth

router.get('/', (req, res) => {
    res.json({
        message: 'Auth router is working'
    });
});

router.post('/signup', (req, res, next) => {
    //console.log('body', req.body);
    const result = Joi.validate(req.body, schema);
    if(result.error == null){
        // make sure username is unique
        users.findOne({
            username: req.body.username
        }).then(user => {

            if(user){

                // there is already a user in the database with this username
                // with need to response with an error 
                const error  = new Error('That username already exist. Please choose another one');
                res.status(409);
                next(error);
            }else{
                // hash the password 
                // insert the user with the hashed password
                bcrypt.hash(req.body.password, 12).then(hashPassword => {
                    // insert the user with the hashed password
                    //res.json({hashPassword});
                    const newUser = {
                        username: req.body.username,
                        password: hashPassword
                    };
                    users.insert(newUser).then(insertedUser => {
                        //delete insertedUser.password;
                       // res.json(insertedUser);
                       createTokenSendResponse(insertedUser, res , next);
                    })
                });
                
            }

           // res.json({ user });
        });

    }else{
        res.status(422);
        next(result.error);
        
    }
    //res.json(result);
    /*res.json({
        message: 'Yay you have sign up'
    });*/
});

function respondError422(res , next ) {
    res.status(422);
    const error = new Error('Unable to login');
    next(error);
}

router.post('/login', (req, res, next) => {
    const result = Joi.validate(req.body, schema);
    if(result.error == null){
       users.findOne({
           username: req.body.username,
       }).then(user => {
            if(user){
              console.log('Comparing ',req.body.password, 'with the hash', user.password);
              bcrypt
              .compare(req.body.password, user.password)
              .then((result) => {
                if(result){
                    createTokenSendResponse(user, res, next);
                }else{
                    respondError422(res , next );
                }
              });
            }else{
                respondError422(res , next );
            }
       });
    }else{
        respondError422(res , next );
    }

});


module.exports = router;