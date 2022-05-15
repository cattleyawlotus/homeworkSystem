<template>
  <div id="navClass" class="navClass" hide-on-scroll flat height="58">
    <div class="d-md-block d-none nav-container">
      <div class="float-left blog-title">
      </div>
      <div class="float-right nav-title">
        <div class="menus-item">
          <router-link to="/home">
            <i class="iconfont2 iconzhuye" style="color: whitesmoke" /><span style="color: whitesmoke;margin-right: 10px">首页</span>
          </router-link>
        </div>
        <div class="user-btn">
          <a>
            <i class="el-icon-s-operation" style="color: whitesmoke"/><span style="color: whitesmoke;margin-right: 10px">类别</span>
            <ul class="user-submenu">
              <li>
                <router-link to="/types">
                  <i class="iconfont2 iconfenlei"/> 分类
                </router-link>
              </li>
              <li>
                <router-link to="/tags">
                  <i class="iconfont2 iconbiaoqian"/> 标签
                </router-link>
              </li>
            </ul>
          </a>
        </div>
        <div class="user-btn">
          <a>
            <i class="el-icon-help" style="color: whitesmoke"/><span style="color: whitesmoke;margin-right: 10px">功能</span>
            <ul class="user-submenu">
              <li>
                <router-link to="/">
                  <i class="el-icon-magic-stick"/>班级
                </router-link>
              </li>
              <li>
                <router-link to="/">
                  <i class="el-icon-service"/> 作业
                </router-link>
              </li>
            </ul>
          </a>
        </div>
           <div class="menus-item">
          <router-link to="/message">
            <i class="iconfont2 iconpinglunzu" style="color: whitesmoke"/> <span style="color: whitesmoke;margin-right: 10px">留言</span>
          </router-link>
        </div>
        <div class="user-btn">
          <a v-if="loginFlag===false">
            <router-link to="/login">
            <i class="iconfont2 icondenglu" style="color: whitesmoke"/><span style="color: whitesmoke;margin-right: 10px">登录</span>
            </router-link>
          </a>
          <template v-else>
            <img
              class="user-avatar"
              v-bind:src=this.avatar
              height="30"
              width="30"
            />
            <ul class="user-submenu">
              <li>
                <router-link to="/info">
                  <i class="iconfont2 icongerenzhongxin" /> 个人中心
                </router-link>
              </li>
              <li>
                <router-link to="/archives">
                  <i class="el-icon-notebook-2"/> 个人发帖
                </router-link>
              </li>
              <li>
                <router-link to="/collects">
                  <i class="el-icon-collection-tag"/> 个人收藏
                </router-link>
              </li>
              <li>
                <router-link to="/postblogs">
                  <i class="el-icon-edit" /> 发布博客
                </router-link>
              </li>
                <!-- <li>
                 <router-link to="/notify">
                  <i class="el-icon-collection-tag"/>
                    <el-badge value=this.infocount>
                      消息通知
                    </el-badge>
                  </router-link>
              </li> -->
              <li>
                <a @click="logout"><i class="iconfont2 icontuichu" /> 退出</a>
              </li>
            </ul>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Notification } from 'element-ui'
export default {
  components: {
  },
  created () {
    this.getUser()
  },
  mounted () {
    window.addEventListener('scroll', this.scroll)
  },
  data () {
    return {
      navClass: '',
      avatar: '',
      user: {},
      queryString: '',
      searchFlag: false,
      loginFlag: false,
      infocount: 0
    }
  },
  methods: {
    toLogin () {
      const tokenStr = window.sessionStorage.getItem('token')
      // 后端指定接口验证了token的正确性
      if (!tokenStr) {
        this.$confirm('登录后才能开启聊天室，请问是否先登录？', '提示', { // 确认框
          type: 'info'
        }).then(() => {
          this.$router.push('/login')
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '你选择不登录'
          })
          return false
        })
      }
      return !!tokenStr
    },
    closeSearch () {
      this.searchFlag = false
    },
    search () {
      // this.$refs.searchModel.setDialogVisible()
      // this.searchFlag = true
      // if (this.queryString !== '') {
      //   sessionStorage.setItem('queryString', this.queryString)
      //   this.queryString = ''
      //   this.searchFlag = false
      //   if (this.$route.path === '/home') { window.location.reload() } else {
      //     this.$router.push('/home')
      //   }
      // }
    },
    scroll () {
      const that = this
      const scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop
      that.scrollTop = scrollTop
      if (that.scrollTop > 60) {
        that.navClass = 'nav-fixed'
      } else {
        that.navClass = 'nav'
      }
    },
    logout () {
      this.$http.get('/api/logout')
      this.loginFlag = false
      // window.sessionStorage.clear()
      this.$router.push('/login')
      // 刷新页面，删除vuex数据
      // window.location.reload()
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.nickname = window.sessionStorage.getItem('user')
        this.avatar = window.sessionStorage.getItem('avatar')
        this.loginFlag = true
        this.$setWs.initWebSocket()
      }
    },
    message (message) {
      Notification({
        title: '消息通知',
        message: message,
        duration: 0
      })
    }
  },
  computed: {
    getm () {
      return this.$store.state.msg
    }
  },
  watch: {
    getm: function (data, val) {
      if (data !== '连接成功') {
        this.message(data)
      }
    }
  }
}
</script>

<style scoped>
  .navClass{
    /*position:fixed; !* 绝对定位，fixed是相对于浏览器窗口定位。 *!*/
    position: relative;
    top:15px; /* 距离窗口顶部距离 */
    right: 22px;
    height:10px; /* 高度 */
    float: right;
    z-index:99; /* 层叠顺序，数值越大就越高。页面滚动的时候就不会被其他内容所遮挡。 */
  }
  i {
    margin-right: 4px;
  }
  ul {
    list-style: none;
  }
  .nav {
    background: rgba(0, 0, 0, 0) !important;
  }
  .nav a {
    color: #eee !important;
  }
  .nav .menus-item a {
    text-shadow: 0.05rem 0.05rem 0.1rem rgba(0, 0, 0, 0.3);
  }
  .nav .blog-title a {
    text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
  }
  .theme--light.nav-fixed {
    background: rgba(255, 255, 255, 0.8) !important;
    box-shadow: 0 5px 6px -5px rgba(133, 133, 133, 0.6);
  }
  .theme--dark.nav-fixed {
    background: rgba(18, 18, 18, 0.8) !important;
  }
  .theme--dark.nav-fixed a {
    color: rgba(255, 255, 255, 0.8) !important;
  }
  .theme--light.nav-fixed a {
    color: #4c4948 !important;
  }
  .nav-fixed .menus-item a,
  .nav-fixed .menus-btn a,
  .nav-fixed .blog-title a {
    text-shadow: none;
  }
  .nav-container {
    font-size: 14px;
    width: 100%;
    height: 100%;
  }
  .nav-mobile-container {
    width: 100%;
    display: flex;
    align-items: center;
  }
  .blog-title,
  .nav-title {
    display: flex;
    align-items: center;
    height: 100%;
  }
  .blog-title a {
    font-size: 18px;
    font-weight: bold;
  }
  .user-btn,
  .menus-btn,
  .menus-item {
    position: relative;
    display: inline-block;
    margin: 0 0 0 0.875rem;
  }
  .menus-btn a,
  .menus-item a {
    transition: all 0.2s;
  }
  .nav-fixed .menus-btn a:hover,
  .nav-fixed .menus-item a:hover {
    color: #49b1f5 !important;
  }
  .menus-item a:hover:after {
    width: 100%;
  }
  .menus-item a:after {
    position: absolute;
    bottom: -5px;
    left: 0;
    z-index: -1;
    width: 0;
    height: 3px;
    background-color: #80c8f8;
    content: "";
    transition: all 0.3s ease-in-out;
  }
  .user-btn a {
    transition: all 0.2s;
  }
  .user-avatar {
    cursor: pointer;
    border-radius: 50%;
  }
  .user-btn:hover .user-submenu {
    display: block;
  }
  .user-submenu {
    position: absolute;
    display: none;
    right: 0;
    width: max-content;
    margin-top: 8px;
    box-shadow: 0 5px 20px -4px rgba(0, 0, 0, 0.5);
    background-color: #fff;
    animation: submenu 0.3s 0.1s ease both;
  }
  .user-submenu:before {
    position: absolute;
    top: -8px;
    left: 0;
    width: 100%;
    height: 20px;
    content: "";
  }
  .user-submenu a {
    line-height: 2;
    color: #4c4948 !important;
    text-shadow: none;
    display: block;
    padding: 6px 14px;
  }
  .user-submenu a:hover {
    background: #4ab1f4;
  }
  @keyframes submenu {
    0% {
      opacity: 0;
      filter: alpha(opacity=0);
      transform: translateY(10px);
    }
    100% {
      opacity: 1;
      filter: none;
      transform: translateY(0);
    }
  }
</style>
