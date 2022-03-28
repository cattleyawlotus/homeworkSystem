<template>
  <div id="app">
    <div class="content-header">
      <h1>博客管理<small>评论管理</small></h1>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">后台管理</el-breadcrumb-item>
        <el-breadcrumb-item>博客管理</el-breadcrumb-item>
        <el-breadcrumb-item>评论管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--中间内容-->
    <div class="app-container">
      <div class="box">
        <div class="filter-container">
          <el-input placeholder="请输入评论关键字" v-model="pagination.queryString" style="width: 200px;"
                    class="filter-item" clearable @clear="findPage"></el-input>
          <!-- <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.label"
              :value="item.value"> -->
            <!-- </el-option> -->
          <!-- </el-select> -->
          <el-button @click="findPage()" icon="el-icon-search" class="dalfBut">查询评论</el-button>
          <!-- <el-button type="primary" class="butT" @click="handleCreate">添加评论</el-button> -->
        </div>
        <div class="ui container">
        <el-table size="middle" current-row-key="id" :data="dataList" stripe highlight-current-row>
          <!-- <el-table-column prop="id" min-width="15px" align="center" label="序号"></el-table-column> -->
          <el-table-column prop="user" label="发布者" align="center"></el-table-column>
          <el-table-column prop="blogId" label="博客id" align="center"></el-table-column>
          <el-table-column prop="parentCommentId" label="父评论" align="center"></el-table-column>
          <el-table-column prop="content" label="内容" align="center"></el-table-column>
          <el-table-column label="管理" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button>
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
        queryString: null
      }, // 查询条件
      // formData: {}, // 表单数据
      dataList: [], // 当前页要展示的分页列表数据
      user: '',
      uid: ''
    }
  },
  created () {
    this.getUser()
    this.findPage()
  },
  methods: {
    async findPage () {
      const { data: res } = await this.$http.get(`/api/forum/manager/getcomment?currPage=${this.pagination.currentPage}&pageSize= ${this.pagination.pageSize}`)
      if (!res.success) {
        return this.$message.error('获取评论列表失败！')
      }
      this.pagination.total = res.data.totalCount
      this.dataList = res.data.list
    },
    handleDelete (row) {
      this.$http.delete(`/api/forum/manager/deletecomment?cid=${row.id}`).then((res) => {
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
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.uid = this.user = window.sessionStorage.getItem('userid')
      }
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../../assets/css/me.css";
  @import "../../../assets/css/style.css";
</style>
