<template>
<div>
  <div class='info'>
    <div class='title'>消息通知 <el-button class="refresh" type="primary" round @click="refresh">刷新</el-button></div>
      <div class="content">
          <el-table :data="this.data" style="width: 100%;" height="600">
            <el-table-column prop="content" label="通知">
            </el-table-column>
          </el-table>
      </div>
  </div>
</div>
</template>

<script>
const Base64 = require('js-base64').Base64
export default {
  components: {
  },
  data () {
    return {
      data: [{ content: '暂无新消息' }],
      uid: ''
    }
  },
  created () {
    this.getUser()
    this.conn()
  },
  methods: {
    async conn () {
      const res = await this.$http.get(`api/forum/user/receiveDirectMessage?qname=queue${this.uid}`)
      if (JSON.parse(window.sessionStorage.getItem('notifylist')) !== null) {
        this.data = JSON.parse(window.sessionStorage.getItem('notifylist'))
      }
      console.log(this.data)
      if (res.data.data.length > 0) {
        this.data.pop()
        for (var i in res.data.data) {
          this.data.push({ content: Base64.decode(res.data.data[i]) })
        }
        window.sessionStorage.setItem('notifylist', JSON.stringify(this.data))
      }
    },
    async refresh () {
      const res = await this.$http.get(`api/forum/user/receiveDirectMessage?qname=queue${this.uid}`)
      if (JSON.parse(window.sessionStorage.getItem('notifylist')) !== null) {
        this.data = JSON.parse(window.sessionStorage.getItem('notifylist'))
      }
      console.log(this.data)
      if (res.data.data.length > 0) {
        for (var i in res.data.data) {
          this.data.push({ content: Base64.decode(res.data.data[i]) })
        }
        window.sessionStorage.setItem('notifylist', JSON.stringify(this.data))
      }
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.uid = window.sessionStorage.getItem('userid')
      }
    }
  }
}
</script>

<style>

.info {
    /* width: 1000px; */
    background: #fff;
    margin-bottom: 10px;
}
.title {
  height: 68px;
  line-height: 68px;
  font-size: 28px;
  font-weight: 600;
  color: #2e2e2e;
  padding-left: 16px;
  border-bottom: 1px solid #f0f0f2;
}
.refresh {
  width: 20px;
  height:20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.content {
  padding: 0px 16px 16px 16px;
}
.notify {
  padding-left: 25px;
  /* background:#FFA500; */
  border-radius: 25px;
  height:40px;
  line-height: 40px;
  font-size: 20px;
  font-weight: 600;
  align-items: center;
}
</style>
