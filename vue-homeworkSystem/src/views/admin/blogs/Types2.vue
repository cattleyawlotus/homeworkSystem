<template>
  <div class="blogs">
    <div class="content-header">
      <h1>博客管理<small>分类管理</small></h1>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">后台管理</el-breadcrumb-item>
        <el-breadcrumb-item>博客管理</el-breadcrumb-item>
        <el-breadcrumb-item>分类管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!--中间内容-->
    <div class="app-container">
      <div class="box">
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
          <el-button @click="findPage()" icon="el-icon-search" class="dalfBut">查询分类</el-button>
          <el-button type="primary" class="butT" @click="handleCreate">添加分类</el-button>
        </div>
      <div class="ui container">
        <el-table size="middle" current-row-key="id" :data="dataList" stripe highlight-current-row>
          <!-- <el-table-column prop="id" min-width="15px" align="center" label="序号"></el-table-column> -->
          <el-table-column prop="typeName" label="分类名称" align="center"></el-table-column>
          <el-table-column label="管理" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              <!-- <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button> -->
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
                  <el-form-item label="类型名" prop="typeName">
                    <el-input v-model="formData.typeName"/>
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
    <br>
    <br>
  </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      pagination: { // 分页相关模型数据
        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示的记录数
        total: 0, // 总记录数
        queryString: '' // 查询条件
      },
      formData: {}, // 表单数据
      dialogFormVisible: false, // 增加表单是否可见
      dialogFormVisible4Edit: false, // 编辑表单是否可见
      dataList: [], // 当前页要展示的分页列表数据
      user: {},
      nickname: '',
      // 被激活的链接地址
      avatar: '',
      rules: { // 校验规则
        // 分类名称
        typeName: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.findPage()
  },
  methods: {
    // 分页查询
    async findPage () {
      // 发送ajax，提交分页请求（页码，每页显示条数，查询条件)
      // const param = {
      //   currentPage: this.pagination.currentPage,
      //   pageSize: this.pagination.pageSize,
      //   queryString: this.pagination.queryString
      // }
      // var param2 = this.$encrypTion(JSON.stringify(param))
      const { data: res } = await this.$http.get(`/api/hs/manager/querytype?currPage=${this.pagination.currentPage}&pageSize= ${this.pagination.pageSize}&typeName=${this.pagination.queryString}`)
      if (!res.success) {
        return this.$message.error('获取类型列表失败！')
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
          this.$http.post(`/api/hs/manager/addtype?typeName=${this.formData.typeName}`).then((res) => {
            // 关闭新增窗口
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
    resetForm () {
      this.formData = {}// 重置表格数据
      if (this.$refs.dataAddForm !== undefined) {
        this.$refs.dataAddForm.resetFields()
      }
    },
    // 弹出添加窗口
    handleCreate () {
      this.dialogFormVisible = true
      this.resetForm()
    },
    handleDelete (row) {
      this.$http.delete(`/api/hs/manager/deletetype?id=${row.id}`).then((res) => {
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
  @import "../../../assets/css/me.css";
  @import "../../../assets/css/style.css";
</style>
