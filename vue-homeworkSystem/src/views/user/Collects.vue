<template>
  <div class="archives">
    <!-- banner -->
    <div class="archives-banner banner">
      <!-- <div class="banner-container"> -->
        <h1 class="banner-title">
          个人收藏
        </h1>
      <!-- </div> -->
    </div>
    <div class="m-archives">
      <!-- 归档列表 -->
      <el-card class="blog-container">
        <timeline>
          <timeline-title bg-color="green"> <h4>目前共计{{ pagination.total}}篇文章</h4> </timeline-title>
          <timeline-item bg-color="#9dd8e0" v-for="item of archiveList" :key="item.blogId">
            <!-- 日期 -->
            <span class="time">{{dateFormat(item.createTime)}}</span>
            <!-- 文章标题 -->
              <div class="archives-title mt-4 mb-4" style="cursor:pointer !important;" @click="toBlog(item.id)">{{ item.title }}</div>
          </timeline-item>
        </timeline>
      <!--footer-->
      <div class="ui bottom attached segment my-blog-shadow">
        <div class="pagination-container">
          <el-pagination
            class="pagiantion"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-sizes="[2,6,10,15]"
            :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total">
          </el-pagination>
        </div>
      </div>
      </el-card>
    </div>
    <br>
    <br>
  </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from 'vue-cute-timeline'
export default {
  components: {
    Timeline,
    TimelineItem,
    TimelineTitle
  },
  data () {
    return {
      uid: '',
      total: '',
      archiveList: [
        {
          blogId: 1,
          createTime: '2010-11-01',
          title: '啊啊'
        }
      ],
      pagination: { // 分页相关模型数据
        currPage: 1, // 当前页码
        pageSize: 6, // 每页显示的记录数
        total: 0, // 总记录数
        queryString: null // 查询条件
      }
    }
  },
  created () {
    this.getUser()
    this.getArchiveList()
  },
  methods: {
    toBlog (blogId) {
      sessionStorage.setItem('blogId', blogId)
      this.$router.push('/blog')
    },
    async getArchiveList () {
      const { data: res } = await this.$http.get('/api/hs/collects/getcollectedblog?currPage=' + this.pagination.currPage + '&pageSize=' + this.pagination.pageSize + '&uid=' + this.uid)
      console.log(res)
      if (!res.success) {
        return this.$message.error(res.message)
      }
      this.pagination.total = res.data.totalCount
      this.archiveList = res.data.list
    },
    scrollDown () {
      window.scrollTo({
        behavior: 'smooth',
        top: document.documentElement.clientHeight
      })
    },
    handleCurrentChange (currentPage) {
      // 设置最新的页码
      this.pagination.currPage = currentPage
      // 重新调用findPage方法进行分页查询
      this.getArchiveList()
      this.scrollDown()
    },
    handleSizeChange (newSize) {
      this.pagination.pageSize = newSize
      this.getArchiveList()
      this.scrollDown()
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      // console.log(this.user)
      if (this.user != null) {
        this.uid = window.sessionStorage.getItem('userid')
      }
    },
    dateFormat: function (time) {
      var date = new Date(time)
      var year = date.getFullYear()
      var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      // 拼接
      return year + '年' + month + '月' + day + '日 ' + hours + ':' + minutes + ':' + seconds
    }
  }
}
</script>

<style scoped>
  .link-scroll-down {
    cursor: pointer;
    position: absolute;
    bottom: 0 !important;
    width: 100%;
  }
  .m-archives {
    padding-top: 27vh !important;
    padding-bottom: 0px !important;
  }
  .archives-title {
    color: #344c67;
    font-size: 21px;
    font-weight: bold;
    line-height: 2;
  }
  .archives-banner {
    height:  67vh;
    background: url('../../assets/images/collectsbanner.jpg') center center /
    cover no-repeat;
    background-color: #49b1f5;
  }
  .blog-container {
    background: #fff;
    color: #4c4948;
    border-radius: 8px;
    box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);
    transition: all 0.3s;
  }
  .blog-container:hover {
    box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
  }
  .link-wrapper:hover a {
    color: #fff;
  }
  .link-wrapper:hover .link-intro {
    color: #fff;
  }
  .link-wrapper:hover .link-avatar {
    transform: rotate(360deg);
  }
  .link-wrapper a {
    color: #333;
    text-decoration: none;
    display: flex;
    height: 100%;
    width: 100%;
  }
</style>
