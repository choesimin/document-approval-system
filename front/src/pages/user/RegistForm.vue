<template>
  <div id="content">
    <h1>{{ title }}</h1>
    <input v-model="form.name" type="text" placeholder="name (3~20)" />
    <input v-model="form.id" type="text" placeholder="id (6~20)" />
    <input v-model="form.password" type="password" placeholder="passowrd (6~20)" />
    <input v-model="form.repassword" type="password" placeholder="repassword" />
    <button type="button" v-on:click="submitForm">regist</button>
    <div id="link-box">
      <router-link to="/">go to login</router-link>
    </div>
  </div>
</template>
<script>
  const axios = require('axios');
  import {UNSUITABLE_DATA} from "@/assets/js/common.js";

  export default {
    data: function () {
      return {
        title: 'regist',
        form: {
          name: '',
          id: '',
          password: '',
          repassword: ''
        }
      }
  	},
    methods: {
      async submitForm() {
        const result = await axios({
          method: "post",
          url: "http://localhost:8080/api/user/regist",
          data: this.form,
          timeout : 1000 * 3
        });

        if (result.data.status == UNSUITABLE_DATA) {
          this.title = result.data.message;
          return;
        }

        this.title = result.data.message;

        this.form.name = '';
        this.form.id = '';
        this.form.password = '';
        this.form.repassword = '';
      }
    }
  }
</script>
<style scoped>
@import '../../assets/css/user.css';
</style>
