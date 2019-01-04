<template>
  <div class="home text-center">
  <h1>Home</h1>
    <div class="jumbotron">
      <h2 class="display-3">Welcome to Overland Community</h2>
      <p class="lead">
      <h2>Connect  Share  &  Engage</h2>
    
      <hr class="my-4">
      <p class="lead">
        <router-link 
          class="btn btn-primary btn-lg"
          :to="{name:'signup'}"
          role="button">Sign Up
        </router-link>
      </p>
      
    </div>
  </div>
</template>

<script>
export default {
  name: 'home',

};
</script>
