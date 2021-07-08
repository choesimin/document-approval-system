<template>
  <div id="content">
    <h1>{{ title }}</h1>
    <input v-model="form.id" type="text" placeholder="id" />
    <input v-model="form.password" type="password" placeholder="password" />
    <button type="button" v-on:click="submitForm">login</button>
		<div id="link-box">
      <router-link to="/user/registform">go to regist</router-link>
		</div>
  </div>
</template>
<script>
  const axios = require("axios");
  import {NO_DATA} from "@/assets/js/common.js";

  export default {
    data: function () {
      return {
        title: "login",
        form: {
          id: "",
          password: ""
        }
      }
    },
    methods: {
      async submitForm() {
        const result = await axios({
          method: "post",
          url: "http://localhost:8080/api/user/login",
          data: this.form,
          timeout : 1000 * 3
        });

        if (result.data.status == NO_DATA) {
          this.title = result.data.message;
          return;
        }

        sessionStorage.setItem("token", result.data.token);
        await this.$router.push("/document/list");
      }
    }
  }
</script>
<style scoped>
@import '../../assets/css/user.css';
</style>
