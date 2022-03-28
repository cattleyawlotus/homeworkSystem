<template>
    <div class="login_container">
      <!-- banner -->
      <div class="archive-banner banner">
        <h1 class="banner-title">归档</h1>
      </div>
      <div class="login_box">
      <!-- 头像区域 -->
      <div class="avatar_box">
        <img src="../assets/images/logo.jpg" alt="">
      </div>
        <!-- 登录表单区域 -->
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
          <!-- 用户名 -->
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user-solid"></el-input>
          </el-form-item>
          <!-- 密码 -->
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" placeholder="请输入密码" prefix-icon="el-icon-moon-night" type="password"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-input v-model="loginForm.code" placeholder="请输入验证码" prefix-icon="el-icon-picture-outline-round" style="width: 50%"></el-input>
            <img v-bind:src="verifyCode" @click="getVerifyCode()" width="130px" height="35px" style="float: right;cursor:pointer;" />
          </el-form-item>
          <!-- 按钮区域 -->
          <el-form-item class="btns">
            <el-button type="success" @click="handleCreate">注册</el-button>
            <el-button type="primary" @click="login">登录</el-button>
            <el-button type="info" @click="resetLoginForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 注册弹层-->
      <div class="add-form">
        <el-dialog title="注册用户" :visible.sync="dialogFormVisible">
          <el-form ref="registForm" :model="formData" :rules="loginFormRules" label-position="right"
                   label-width="100px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input label="请输入用户名" v-model="formData.username"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="密码" prop="password">
                  <el-input v-model="formData.password"/>
                </el-form-item>
              </el-col>
            </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="formData.nickname"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱">
                <el-input v-model="formData.email"/>
              </el-form-item>
            </el-col>
          </el-row>
            <!-- <el-col :span="24">
              <el-form-item label="头像地址">
                <el-input v-model="formData.avatar"/>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="#"
                accept="image/png,.jpg"
                multiple
                :limit="1"
                :on-exceed="masterFileMax"
                :show-file-list="false"
                :http-request="uploadPic"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              </el-form-item>
            </el-col>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="regist()">确定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
      imageUrl: 'data:image/jpeg;base64,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',
      verifyCode: '',
      formData: {
        username: '',
        password: '',
        nickname: '',
        email: '',
        avatar: 'data:image/jpeg;base64,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',
        role: 'user'
      },
      dialogFormVisible: false, // 增加表单是否可见
      // 这是登录表单的数据绑定对象
      loginForm: {
        username: 'admin',
        password: '123456',
        verKey: '',
        code: ''
      },
      // 这是表单的验证规则对象
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入登录名称', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        // 验证码
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ],
        nickname: [
          { required: false, message: '请输入昵称', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    window.sessionStorage.clear()
    this.getVerifyCode()
  },
  methods: {
    masterFileMax (files, fileList) {
      console.log(files, fileList)
      this.$message.warning('请最多上传一张图片')
    },
    // 将本地图片转化为Base64
    ImageToBase64 (files) {
      var reader = new FileReader()
      reader.readAsDataURL(files)
      reader.onload = () => {
        // console.log('file 转 base64结果：' + reader.result)
        this.imageUrl = reader.result
        this.formData.avatar = reader.result
      }
      reader.onerror = function (error) {
        console.log('Error: ', error)
      }
    },
    async uploadPic (param) {
      var fileObj = param.file
      this.ImageToBase64(fileObj)
    },
    handleAvatarSuccess (res, file) {
      // this.imageUrl = URL.createObjectURL(file.raw)
    },
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt5M) {
        this.$message.error('上传头像图片大小不能超过5MB!')
      }
      return isJPG && isLt5M
    },
    async getVerifyCode () {
      const { data: res } = await this.$http.get('/api/forum/user/default')
      if (res.success) {
        this.verifyCode = 'data:image/png;base64,' + res.data.img
        this.loginForm.verKey = res.data.cToken
      } else {
        this.$message.error(res.message)
      }
    },
    // 点击重置按钮，重置登录表单
    resetLoginForm () {
      this.$refs.loginFormRef.resetFields()
    },
    // 弹出添加窗口
    handleCreate () {
      this.dialogFormVisible = true
      if (this.$refs.registForm != null) {
        this.$refs.registForm.resetFields()
      }
    },
    regist () {
      // 进行表单校验
      this.$refs.registForm.validate((valid) => {
        if (valid) {
          // 表单校验通过，发ajax请求，把数据录入至后台处理
          // const param = this.$encrypTion(JSON.stringify(this.formData))
          const param = this.formData
          this.$http.post('/api/forum/user/register', param).then((res) => {
            // 关闭新增窗口
            this.dialogFormVisible = false
            if (res.data.success !== false) {
              // 弹出提示信息
              this.$message({
                message: '注册成功，快来登录吧！',
                type: 'success'
              })
            } else { // 执行失败
              this.$message.error(res.data.message)
            }
          })
        } else { // 校验不通过
          this.$message.error('校验失败，请检查输入格式')
          return false
        }
      })
    },
    login () {
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) return
        // console.log('===>' + JSON.stringify(this.loginForm))
        // console.log('===>' + this.$encrypTion(param))
        // 加密
        // var param = this.$encrypTion(JSON.stringify(this.loginForm))
        var param = this.loginForm
        // console.log('param2' + param2)
        // const { data: res } = await this.$http.post('/api/server/admapi/login', param)
        const { data: res } = await this.$http.post('/api/forum/user/login', param)

        console.log(res)
        if (res.success !== true) {
          this.$message.error(res.message)
          await this.getVerifyCode()
        } else {
          this.$message.success('登录成功')
          // 1. 将登录成功之后的 token，保存到客户端的 sessionStorage 中
          //   1.1 项目中出了登录之外的其他API接口，必须在登录之后才能访问
          //   1.2 token 只应在当前网站打开期间生效，所以将 token 保存在 sessionStorage 中
          // console.log(res)
          window.sessionStorage.setItem('token', res.data.token)
          window.sessionStorage.setItem('user', res.data.user.username)
          window.sessionStorage.setItem('userid', res.data.user.currentUserInfo.id)
          window.sessionStorage.setItem('avatar', res.data.user.currentUserInfo.avatar)
          window.sessionStorage.setItem('role', res.data.user.currentUserInfo.role)
          // 2. 通过编程式导航跳转到后台主页
          if (res.data.user.currentUserInfo.role === 'user') {
            await this.$router.push('/home')
          } else if (res.data.user.currentUserInfo.role === 'admin') {
            await this.$router.push('/Welcome')
          }
          // // 刷新页面，删除vuex数据
          setTimeout(() => {
            window.location.reload()
          }, 100)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .banner-title {
    animation: title-scale 1s;
    position: absolute;
    top: 25rem;
    padding: 0 0.5rem;
    width: 100%;
    font-size: 2.5rem;
    text-align: center;
    color: #eee;
  }
  .archive-banner {
    height: 110vh;
    // background: url(https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mccYPEGHYJF8vf05Y7Jp3Sq4PYCDwfPyvkq4c5VlhffPJbHw4QoE1dsiS8OtN2H5XvhPtg1C1JZwAOMeqYFSoGDg!/r) center center /
    // cover no-repeat;
    background: url('../assets/images/loginbg.png') center center /
    cover no-repeat;
    background-color: #49b1f5;
  }
  .login_container {
    background-color: #2b4b6b;
    height: 100%;
  }
  .login_box {
    width: 450px;
    height: 360px;
    background-color: #fff;
    border-radius: 15px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);

    .avatar_box {
      height: 130px;
      width: 130px;
      border: 1px solid #eee;
      border-radius: 50%;
      padding: 10px;
      box-shadow: 0 0 10px #ddd;
      position: absolute;
      left: 50%;
      transform: translate(-50%, -50%);
      background-color: #fff;
      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #eee;
      }
    }
  }

  .login_form {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
  }

  .btns {
    display: flex;
    justify-content: flex-end;
  }
  .avatar-uploader .el-upload {
    border: 5px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 60px;
    height: 60px;
    display: block;
  }
</style>
