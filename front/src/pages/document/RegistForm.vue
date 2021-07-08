<template>
  <div id="content">
    <input v-model="form.title" style="width: 100%" type="text" placeholder="title" />
    <br/><br/>
    <textarea v-model="form.content" placeholder="content" style="height: 200px;"></textarea>
    <br/><br/>
    <br/><br/>
    <select v-model="selected">
      <option v-bind:value="0" disabled hidden selected>approver</option>
      <option v-for="user in user_list" v-bind:value="user">{{user.name}}</option>
    </select>
    <button v-on:click="addApprover" type="button">add</button>
    <br/><br/>
    <table>
      <tr>
        <th>turn</th>
        <th>approver</th>
      </tr>
      <tr v-for="(approver, index) in form.approver_list" v-on:click="removeApprover(approver.user_seq)">
        <td>{{index + 1}}</td>
        <td>{{approver.name}}</td>
      </tr>
    </table>
    <br/>
    <p style="text-align: right;">* click table row to remove approver</p>
    <br/><br/>
    <br/><br/>
    <button v-on:click="submitForm" type="button">regist</button>
    <br/><br/>
    <h1>{{message}}</h1>
  </div>
</template>

<script>
import {tokenCheck, UNSUITABLE_DATA} from "@/assets/js/common.js";

const axios = require("axios");

export default {
  data () {
    return {
      message: "",
      selected: 0,
      user_list: [],
      form: {
        title: "",
        content: "",
        approver_list: []
      }
    }
  },
  mounted() {
    this.getUserList()
  },
  methods: {
    async submitForm() {
      const result = await axios({
        method: "post",
        url: "http://localhost:8080/api/document/regist",
        headers: {
          token: sessionStorage.getItem("token")
        },
        data: this.form,
        timeout : 1000 * 3
      });
      tokenCheck(result.data.status);

      if (result.data.status == UNSUITABLE_DATA) {
        this.message = result.data.message;
        return;
      }

      await this.$router.push("/document/list");
    },
    async getUserList() {
      const result = await axios({
        method: "get",
        url: "http://localhost:8080/api/user/list",
        timeout: 1000 * 3
      });
      tokenCheck(result.data.status);

      this.user_list = result.data.data1;
    },
    addApprover() {
      if (this.selected == 0) {
        return;
      }

      // check user whether aleady added or not
      let user = this.selected;
      let approver_list = this.form.approver_list;
      for (let i = 0; i < approver_list.length; i++) {
        if (approver_list[i].user_seq == user.seq) {
          return;
        }
      }

      // add approver to approver_list
      let approver = {
        user_seq: 0,
        name: ""
      };
      approver.user_seq = this.selected.seq;
      approver.name = this.selected.name;
      this.form.approver_list.push(approver);
    },
    removeApprover(user_seq) {
      let approver_list = this.form.approver_list;
      for (let i = 0; i < approver_list.length; i++) {
        if (approver_list[i].user_seq == user_seq) {
          approver_list.splice(i, 1);
        }
      }
    }
  }
}
</script>
