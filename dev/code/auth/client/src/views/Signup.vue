<template>
   <section>
         <h1>Signup</h1>
         <div v-if="SigningUP">
             <img src="../assets/Pacman-0.7s-148px.svg" />
         </div>
         <div v-if="errorMessage" class="alert alert-danger" role="alert">
            {{errorMessage}}
        </div>
    <form v-f="!SigningUP" @submit.prevent="signup">
        <div class="form-group">
            <label for="username">Username</label>
            <input v-model="user.username" type="text" class="form-control" id="username" aria-describedby="usernameHelp" placeholder="Enter a username" required>
            <small id="usernameHelp" class="form-text text-muted">Username must be longer than 2 character and shorter than 30 character
            Username can only contain alphanumeric character and under_score.</small>
        </div>
        <div class="form-row">
             <div class="form-group col-md-6">
            <label for="password">Password</label>
            <input v-model="user.password" type="password" class="form-control" id="password" 
            aria-describedby="passwordHelp" placeholder="Password" required>
            <small id="usernameHelp" class="form-text text-muted">Password must be 10 or more character 
            long.
           </small>
        </div>
         <div class="form-group col-md-6">
            <label for="confirmPassword">Confirm Password</label>
            <input v-model="user.confirmPassword" type="password" class="form-control" id="confirmPassword" 
            aria-describedby="confirmPasswordHelp" placeholder="ConfirmPassword" required>
            <small id="confirmPasswordHelp" class="form-text text-muted">Please confirm your password.
           </small>
        </div>

        </div>
       
        <button type="submit" class="btn btn-primary">Signup</button>
    </form>
   </section>
</template>

<script>
import Joi from 'joi';
//import Pacman from '../assets/Pacman-1s-193px.svg';

const SIGNUP_URL = 'http://localhost:5000/auth/signup';

const schema = Joi.object().keys({
    username: Joi.string().regex(/^[a-zA-Z0-9]+$/).min(2).max(30).required(),
    password: Joi.string().trim().min(10).required(),
    confirmPassword: Joi.string().trim().min(10).required(),
});

export default {
    data: () => ({
        SigningUP: false,
        errorMessage: '',
        user: {
            username: '',
            password: '',
            confirmPassword: '',

        },
    }),
    watch: {
        user:{
            handler(){
                this.errorMessage = '';
            },
            deep:true,
        },
    },
    methods:{
        signup(){
            this.errorMessage = '';
            if(this.validUser()){
                const body = {
                    username: this.user.username,
                    password: this.user.password,
                };
                this.SigningUP = true;
                fetch(SIGNUP_URL, {
                    method: 'POST',
                    body: JSON.stringify(body),
                    headers: {
                        'content-type': 'application/json',
                    },
                  }).then((response) => {
                      if(response.ok){
                          return response.json();
                      }
                      return response.json().then(error => {
                          throw new Error(error.message);
                      });
                  }).then((result) => {
                      //localStorage.token = result.token;
                     //console.log(user);
                     setTimeout(() => {
                     this.SigningUP = false;
                     this.$router.push('/login');
                     }, 1000);
                  }).catch((error) => {
                       setTimeout(() => {
                     this.SigningUP = false;
                     this.errorMessage = error.message;
                     }, 1000); 
                  });
            }
        },
        validUser(){
            if(this.user.password != this.user.confirmPassword){
                this.errorMessage = 'Password must match.';
                return false;
            }
            
            const result = Joi.validate(this.user, schema);
            if(result.error == null){
                return true;
            }

            if(result.error.message.includes('username')) {
                this.errorMessage = 'Username is invalid';
            }else{
                this.errorMessage = 'Password is invalid';
            }

            console.log(result.error.message);
            return false;
            
        },
    },
};

</script>
