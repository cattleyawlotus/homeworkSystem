<template>
  <div class="blogs">
    <div class="content-header">
      <h1>用户管理<small>账号管理</small></h1>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">后台管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        <el-breadcrumb-item>账号管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--中间内容-->
    <div class="app-container">
      <div class="box">
        <div class="filter-container">
          <el-input placeholder="请输入用户名" v-model="pagination.queryString" style="width: 200px;"
                    class="filter-item" clearable @clear="findPage"></el-input>
          <el-button @click="findPage()" icon="el-icon-search" class="dalfBut">查询用户</el-button>
          <el-button type="primary" class="butT" @click="handleCreate">添加用户</el-button>
        </div>
        <div class="ui container">
        <el-table size="middle" current-row-key="id" :data="dataList" stripe highlight-current-row>
          <!-- <el-table-column prop="id" min-width="15px" align="center" label="序号"></el-table-column> -->
          <el-table-column prop="username" label="用户名" align="center"></el-table-column>
          <el-table-column prop="role" label="角色" align="center"></el-table-column>
          <el-table-column label="管理" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              <!-- <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button> -->
              <!-- <el-button size="mini" type="info" @click="handleForbidden(scope.row)">禁用</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
            class="pagiantion"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-sizes="[2,5,10,15]"
            :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total">
          </el-pagination>
        </div>
        <!-- 新增用户弹层-->
        <div class="add-form">
          <el-dialog title="新增用户" :visible.sync="dialogFormVisible">
            <el-form :model="formData" :rules="userRules" ref="userForm" label-width="100px">
            <el-row>
              <el-col :span="8">
                <el-form-item prop="username" label="用户名">
                  <el-input v-model="formData.username"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item prop="password" label="密码">
                  <el-input v-model="formData.password"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item prop="nickname" label="昵称">
                  <el-input v-model="formData.nickname"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item prop="email" label="邮箱">
                  <el-input v-model="formData.email"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item prop="role" label="角色">
                   <el-select v-model="formData.role" placeholder="请选择">
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item prop="avatar" label="头像地址">
              <div style="height: 100px">
              <el-input style="width: 78%" v-model="formData.avatar"></el-input>
              <el-upload
                style="margin-left: 80%!important;"
                class="avatar-uploader"
                action="serverApi/oss/userAvatar/"
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
              </div>
            </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="handleAdd()">确定</el-button>
            </div>
          </el-dialog>
        </div>
      </div>
      </div>
    </div>
    <br>
    <br>
  </div>
</template>

<script>
export default {
  data () {
    return {
      options: [{
        value: 'user',
        label: '用户'
      }, {
        value: 'admin',
        label: '管理员'
      }],
      pagination: { // 分页相关模型数据
        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示的记录数
        total: 0, // 总记录数
        queryString: ''
      }, // 查询条件
      // formData: {}, // 表单数据
      dialogFormVisible: false, // 增加表单是否可见
      dialogFormVisible4Edit: false, // 编辑表单是否可见
      dataList: [], // 当前页要展示的分页列表数据
      value: '',
      imageUrl: '',
      formData: {
        uid: '',
        username: '',
        password: '',
        nickname: '',
        email: '',
        avatar: '',
        role: ''
      },
      userRules: {
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
        nickname: [
          // { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        avatar: [
          // { required: true, message: '请输入头像地址', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        email: [
          // { required: true, message: '请输入头像地址', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.getUser()
    this.findPage()
  },
  methods: {
    async findPage () {
      const { data: res } = await this.$http.get(`/api/forum/manager/queryuser?currPage=${this.pagination.currentPage}&pageSize= ${this.pagination.pageSize}&userName=${this.pagination.queryString}`)
      if (!res.success) {
        return this.$message.error('获取用户列表失败！')
      }
      this.pagination.total = res.data.totalCount
      this.dataList = res.data.list
    },
    handleAdd () {
      // 进行表单校验
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          // 表单校验通过，发ajax请求，把数据录入至后台处理
          this.$http.post('/api/forum/manager/adduser', this.formData).then((res) => {
            // 关闭新增窗口
            console.log(res)
            this.dialogFormVisible = false
            if (res.data.success) {
              // 新增成功后，重新调用分页查询方法，查询出最新的数据
              this.findPage()
              // 弹出提示信息
              this.$message({
                message: '添加成功',
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
    handleDelete (row) {
      this.$http.delete(`/api/forum/manager/deleteuser?id=${row.id}`).then((res) => {
        if (res.data.success) {
          this.findPage()
          this.$message({
            message: '删除成功',
            type: 'success'
          })
        } else { // 执行失败
          this.$message.error(res.data.message)
        }
      })
    },
    handleUpdate (row) {
      this.$message.info('对不起暂且没有此功能')
    },
    // 切换页码
    handleCurrentChange (currentPage) {
      // 设置最新的页码
      this.pagination.currentPage = currentPage
      // 重新调用findPage方法进行分页查询
      this.findPage()
    },
    handleSizeChange (newSize) {
      this.pagination.pageSize = newSize
      this.findPage()
    },
    masterFileMax (files, fileList) {
      console.log(files, fileList)
      this.$message.warning('请最多上传一张图片')
    },
    async uploadPic (param) {
      var fileObj = param.file
      var form = new FormData()
      // 文件对象
      form.append('file', fileObj)
      const { data: res } = await this.$http.post('/serverApi/oss/userAvatar', form)
      if (res.flag) {
        // 弹出提示信息
        this.$message.success('上传头像成功')
        this.formData.avatar = res.data.url
        console.log(res.data.url)
      } else { // 执行失败
        this.$message.error(res.message)
      }
    },
    handleAvatarSuccess (res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
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
    resetForm () {
      this.formData = {}// 重置表格数据
      if (this.$refs.dataAddForm !== undefined) {
        this.$refs.userForm.resetFields()
      }
    },
    handleCreate () {
      this.dialogFormVisible = true
      this.resetForm()
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.nickname = this.user = window.sessionStorage.getItem('user')
        this.avatar = this.user = window.sessionStorage.getItem('avatar')
      }
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../../assets/css/me.css";
  @import "../../../assets/css/style.css";
</style>
