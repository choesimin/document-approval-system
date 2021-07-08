<template>
  <div id="content">
    <h1 style="display: inline-block;">{{document.title}}</h1>
    &nbsp &nbsp &nbsp &nbsp
    <span>{{document.state}}</span>
    <p style="font-size: 12px;">regist : {{document.regdate}}</p>
    <p style="font-size: 12px;">finish : {{document.findate}}</p>
    <p style="text-align: right">{{document.user_name}}</p>
    <p>{{document.content}}</p>
    <br/><br/>
    <br/><br/>
    <div v-if="isApprover">
      <textarea v-model="opinion" placeholder="opinion"></textarea>
      <button v-on:click="approve('approve')">approve</button>
      <button v-on:click="approve('reject')">reject</button>
    </div>
    <br/><br/>
    <br/><br/>
    <table>
      <tr>
        <th>turn</th>
        <th>approver</th>
        <th>state</th>
        <th>opinion</th>
        <th>finish date</th>
      </tr>
      <tr v-for="approver in document.approver_list">
        <td>{{approver.turn}}</td>
        <td>{{approver.user_name}}</td>
        <td>{{approver.state}}</td>
        <td>{{approver.opinion}}</td>
        <td style="font-size:12px;">{{approver.findate}}</td>
      </tr>
    </table>
  </div>
</template>

<script>
import {tokenCheck} from "@/assets/js/common.js";
import {SUCCESS} from "@/assets/js/common.js";

const axios = require("axios");

export default {
  mounted() {
    this.getDocument();
  },
  data() {
    return {
      isApprover: false,
      document: {
        title: "",
        content: "",
        user_name: "",
        state: "",
        regdate: "",
        approver_list: []
      },
      opinion: ""
    }
  },
  methods: {
    getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
      return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    async getDocument() {
      const result = await axios({
        method: "get",
        url: "http://localhost:8080/api/document/detail",
        headers: {
          token: sessionStorage.getItem("token")
        },
        params: {
          seq: this.getParameterByName("seq")
        },
        timeout: 1000 * 3
      });
      tokenCheck(result.data.status);

      if (result.data.status == SUCCESS) {
        this.isApprover = true;
      }

      this.document = result.data.data1;
    },
    async approve(state) {
      let approver_list = this.document.approver_list;
      let approver_seq = 0;

      for (var i = 0; i < approver_list.length; i++) {
        let approver = approver_list[i];

        if (this.document.turn == approver.turn) {
          approver_seq = approver.seq;
        }
      }

      const result = await axios({
        method: "post",
        url: "http://localhost:8080/api/document/approve",
        params: {
          seq: approver_seq,
          document_seq: this.getParameterByName("seq"),
          state: state,
          opinion: this.opinion
        },
        timeout: 1000 * 3
      });
      tokenCheck(result.data.status);

      this.isApprover = false;
      this.opinion = "";
      await this.getDocument();
    }
  }
}
</script>
