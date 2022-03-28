<template>
<div>
  <div class="info">
    <div class="title">基本信息</div>
    <div class="content">
      <el-form :label-position="labelPosition" label-width="80px" :model="user">
        <el-form-item label="id:" class="content-l">
          <el-input class="content-r" v-model="user.id" :disabled=true></el-input>
        </el-form-item>
       <el-form-item label="用户名:" class="content-l">
          <el-input class="content-r" v-model="user.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="昵称:" class="content-l">
          <el-input class="content-r" v-model="user.nickname" :disabled="this.modify"></el-input>
        </el-form-item>
        <el-form-item label="密码:" class="content-l">
          <el-input class="content-r" v-model="user.password" :disabled="this.modify"></el-input>
        </el-form-item>
        <el-form-item label="邮箱:" class="content-l">
          <el-input class="content-r" v-model="user.email" :disabled="this.modify"></el-input>
        </el-form-item>
        <el-form-item label="角色:" class="content-l">
          <el-input class="content-r" v-model="user.role" :disabled="true"></el-input>
        </el-form-item>
         <el-form-item class="content-l">
            <span slot="label">
              <el-button v-if=this.buttonshow class="modify" type="primary" icon="el-icon-edit" circle @click="domodify"></el-button>
              <el-button v-if=!this.buttonshow class="modify" type="success" icon="el-icon-check" circle @click="check"></el-button>
            </span>
        </el-form-item>
      </el-form>
    </div>
  </div>
</div>
</template>

<script>
export default {
  components: {
  },
  data () {
    return {
      labelPosition: 'right',
      modify: true,
      buttonshow: true,
      user: {
        id: '',
        username: '',
        nickname: '',
        password: '',
        email: '',
        role: '',
        avatar: ''
      }
    }
  },
  created () {
    this.getuser()
  },
  methods: {
    getuser () {
      const id = window.sessionStorage.getItem('userid')
      this.$http.get(`/api/forum/user/info?uid=${id}`).then(res => {
        if (res.data.success) {
          this.user = res.data.data
        } else {
          this.$message.error('获取用户信息失败!' + res.data.message)
        }
      })
    },
    domodify () {
      this.modify = false
      this.buttonshow = false
    },
    check () {
      this.modify = true
      this.buttonshow = true

      this.$http.put('/api/forum/user/update', this.user).then(res => {
        if (res.data.success) {
          this.$message.success('修改用户成功!')
          this.getuser()
        } else {
          this.$message.error('修改用户信息失败!' + res.data.message)
        }
      })
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
.content {
  padding: 0px 16px 16px 16px;
}
.content-l {
  /* display:inline; */
  margin: 10px 0px 0px 0px;
  font-size: 18px;
  font-weight: 600;
  color: #2e2e2e;
}
.content-r {
  width: 200px;
  padding: 0px 30px 0px 00px;
}
.modify {
  /* padding: 20px; */
  width: 20px;
  height:20px;
  /* float: right; */
  /* margin-top: 15px; */
  /* margin-left: 10px; */
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

</style>
