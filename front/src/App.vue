<template>
  <div id="app">
    <router-view />
  </div>
</template>
<script>
export default {

}
</script>
<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-size: 15px;
  font-family: sans-serif;
}
a {
  color: black;
}
input, button, select {
  -webkit-appearance: none;
  border: 1px solid black;
  padding: 8px;
  background-color: white;
}
button:hover {
  color: white;
  background-color: black;
}
#content {
  padding: 20px;
}
</style>
