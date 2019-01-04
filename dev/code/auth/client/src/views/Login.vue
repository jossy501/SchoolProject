<template>
  <section>
       <h1>Login</h1>
       <div v-if="loggingIn">
             <img src="../assets/Pacman-0.7s-148px.svg" />
         </div>
        <div v-if="errorMessage" class="alert alert-danger" role="alert">
            {{errorMessage}}
        </div>
       <form @submit.prevent="login()">
         <div class="form-group">
            <label for="username">Username</label>
            <input v-model="user.username" type="text" class="form-control" id="username" aria-describedby="usernameHelp" placeholder="Enter a username" required>
            <small id="usernameHelp" class="form-text text-muted">Enter username to login</small>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input v-model="user.password" type="password" class="form-control" id="password" 
            aria-describedby="passwordHelp" placeholder="Password" required>
            <small id="usernameHelp" class="form-text text-muted">Password must be 10 or more character 
            long.
           </small>
        </div>
               <button type="submit" class="btn btn-primary">Login</button>
       </form>
  </section>
</template>
<script>
import Joi from 'joi';

const LOGIN_URL = 'http://localhost:5000/auth/login';

const schema = Joi.object().keys({
    username: Joi.string().regex(/^[a-zA-Z0-9]+$/).min(2).max(30).required(),
    password: Joi.string().trim().min(10).required(),
});
export default {
     data: () => ({
       errorMessage: '',
       loggingIn: false,
        user: {
            username: '',
            password: '',
        },
    }),
    methods: {
      login(){
        this.errorMessage = '';
          if(this.validUser()){
             this.loggingIn = true;
           // console.log('Logging In');
            const body = {
                username : this.user.username,
                password : this.user.password,
            };
            fetch(LOGIN_URL, {
                method: 'POST',
                headers: {
                    'content-type': 'application/json',
                },
                body: JSON.stringify(body),
            }).then((response) => {
                      if(response.ok){
                          return response.json();
                      }
                      return response.json().then(error => {
                          throw new Error(error.message);
                      });
                  }).then((result) => {
                     console.log(result);
                     localStorage.token = result.token;

                     //console.log('Result here',result);

                     setTimeout(() => {
                     this.loggingIn = false;
                     this.$router.push('/dashboard');
                     }, 1000);
                  }).catch((error) => {
                       setTimeout(() => {
                     this.loggingIn = false;
                     this.errorMessage = error.message;
                     }, 1000); 
                  });
          }
      },
      validUser(){
        const result = Joi.validate(this.user, schema);
            if(result.error == null){
                return true;
            }

            if(result.error.message.includes('username')) {
                this.errorMessage = 'Username is invalid';
            }else{
                this.errorMessage = 'Password is invalid';
            }
            return false;
      }

    },

};
</script>
<style>

</style>
