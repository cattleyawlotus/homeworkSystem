<template>
  <div class="home">
    <div class="blog-banner banner">
      <h1 class="banner-title">博客详情</h1>
    </div>
    <!--中间内容-->
    <div id="waypoint" class="m-container-small m-blog animated fadeInLeft">
      <div class="ui container">
        <div style="width: 1000px;">
          <div class="ui top attached segment" >
            <div class="ui horizontal link list">
              <div class="item">
                <img v-bind:src="dataList.avatar" class="ui avatar image">
                <div class="content"><a class="header">{{dataList.username}}</a></div>
              </div>
              <div class="item">
                <i class="calendar icon"></i> {{dateFormat(dataList.createTime)}}
              </div>
              <div class="item">
                <i class="eye icon"></i> {{dataList.views}}
              </div>
              <div class="item">
                <i class="thumbs up outline icon"></i> {{dataList.thumbs}}
              </div>
              <div class="item">
                <i class="el-icon-star-off"></i> {{dataList.collects}}
              </div>
            </div>
          </div>
          <div class="ui attached segment" align="center">
            <!--图片区域-->
            <img v-bind:src=dataList.firstPicture class="ui fluid rounded image" style="width: 200px">
          </div>
          <div class="ui  attached padded segment">
            <!--内容-->
            <div class="ui right aligned basic segment">
              <div class="ui orange basic label">{{dataList.shareStatement}}</div>
            </div>
            <h2 class="ui center aligned header" v-text="dataList.title"></h2>
            <br>
            <!-- <div id="content" class="typo  typo-selection js-toc-content m-padded-lr-responsive m-padded-tb-large" v-html="dataList.content" style="width: 800px">
            </div> -->
            <mavon-editor
              :value="dataList.content"
              defaultOpen="preview"
              :boxShadow="false"
              style="z-index:1;height:50vh"
              :editable="false"
              :subfield="false"
              :toolbarsFlag="false">
            </mavon-editor>

            <!-- 标签
            <div class="m-padded-lr-responsive">
            <div class="ui basic teal left pointing label" v-for="item in tagList" :key="item.tagId">{{item.tagName}}</div>
            </div> -->

            <!--赞赏-->
            <div class="ui center aligned basic segment">
              <button id="payButton" class="ui orange basic circular button">赞赏</button>
            </div>
            <div class="ui payQR flowing popup transition hidden">
              <div class="ui orange basic label">
                <div class="ui images" style="font-size: inherit !important;">
                  <div class="image">
                    <img src="#" alt="" class="ui rounded bordered image" style="width: 120px">
                    <div>支付宝</div>
                  </div>
                  <div class="image">
                    <img src="" alt="" class="ui rounded bordered image" style="width: 120px">
                    <div>微信</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="ui attached positive message">
            <!--博客信息-->
            <div class="ui middle aligned grid">
              <div class="eleven wide column">
                <ui class="list">
                  <li>作者：{{dataList.username}}（联系作者）</li>
                  <li>发表时间：{{dataList.createTime}}</li>
                  <li>版权声明：自由转载-非商用-非衍生-保持署名（创意共享3.0许可证）</li>
                  <li>公众号转载：请在文末添加作者公众号二维码</li>
                </ui>
              </div>
              <div class="five wide column">
                <img src="../../assets/images/wechat.jpg" alt="" class="ui right floated rounded bordered image" style="width: 110px">
              </div>
            </div>
          </div>
          <!--评论区-->
          <div id="comment-container" class="ui bottom attached segment">
            <div class="ui blue segment">
              <div class="ui threaded comments" style="max-width: 100%">
                <h3 class="ui dividing header">Comments</h3>
                <!--评论区-->
                <div>
                  <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
                    <el-avatar class="header-img" :size="40" :src="avatar"></el-avatar>
                    <div class="reply-info" >
                      <div tabindex="0" contenteditable="true" id="replyInput" spellcheck="false" placeholder="输入评论..." class="reply-input" @focus="showReplyBtn"   @input="onDivInput($event)">
                      </div>
                    </div>
                    <div class="reply-btn-box" v-show="btnShow">
                      <el-button class="reply-btn" size="medium" @click="sendComment" type="primary">发表评论</el-button>
                    </div>
                  </div>
                  <h3 class="ui dividing header"></h3>
                  <!-- 评论列表 -->
                  <div v-for="(item,i) in comments" :key="i" class="author-title reply-father">
                    <el-avatar class="header-img" :size="40" :src="item.avatar"></el-avatar>
                    <div class="author-info">
                      <span class="author-name">{{item.user}}</span>
                      <span class="author-time">{{dateFormat(item.time)}}</span>
                    </div>
                    <div class="icon-btn">
                      <i class="iconfont el-icon-thumb"></i>{{item.thumbs}}
                      <span @click="showReplyInput(i,item.id)"><i class="iconfont el-icon-s-comment"></i>{{item.commentNum}}</span>
                      <span @click="deleteComment(item)" v-if="item.user===user"><i class="iconfont el-icon-delete-solid"  ></i></span>
                    </div>
                    <div class="talk-box">
                      <p>
                        <span class="reply">{{item.content}}</span>
                      </p>
                    </div>
                    <!-- 子评论 -->
                    <div class="reply-box">
                      <div v-for="(reply,j) in item.reply" :key="j" class="author-title">
                          <el-avatar class="header-img" :size="40" :src="reply.avatar"></el-avatar>
                          <div class="author-info">
                              <span class="author-name">{{reply.user}}</span>
                              <span class="author-time">{{dateFormat(item.time)}}</span>
                          </div>
                          <div class="icon-btn">
                              <i class="iconfont el-icon-thumb"></i>{{reply.thumbs}}
                              <span @click="showReplyInput(i,reply.user,reply.id)"><i class="iconfont el-icon-s-comment"></i>{{reply.commentNum}}</span>
                              <span @click="deleteComment(reply)" v-if="reply.user===user"><i class="iconfont el-icon-delete-solid"  ></i></span>
                          </div>
                          <div class="talk-box">
                              <p>
                                  <span>回复 {{reply.parentName}}:</span>
                                  <span class="reply">{{reply.content}}</span>
                              </p>
                          </div>
                          <div class="reply-box">
                          </div>
                      </div>
                    </div>
                    <div v-show="_inputShow(i)" class="my-reply my-comment-reply">
                      <el-avatar class="header-img" :size="40" :src="avatar"></el-avatar>
                      <div class="reply-info" >
                          <div tabindex="0" contenteditable="true" spellcheck="false" placeholder="输入评论..."   @input="onDivInput($event)"  class="reply-input reply-comment-input"></div>
                      </div>
                      <div class=" reply-btn-box">
                          <el-button class="reply-btn" size="medium" @click="sendCommentReply(i,parentCommentId)" type="primary">发表评论</el-button>
                      </div>
                    </div>
                  </div>
                </div>
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
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 工具栏 -->
    <div id="toolbar" class="m-padded m-fixed m-right-bottom" >
      <div class="ui vertical icon buttons ">
        <button type="button" class="ui toc blue button" >目录</button>
        <a href="#comment-container" class="ui blue button" >留言</a>
        <button class="ui wechat icon button"><i class="weixin icon"></i></button>
        <button class="ui icon button" @click="thumbsUp">
          <i v-if="thumbsFlag" class="thumbs up icon" style="color: red"></i>
          <i v-else class="thumbs up outline icon"></i>
        </button>
        <button class="ui icon button" @click="collect">
          <i v-if="collectFlag" class="star icon" style="color: yellow"></i>
          <i v-else class="star outline icon"></i>
        </button>
        <div id="toTop-button" class="ui icon button" ><i class="chevron up icon"></i></div>
      </div>
    </div>
    <div class="ui toc-container flowing popup transition hidden" style="width: 250px!important;">
      <ol class="js-toc">
      </ol>
    </div>
    <div id="qrcode" class="ui wechat-qr flowing popup transition hidden " style="width: 130px !important;">
<!--      <img src="../assets/images/wechat.jpg" alt="" class="ui rounded image" style="width: 120px !important;">-->
    </div>

    <br>
    <br>
    <Footer></Footer>
  </div>

</template>
<script>
import Prism from '../../assets/lib/prism/prism'
import QRCode from 'qrcodejs2'
import Footer from '../../components/layout/Footer'
const clickoutside = {
  // 初始化指令
  bind (el, binding, vnode) {
    function documentHandler (e) {
    // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
      // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e)
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler
    document.addEventListener('click', documentHandler)
  },
  update () {},
  unbind (el, binding) {
    // 解除事件监听
    document.removeEventListener('click', el.vueClickOutside)
    delete el.vueClickOutside
  }
}

export default {
  // 注册组件
  components: {
    Footer
  },
  data () {
    return {
      pagination: { // 分页相关模型数据
        currPage: 1, // 当前页码
        pageSize: 6, // 每页显示的记录数
        total: 0, // 总记录数
        queryString: null // 查询条件
      },
      formData: {
        blogId: '',
        content: '请输入评论信息...', // 评论内容
        parentCommentId: ''
      },
      uid: '',
      user: '',
      nickname: '',
      ip: '',
      // 被激活的链接地址
      avatar: '',
      dataList: [],
      dataList2: [],
      thumbsFlag: false,
      collectFlag: false,
      // 评论数据
      btnShow: false,
      index: '0',
      replyComment: '',
      // myName: 'Lana Del Rey',
      // myId: 19870621,
      to: '',
      parentCommentId: -1,
      comments: []
    }
  },
  directives: { clickoutside },
  created () {
    this.getUser()
    this.getOneBlog()
    this.getCommentList()
  },
  methods: {
    async thumbsUp () {
      if (this.toLogin()) {
        const blogId = sessionStorage.getItem('blogId')
        if (this.thumbsFlag === false) {
          const { data: res } = await this.$http.post(`/api/hs/thumbs/create?bid=${blogId}&uid=${this.uid}`)
          if (res.success) {
            this.$message.success(res.message)
            this.thumbsFlag = true
          } else {
            this.$message.info(res.message)
          }
        } else {
          const { data: res } = await this.$http.delete(`/api/hs/thumbs/delete?bid=${blogId}&uid=${this.uid}`)
          if (res.success) {
            this.$message.success(res.message)
            this.thumbsFlag = false
          } else {
            this.$message.info(res.message)
          }
        }
      }
    },
    async collect () {
      if (this.toLogin()) {
        const blogId = sessionStorage.getItem('blogId')
        if (this.collectFlag === false) {
          const { data: res } = await this.$http.post(`/api/hs/collects/create?bid=${blogId}&uid=${this.uid}`)
          if (res.success) {
            this.$message.success('收藏成功！')
            this.collectFlag = true
          } else {
            this.$message.info(res.message)
          }
        } else {
          const { data: res } = await this.$http.delete(`/api/hs/collects/delete?bid=${blogId}&uid=${this.uid}`)
          if (res.success) {
            this.$message.success('取消收藏')
            this.collectFlag = false
          } else {
            this.$message.info(res.message)
          }
        }
      }
    },
    toLogin () {
      const tokenStr = window.sessionStorage.getItem('token')
      // 后端指定接口验证了token的正确性
      if (!tokenStr) {
        this.$confirm('登录后才能发表评论或者点赞，请问是否先登录？', '提示', { // 确认框
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
    async deleteComment (item) {
      this.$confirm('若该评论有子评论的话会被一起删除，你确定要继续删除吗？', '提示', { // 确认框
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/hs/comment/deletecomment?uid=${this.uid}&cid=${item.id}`).then((res) => {
          if (res.data.success) {
            // sessionStorage.setItem('parentCommentId', -1)
            // this.formData.content = '请输入评论信息...'
            // 弹出提示信息
            this.$message({
              message: '删除评论成功',
              type: 'success'
            })
          } else {
            this.$message.error(res.data.message)
          }
        })
        this.getCommentList()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '删除操作已取消'
        })
      })
    },
    async getCommentList () {
      const blogId = sessionStorage.getItem('blogId')
      const { data: res } = await this.$http.get(`/api/hs/comment/getcomment?id=${blogId}&currPage=${this.pagination.currPage}&pageSize=${this.pagination.pageSize}`)
      if (!res.success) {
        console.log(res)
        return this.$message.error('获取评论列表信息失败！')
      }
      // this.dataList2 = res.data
      this.comments = res.data.list
      this.pagination.total = res.data.totalCount
    },
    reloadPrism () {
      process.browser && document.querySelectorAll('pre code').forEach(block => Prism.highlightElement(block))
    },
    // 获取博客
    async getOneBlog () {
      const blogId = sessionStorage.getItem('blogId')
      await this.$http.post(`/api/hs/views/create?bid=${blogId}&ip=${this.ip}`)
      const { data: res } = await this.$http.get(`/api/hs/blog/getblogbyid?id=${blogId}`)
      if (!res.success) {
        return this.$message.error(res.message)
      }
      this.dataList = res.data

      const { data: thumbed } = await this.$http.get(`/api/hs/thumbs/query?bid=${blogId}&uid=${this.uid}`)
      this.thumbsFlag = thumbed.data

      const { data: collected } = await this.$http.get(`/api/hs/collects/query?bid=${blogId}&uid=${this.uid}`)
      this.collectFlag = collected.data
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.uid = window.sessionStorage.getItem('userid')
        this.nickname = window.sessionStorage.getItem('user')
        this.avatar = window.sessionStorage.getItem('avatar')
      }
      this.ip = this.$returnCitySN.cip
    },
    logout () {
      window.sessionStorage.clear()
      this.$router.push('/home')
      // 刷新页面，删除vuex数据
      window.location.reload()
    },
    // 评论相关函数
    inputFocus () {
      var replyInput = document.getElementById('replyInput')
      replyInput.style.padding = '8px 8px'
      replyInput.style.border = '2px solid blue'
      replyInput.focus()
    },
    showReplyBtn () {
      this.btnShow = true
    },
    hideReplyBtn () {
      this.btnShow = false
      var replyInput = document.getElementById('replyInput')
      replyInput.style.padding = '10px'
      replyInput.style.border = 'none'
    },
    showReplyInput (i, id) {
      this.comments[this.index].inputShow = false
      this.index = i
      this.comments[i].inputShow = true
      this.parentCommentId = id
    },
    _inputShow (i) {
      return this.comments[i].inputShow
    },
    // 发送评论和回复
    async sendComment () {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      } else {
        const input = document.getElementById('replyInput')
        const blogId = sessionStorage.getItem('blogId')
        if (this.toLogin()) {
          await this.$http.post(`/api/hs/comment/comment?uid=${this.uid}&bid=${blogId}&content=${this.replyComment}`)
        }
        this.replyComment = ''
        input.innerHTML = ''
        this.getCommentList()
      }
    },
    async sendCommentReply (i, pcid) {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      } else {
        const blogId = sessionStorage.getItem('blogId')
        if (this.toLogin()) {
          await this.$http.post(`/api/hs/comment/comment?uid=${this.uid}&bid=${blogId}&content=${this.replyComment}&pcid=${pcid}`)
        }
        this.replyComment = ''
        document.getElementsByClassName('reply-comment-input')[i].innerHTML = ''
        this.getCommentList()
      }
    },
    // 获取评论
    onDivInput: function (e) {
      this.replyComment = e.target.innerHTML
    },
    handleCurrentChange (currentPage) {
      // 设置最新的页码
      this.pagination.currPage = currentPage
      // 重新调用findPage方法进行分页查询
      this.getCommentList()
    },
    handleSizeChange (newSize) {
      this.pagination.pageSize = newSize
      this.getCommentList()
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
  },
  mounted () {
    // 有效
    setTimeout(() => {
      this.reloadPrism()
      // eslint-disable-next-line no-undef
      tocbot.init({
        // Where to render the table of contents.
        tocSelector: '.js-toc',
        // Where to grab the headings to build the table of contents.
        contentSelector: '.js-toc-content',
        // Which headings to grab inside of the contentSelector element.
        headingSelector: 'h1, h2, h3'
      })
    }, 1000)
    $('.ui.dropdown').dropdown({
      on: 'hover'
    })
    $('.menu.toggle').click(function () {
      $('.m-item').toggleClass('m-mobile-hide')
    })
    $('#payButton').popup({
      popup: $('.payQR.popup'),
      on: 'click',
      position: 'bottom center'
    })
    $('.toc.button').popup({
      popup: $('.toc-container.popup'),
      on: 'click',
      position: 'left center'
    })
    $('.wechat').popup({
      popup: $('.wechat-qr'),
      position: 'left center'
    })
    $('#toTop-button').click(function () {
      console.log('111')
      $(window).scrollTo(0, 500)
    })
    // eslint-disable-next-line no-unused-vars
    var qrcode = new QRCode('qrcode', {
      text: 'https://blog.csdn.net/Dlihctcefrep',
      width: 110,
      height: 110,
      colorDark: '#000000',
      colorLight: '#ffffff',
      correctLevel: QRCode.CorrectLevel.H
    })
  }
}
</script>

<style scoped>
  @import "../../assets/css/typo.css";
  @import "../../assets/css/animate.css";
  .container{
    animation: main 1s;
  }
  .m-blog {
    padding-top: 69vh !important;
    padding-bottom: 0px !important;
  }
  .blog-banner {
    height: 67vh;
    /* background: url(https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mcSPhM*LqG*bYzP4o2JVbahf8eHd4Yd9FO1B4n8UbWJRaQANKT1EBypIfvSNRueI1LSu6d212FUoavpm63ZrUI3E!/r) center
    center / cover no-repeat; */
    background: url('../../assets/images/blogbanner.jpg') center
    center / cover no-repeat;
    background-color: #49b1f5;
  }
  /* 评论区样式 */
  .my-reply {
  padding: 10px;
  background-color: #fafbfc;
  }
  .my-reply .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .my-reply .reply-info {
    display: inline-block;
    margin-left: 5px;
    width: 90%;
  }
  @media screen and (max-width: 1200px) {
    .my-reply .reply-info {
      width: 80%;
    }
  }
  .my-reply .reply-info .reply-input {
    min-height: 20px;
    line-height: 22px;
    padding: 10px 10px;
    color: #ccc;
    background-color: #fff;
    border-radius: 5px;
  }
  .my-reply .reply-info .reply-input:empty:before {
    content: attr(placeholder);
  }
  .my-reply .reply-info .reply-input:focus:before {
    content: none;
  }
  .my-reply .reply-info .reply-input:focus {
    padding: 8px 8px;
    border: 2px solid #00f;
    box-shadow: none;
    outline: none;
  }
  .my-reply .reply-btn-box {
    height: 25px;
    margin: 10px 0;
  }
  .my-reply .reply-btn-box .reply-btn {
    position: relative;
    float: right;
    margin-right: 15px;
  }
  .my-comment-reply {
    margin-left: 50px;
  }
  .my-comment-reply .reply-input {
    width: flex;
  }
  .author-title:not(:last-child) {
    border-bottom: 1px solid rgba(178,186,194,0.3);
  }
  .author-title {
    padding: 10px;
  }
  .author-title .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .author-title .author-info {
    display: inline-block;
    margin-left: 5px;
    width: 60%;
    height: 40px;
    line-height: 20px;
  }
  .author-title .author-info >span {
    display: block;
    cursor: pointer;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .author-title .author-info .author-name {
    color: #000;
    font-size: 18px;
    font-weight: bold;
  }
  .author-title .author-info .author-time {
    font-size: 14px;
  }
  .author-title .icon-btn {
    width: 30%;
    padding: 0 !important;
    float: right;
  }
  @media screen and (max-width: 1200px) {
    .author-title .icon-btn {
      width: 20%;
      padding: 7px;
    }
  }
  .author-title .icon-btn >span {
    cursor: pointer;
  }
  .author-title .icon-btn .iconfont {
    margin: 0 5px;
  }
  .author-title .talk-box {
    margin: 0 50px;
  }
  .author-title .talk-box >p {
    margin: 0;
  }
  .author-title .talk-box .reply {
    font-size: 16px;
    color: #000;
  }
  .author-title .reply-box {
    margin: 10px 0 0 50px;
    background-color: #efefef;
  }
</style>
