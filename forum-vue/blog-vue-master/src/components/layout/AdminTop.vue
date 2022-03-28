<template>
<div>
  <el-menu :default-active="activeIndex"  mode="horizontal" @select="handleSelect" class="container" >
          <el-menu-item index="1"><router-link to="/Welcome"><i class="el-icon-s-home"></i> 首页 </router-link></el-menu-item>
          <el-menu-item index="2" @click="dialogFormVisible= true"><i class="el-icon-map-location"></i>访问分布</el-menu-item>
  </el-menu>
  <el-button type="primary" round @click="logout">退出</el-button>
  <el-dialog title="用户分布" :visible.sync="dialogFormVisible">
    <div slot="footer" class="dialog-footer">
        <Map></Map>
    </div>
  </el-dialog>
</div>
</template>

<script>
import Map from '../../views/admin/dataWatch/map'
export default {
  components: {
    Map
  },
  data () {
    return {
      activeIndex: '0',
      activeIndex2: '0',
      dialogFormVisible: false
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    logout () {
      this.$http.get('/api/logout')
      this.$router.push('/login')
      // window.location.reload()
    }
  }
}
</script>

<style>
  .container {
    color: #fff;
    width: 100%;
  }
</style>
