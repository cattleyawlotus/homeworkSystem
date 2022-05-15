<template>
  <div class="blogs">
    <div class="content-header">
      <h1>留言管理<small>留言管理</small></h1>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">后台管理</el-breadcrumb-item>
        <el-breadcrumb-item>留言管理</el-breadcrumb-item>
        <el-breadcrumb-item>留言管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--中间内容-->
    <div class="app-container">
      <div class="box">
        <div class="filter-container">
          <el-input placeholder="请输入内容" v-model="pagination.queryString" style="width: 200px;"
                    class="filter-item" clearable @clear="findPage"></el-input>
          <!-- <el-select v-model="value" placeholder="请选择"> -->
            <!-- <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.label"
              :value="item.value">
            </el-option> -->
          <!-- </el-select> -->
          <el-button @click="findPage()" icon="el-icon-search" class="dalfBut">查询留言</el-button>
          <el-button type="primary" class="butT" @click="handleCreate">添加留言</el-button>
        </div>
      <div class="ui container">
        <el-table size="middle" current-row-key="id" :data="dataList" stripe highlight-current-row>
          <!-- <el-table-column prop="id" min-width="15px" align="center" label="序号"></el-table-column> -->
          <el-table-column prop="nickname" label="用户" align="center"></el-table-column>
          <el-table-column prop="time" label="过屏时间" align="center"></el-table-column>
          <el-table-column prop="messageContent" label="留言" align="center"></el-table-column>
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
        <!-- 新增标签弹层-->
        <div class="add-form">
          <el-dialog title="新增留言" :visible.sync="dialogFormVisible">
            <el-form ref="dataAddForm" :model="formData"  label-position="right" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="内容" prop="content">
                    <el-input v-model="formData.content"/>
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
      uid: '1'
    }
  },
  created () {
    this.findPage()
    this.getUser()
  },
  methods: {
    // 分页查询
    async findPage () {
      const { data: res } = await this.$http.get(`/api/forum/manager/meaasge?currPage=${this.pagination.currentPage}&pageSize= ${this.pagination.pageSize}&message=${this.pagination.queryString}`)
      if (!res.success) {
        return this.$message.error('获取标签列表失败！')
      }
      this.pagination.total = res.data.totalCount
      this.dataList = res.data.list
      console.log(res)
    },
    // 添加按钮
    handleAdd () {
      this.$http.post(`/api/forum/message/create?message=${this.formData.content}&uid=${this.uid}`).then(res => {
        if (res.data.success) {
          this.$message.success('新增留言成功')
          this.findPage()
        } else {
          this.$message.error('留言失败')
        }
      })
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.nickname = window.sessionStorage.getItem('user')
        this.avatar = window.sessionStorage.getItem('avatar')
        this.uid = window.sessionStorage.getItem('userid')
      }
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
      // this.$refs.dataAddForm.resetFields()
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
      this.$http.delete(`/api/forum/manager/deletemeaasge?id=${row.id}`).then((res) => {
        console.log(res)
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
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../../assets/css/me.css";
  @import "../../../assets/css/style.css";
</style>
