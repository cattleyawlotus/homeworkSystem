<template>
  <div id="app">
    <!-- 导航栏 -->
    <TopNavBar
      v-show="
        path === '/types' ||
        path === '/home' ||
        path === '/archives' ||
        path === '/blog' ||
        path === '/tags' ||
        path === '/message' ||
        path === '/collects' ||
        path === '/info' ||
        path === '/myinfo' ||
        path === '/notify'
      "
    ></TopNavBar>
    <router-view />
  </div>
</template>
<script>
import Vue from 'vue'
import TopNavBar from './components/layout/TopNavBar'
export default {
  data () {
    return {
      path: '',
      myApp: new Vue() // 为了非父子组件共享值
    }
  },
  components: {
    TopNavBar
    // adminTop
  },
  // 判断路由
  mounted () {
    this.path = this.$route.path
  },
  watch: {
    $route (to, from) {
      this.path = to.path
    }
  }
}
</script>
<style>
</style>
