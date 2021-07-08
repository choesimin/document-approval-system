<template>
  <div id="content">
    <button class="button_active">{{user_name}}</button>
    <button v-bind:class="{button_active: category == 'outbox'}" type="button" v-on:click="setSearchMode(false), setCurrentPage(1), setCategory('outbox'), getList()">outbox</button>
    <button v-bind:class="{button_active: category == 'inbox'}" type="button" v-on:click="setSearchMode(false), setCurrentPage(1), setCategory('inbox'), getList()">inbox</button>
    <button v-bind:class="{button_active: category == 'archive'}" type="button" v-on:click="setSearchMode(false), setCurrentPage(1), setCategory('archive'), getList()">archive</button>
    <button style="float: right;" type="button" v-on:click="setSearchMode(true), setCurrentPage(1), getList()">search</button>
    <input style="float: right;" v-model="search_word" type="text" placeholder="search with title" />
    <br/><br/>
    <table>
      <tr>
        <th>writer</th>
        <th>title</th>
        <th>regist date</th>
        <th>state</th>
      </tr>
      <tr v-for="document in list" v-on:click="detail(document.seq)">
        <td>{{document.user_name}}</td>
        <td>{{document.title}}</td>
        <td>{{document.regdate}}</td>
        <td>{{document.state}}</td>
      </tr>
    </table>
    <br/>
    <div style="text-align: center;">
      <span v-if="current_page > pager.block_size">
        <button v-on:click="setCurrentPage(1), getList()">1</button>
        <button v-on:click="setCurrentPage(pager.first_page - 1), getList()"><</button>
      </span>
      <span v-for="page in page_list">
        <button v-bind:class="{button_active: current_page == page}" v-on:click="setCurrentPage(page); getList()">{{page}}</button>
      </span>
      <span v-if="current_page <= (pager.total_page - 1) - ((pager.total_page - 1) % pager.block_size)">
        <button v-on:click="setCurrentPage(pager.last_page + 1), getList()">></button>
        <button v-on:click="setCurrentPage(pager.total_page), getList()">{{pager.total_page}}</button>
      </span>
    </div>
  </div>
</template>

<script>
import {tokenCheck} from "@/assets/js/common.js";

const axios = require("axios");

export default {
  data() {
    return {
      user_name: "",
      search_word: "",
      list: [],
      category: "outbox",
      current_page: 1,
      page_list: [],
      pager: {},
      isSearching: false
    }
  },
  mounted() {
    this.getList();
    this.getUsername();
  },
  methods: {
    setCategory(category) {
      this.category = category;
    },

    setCurrentPage(current_page) {
      this.current_page = current_page;
    },

    setPageList() {
      this.page_list = [];

      for (let i = this.pager.first_page; i <= this.pager.last_page; i++) {
        if (i > this.pager.total_page) break;
        this.page_list.push(i);
      }
    },

    setSearchMode(flag) {
      this.isSearching = flag;
    },

    detail(seq) {
      this.$router.push("/document/detail?seq=" + seq);
    },

    async getList() {
      const result = await axios({
        method: "get",
        url: "http://localhost:8080/api/document/list",
        headers: {
          token: sessionStorage.getItem("token")
        },
        params: {
          category: this.category,
          search_word: this.search_word,
          current_page: this.current_page,
          isSearching: this.isSearching
        },
        timeout: 1000 * 3
      });
      tokenCheck(result.data.status);

      this.list = result.data.data1;
      this.pager = result.data.data2;

      this.setPageList();
    },

    async getUsername() {
      const result = await axios({
        method: "get",
        url: "http://localhost:8080/api/user/name",
        headers: {
          token: sessionStorage.getItem("token")
        },
        timeout: 1000 * 3
      });
      tokenCheck(result.data.status);

      this.user_name = result.data.data1;
    }
  }
}
</script>
