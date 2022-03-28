<template>
  <div class="blogs">
    <div class="content-header">
      <h1>博客管理<small>查看博客</small></h1>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">后台管理</el-breadcrumb-item>
        <el-breadcrumb-item>博客管理</el-breadcrumb-item>
        <el-breadcrumb-item>查看博客</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!--中间内容-->
    <div class="app-container">
    <div class="box">
<!--    <div  class="m-container-small m-padded-tb-big">-->
      <div class="filter-container">
        <el-input placeholder="请输入标题" v-model="pagination.queryString" style="width: 200px;"
                  class="filter-item" clearable @clear="findPage"></el-input>
        <!-- <el-select v-model="value" placeholder="请选择"> -->
          <!-- <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option> -->
        <!-- </el-select> -->
        <el-button @click="findPage()" icon="el-icon-search" class="dalfBut">查询博客</el-button>
        <el-button type="primary" class="butT" @click="pushPostBlogs">添加博客</el-button>
      </div>
      <div class="ui container">
        <el-table size="middle" current-row-key="id" :data="dataList" stripe highlight-current-row>
          <!-- <el-table-column type="index" min-width="15px" align="center" label="序号"></el-table-column> -->
          <!-- <el-table-column prop="id" label="序号" align="center"></el-table-column> -->
          <el-table-column prop="title" label="标题" align="center"></el-table-column>
          <el-table-column prop="type" label="类型" align="center"></el-table-column>
          <el-table-column prop="recommend" label="推荐"  align="center" >
            <template slot-scope="scope">
              <el-switch v-model="scope.row.recommend"
                         @change="recommendStateChanged(scope.row)">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="published" label="状态" align="center" :formatter="formatBoolean"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间" align="center"></el-table-column>
          <el-table-column label="管理" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button>
              <!--              <el-button size="mini" type="info" @click="handleForbidden(scope.row)">禁用</el-button>-->
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
        <!-- 新增标签弹层 手机号 真实姓名（*） 密码 备注 角色-->
        <div class="add-form">
          <el-dialog title="新增分类" :visible.sync="dialogFormVisible">
            <el-form ref="dataAddForm" :model="formData" :rules="rules" label-position="right"
                     label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="分类编号" prop="typeId">
                    <el-input v-model="formData.typeId"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="分类名" prop="name">
                    <el-input v-model="formData.name"/>
                  </el-form-item>
                </el-col>
              </el-row>
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
      value: {},
      options: [
        { label: '个人编写的博客数较少，本查询功能接口暂未实现' }
      ],
      pagination: { // 分页相关模型数据
        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示的记录数
        total: 0, // 总记录数
        queryString: null // 查询条件
      },
      formData: {}, // 表单数据
      dialogFormVisible: false, // 增加表单是否可见
      dialogFormVisible4Edit: false, // 编辑表单是否可见
      dataList: [], // 当前页要展示的分页列表数据
      user: {},
      uid: '',
      nickname: '',
      // 被激活的链接地址
      avatar: '',
      rules: { // 校验规则
        // 真实姓名
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.findPage()
    this.getUser()
  },
  methods: {
    formatBoolean: function (row, index) {
      var ret = ''
      if (row.published === true) {
        ret = '已发布'
      } else {
        ret = '草稿'
      }
      return ret
    },
    pushPostBlogs () {
      this.$router.push('/postblogs')
    },
    // 分页查询
    async findPage () {
      // 发送ajax，提交分页请求（页码，每页显示条数，查询条件)
      // const param = {
      //   currentPage: this.pagination.currentPage,
      //   pageSize: this.pagination.pageSize,
      //   queryString: this.pagination.queryString
      // }
      // var param2 = this.$encrypTion(JSON.stringify(param))
      const { data: res } = await this.$http.get('/api/forum/blog/getallblog?currPage=' + this.pagination.currentPage + '&pageSize=' + this.pagination.pageSize)
      // 解析controller响应回的数据
      // console.log(res)
      if (!res.success) {
        return this.$message.error('获取博客列表失败!')
      }
      this.pagination.total = res.data.totalCount
      this.dataList = res.data.list
    },
    // 添加按钮
    handleAdd () {
      // 进行表单校验
      this.$refs.dataAddForm.validate((valid) => {
        if (valid) {
          // 表单校验通过，发ajax请求，把数据录入至后台处理
          // const param = this.$encrypTion(JSON.stringify(this.formData))
          this.$http.post('/api/types2/add', this.formData).then((res) => {
            // 关闭新增窗口
            this.dialogFormVisible = false
            if (res.data.flag) {
              // 新增成功后，重新调用分页查询方法，查询出最新的数据
              this.findPage()
              // 弹出提示信息
              this.$message({
                message: '添加成功',
                type: 'success'
              })
            } else { // 执行失败
              this.$message.error(res.data.code)
            }
          })
        } else { // 校验不通过
          this.$message.error('校验失败，请检查输入格式')
          return false
        }
      })
    },
    // 监听 switch 开关状态的改变
    async recommendStateChanged (row) {
      // console.log(row)
      // console.log(row.recommend)
      this.$http.put('/api/forum/blog/updateblog', row).then((res) => {
        console.log(res)
        if (res.data.success) {
          // 弹出提示信息
          if (row.recommend) {
            this.$message({
              message: '推荐成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '取消推荐',
              type: 'success'
            })
          }
        } else { // 执行失败
          this.$message.error('推荐失败')
        }
      })
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.nickname = this.user = window.sessionStorage.getItem('user')
        this.avatar = this.user = window.sessionStorage.getItem('avatar')
        this.uid = window.sessionStorage.getItem('userid')
      }
    },
    logout () {
      window.sessionStorage.clear()
      this.$router.push('/login')
      // 刷新页面，删除vuex数据
      window.location.reload()
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
    // 重置表单
    resetForm () {
      this.formData = {}// 重置表格数据
      this.$refs.dataAddForm.resetFields()
    },
    // 弹出添加窗口
    handleCreate () {
      this.dialogFormVisible = true
      this.resetForm()
    },
    handleDelete (row) {
      this.$http.delete(`/api/forum/blog/deleteblog?bid=${row.id}&uid=${this.uid}`).then((res) => {
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
    handleUpdate () {
      // this.$router.push('/postblogs')
      this.$message.info('对不起，暂且没有此功能')
    }
  },
  mounted () {
    $('.ui.dropdown').dropdown({
      on: 'hover'
    })
    $('.menu.toggle').click(function () {
      $('.m-item').toggleClass('m-mobile-hide')
    })
  }
}
</script>

<style lang="less" scoped>
  @import "../../../assets/css/style.css";
  @import "../../../assets/css/me.css";
  .blogs {
    height: 100%;
    margin: 0;
    padding: 0;
  }
</style>
