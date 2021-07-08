<template>
  <div id="app">
    <Nav />
    <router-view />
  </div>
</template>

<script>
const axios = require('axios');
import Nav from '@/components/Nav.vue'

export default {
  components: {
    Nav
  }
}
</script>

<style>
@import '../../assets/css/document.css';
</style>
