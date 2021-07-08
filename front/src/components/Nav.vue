<template>
  <nav>
    <router-link to="/document/list">list</router-link>
    <router-link to="/document/registform">regist</router-link>
    <br/><br/><br/><br/>
    <a v-on:click="logout">logout</a>
  </nav>
</template>
<script>
const axios = require("axios");

export default {
  methods: {
    logout() {
      sessionStorage.removeItem("token");
      location.href = "/";
    }
  }
}
</script>
<style scoped>
nav {
  position: fixed;
  height: 100%;
  width: 200px;
  padding-top: 20px;
  text-align: center;
  background-color: black;
}
a {
  display: block;
  width: 100%;
  padding: 10px 0;
  text-decoration: none;
  color: white;
  font-size: 20px;
}
a:hover {
  background-color: white;
  color: black;
}
</style>
