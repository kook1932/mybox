<template>
  <div id="app">
    <Header />
    <Gnb />
    <router-view></router-view>
  </div>
</template>

<script>
import Header from './components/layout/Header.vue'
import Gnb from './components/layout/Gnb.vue'

export default {
  name: 'App',
  components: {
    Header,
    Gnb
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
}
</style>
