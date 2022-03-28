<template>
  <div class="tags">
    <div class="tags-banner banner">
      <h1 class="banner-title">MachineLearning</h1>
      <h2 class="banner-title" style="margin-top: 40px">NLP</h2>
    </div>
    <div class="m-container-small m-types">
        <!-- 弹幕列表 -->
        <!-- <div class="barrage-container">
          <vue-baberrage :barrageList="barrageList" :loop="true">
            <template v-slot:default="slotProps">
                <span class="barrage-items">
                  <img
                    :src="slotProps.item.avatar"
                    width="30"
                    height="30"
                    style="border-radius:100%"
                  />
                  <span class="ml-2">{{ slotProps.item.title }} :</span>
                  <span class="ml-2">{{ slotProps.item.messageContent }}</span>
                </span>
            </template>
          </vue-baberrage>
        </div> -->
        <div class="m-fenci">
          <el-card class="blog-container2">
            <!-- 说明 -->
            <div class="link-title mt-4 mb-4" style="text-align: center">
              分词
            </div>
            <br>
            <blockquote class="mb-10 my-blog-shadow">
              <div>{{testResult1}}</div>
            </blockquote>
            <br>
            <br>
            <br>
            <div><el-input suffix-icon="el-icon-edit" style="width: 270px" v-model="str10" placeholder="输入内容" ></el-input>
              <button class="ui google plus button my-button" @click="extraction"><i class="pencil alternate icon"></i>生成结果</button></div>
            <br>
            <br>
          </el-card>
        </div>
        <div class="m-panjue">
          <el-card class="blog-container2">
            <!-- 说明 -->
            <div class="link-title mt-4 mb-4" style="text-align: center">
              判决结果预测
            </div>
            <br>
            <blockquote class="mb-10 my-blog-shadow">
              <div>{{testResult2}}</div>
            </blockquote>
            <br>
            <br>
            <br>
            <div><el-input suffix-icon="el-icon-edit" style="width: 270px" v-model="str11" placeholder="输入内容" ></el-input>
              <button class="ui google plus button my-button" @click="judgmentPrediction"><i class="pencil alternate icon"></i>生成判决结果</button></div>
            <br>
            <br>
          </el-card>
        </div>
        <div class="m-poem">
          <el-card class="blog-container2">
            <!-- 说明 -->
            <div class="link-title mt-4 mb-4" style="text-align: center">
              相似度计算
            </div>
            <br>
            <blockquote class="mb-10 my-blog-shadow">
              <div>{{testResult3}}</div>
            </blockquote>
            <br>
            <br>
            <br>
            <div><el-input suffix-icon="el-icon-edit" style="width: 270px" v-model="str12" placeholder="输入内容" ></el-input>
              <!-- <button class="ui primary button my-button" @click="randomPoem"><i class="star icon"></i>生成随机古诗</button> -->
              <!-- <button class="ui secondary button my-button" @click="acrosticPoem"><i class="paragraph icon"></i>生成藏头诗</button> -->
              <button class="ui google plus button my-button" @click="judgmentSimilarity"><i class="pencil alternate icon"></i>生成结果</button></div>
            <br>
            <br>
          </el-card>
        </div>
      <!-- <el-dialog
        title="文本分类的结果"
        :visible.sync="centerDialogVisible"
        width="80%"
        height="90%"
        center>
        <div class="search-result-wrapper">
          <hr class="divider" />
          <br>
          <br>
          <template>
            <el-col style="width: 33%;" v-for="(item, index) in dataList2" :key="index">
            <div class="ui card my-shadow">
              <div class="image">
                <img v-if="item.type==='java'" class="ui rounded image" style="height: 197px" src="https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mcfxg*eg*DXzoyOPhCpNztcXuaOnSxohME.pPlGk0X6DySw85OaptMioCCglUIbkXCgALZYs9vHvPRXBaQfUpMsw!/r">
                <img v-if="item.type==='python'" class="ui rounded image" style="height: 197px" src="https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mcVU0waANwiuihq.W2nBzwOwXqxYRz4ImLPsNhUcM*0mhWWYh*q*8*4kXYbfAoXAqKvTD01Y*vpl7XfJncQ4cC.8!/r">
                <img v-if="item.type==='db'" class="ui rounded image" style="height: 197px" src="https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mcfxg*eg*DXzoyOPhCpNztcWZGLjzPgACkFdY6wZX94X6RimTfpFdSg9Nq5YCW8SN6Z2rmInF8bxIhdL6tHQ8TTQ!/r">
              </div>
              <div class="content">
                <a class="header">{{item.type}}</a>
                <div class="meta">
                  <span class="date">预测的权重是：</span>
                </div>
                <div class="description">{{item.result}}</div>
              </div>
            </div>
          </el-col>
          </template>
          <span slot="footer" class="dialog-footer">
    </span>
        </div>
      </el-dialog> -->
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import { vueBaberrage } from 'vue-baberrage'
Vue.use(vueBaberrage)
export default {
  data () {
    return {
      user: {},
      nickname: '',
      avatar: 'https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mccJUo7*q6gaMPZmbFDSW8tjmAm4XwuoUZmMKw3asmvn1mxsE*Tf0fj.VOh2G6OX7v4duFOfedV2oGNQ*GrJEPkA!/r',
      str10: '',
      str11: '',
      str12: '',
      centerDialogVisible: false,
      picUrl: {
        a: '',
        b: '',
        c: ''
      },
      extractionResult: {
        type: '',
        charge: '',
        responsibilities: '',
        commutationFactors: '',
        aggravatingFactors: '',
        result: ''
      },
      testResult1: '事件要素抽取',
      testResult2: '判决结果预测',
      testResult3: '相似度计算'
    }
  },
  created () {
    this.getUser()
  },
  methods: {
    async extraction () {
      if (JSON.stringify(this.str10) === '{}' || this.str10 !== '') {
        const { data: res } = await this.$http.get('api/forum/nlp/extraction/?text=' + this.str10)
        if (res.success) {
          this.testResult1 = ''
          for (var key in res.data) {
            this.testResult1 += key + ':'
            for (var value in res.data[key]) {
              this.testResult1 += res.data[key][value] + ' '
            }
            this.testResult1 += '\n'
          }
        } else {
          this.$message.error(res.message)
        }
      }
    },
    async judgmentPrediction () {
      if (JSON.stringify(this.str11) === '{}' || this.str11 !== '') {
        const { data: res } = await this.$http.get('api/forum/nlp/prediction/?text=' + this.str11)
        if (res.success) {
          this.testResult2 = res.data.months
          // for (var key in res.data) {
          //   this.testResult2 += key + ':'
          //   this.testResult2 += res.data[key] + ' '
          //   this.testResult2 += '\n'
          // }
        } else {
          this.$message.error(res.message)
        }
      }
    },
    async judgmentSimilarity () {
      if (JSON.stringify(this.str12) === '{}' || this.str12 !== '') {
        const { data: res } = await this.$http.get('api/forum/nlp/similarity/?text=' + this.str12)
        if (res.success) {
          this.testResult3 = ''
          for (var key in res.data) {
            this.testResult3 += key + ':'
            this.testResult3 += res.data[key] + ' '
            this.testResult3 += '\n'
          }
        } else {
          this.$message.error(res.message)
        }
      }
    },
    getUser () {
      this.user = window.sessionStorage.getItem('user')
      if (this.user != null) {
        this.nickname = window.sessionStorage.getItem('user')
        this.avatar = window.sessionStorage.getItem('avatar')
      }
    },
    mounted () {
    }
  }
}
</script>

<style scoped>
  .m-types {
    padding-top: 60vh !important;
    padding-bottom: 0px !important;
  }
  .my-button{
    margin-left: 10px!important;
    position: relative;
    z-index: 100;
  }
  .tags-banner {
    height:  67vh;
    /* background: url(https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mce80KgERvhV49OLgLEc6tIVzgewffrS4J*Towawiswq8W32hHiP2mi3eEOka5wZKbmxS4NAOB7C8HDnHM7AmmwY!/r) center
    center / cover no-repeat; */
    background: url('../../assets/images/aibanner.png') center
    center / cover no-repeat;
    background-color: #49b1f5;
  }
  blockquote {
    line-height: 2;
    margin: 0;
    font-size: 15px;
    border-left: 0.2rem solid #1685a9;
    padding: 10px 1rem !important;
    background-color: #ecf7fe;
    border-radius: 4px;
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
  .blog-container2 {

    background: #fff;
    color: #4c4948;
    border-radius: 8px;
    box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);
    transition: all 0.3s;
  }
  .blog-container2:hover {
    box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
  }
  .m-fenci {
    padding-top: 11vh !important;
    padding-bottom: 0px !important;
  }
  .m-panjue{
    padding-top: 11vh !important;
    padding-bottom: 0px !important;
  }
  .m-poem {
    padding-top: 11vh !important;
    padding-bottom: 0px !important;
  }
  .m-text {
    top: 40vh !important;
    /*padding-bottom: 0px !important;*/
  }
  .link-title {
    color: #344c67;
    font-size: 21px;
    font-weight: bold;
    line-height: 2;
  }
  @media (max-width: 759px) {
    .link-avatar {
      margin-left: 30px;
    }
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
  .message-input-wrapper input {
    outline: none;
    width: 70%;
    border-radius: 20px;
    height: 100%;
    padding: 0 1.25rem;
    color: #eee;
    border: #fff 1px solid;
  }
  .message-input-wrapper input::-webkit-input-placeholder {
    color: #eeee;
  }
  .message-input-wrapper button {
    outline: none;
    border-radius: 20px;
    height: 100%;
    padding: 0 1.25rem;
    border: #fff 1px solid;
  }
  .barrage-container {
    position: absolute;
    top: 500px;
    left: 0;
    right: 0;
    bottom: 0;
    height: calc(100% -50px);
    width: 100%;
  }
  .barrage-items {
    background: rgb(0, 0, 0, 0.7);
    border-radius: 100px;
    color: #fff;
    padding: 5px 10px 5px 5px;
    align-items: center;
    display: flex;
  }
  .box-card{
    width: 80%;
    margin-left: 9%;
  }
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }
  .search-input-wrapper {
    display: flex;
    padding: 5px;
    height: 35px;
    width: 100%;
    border: 2px solid #8e8cd8;
    border-radius: 2rem;
  }
  @media (min-width: 960px) {
    .search-result-wrapper {
      padding-right: 5px;
      height: 450px;
      overflow: auto;
    }
  }
  @media (max-width: 959px) {
    .search-result-wrapper {
      height: calc(100vh - 110px);
      overflow: auto;
    }
  }
  .divider {
    margin: 20px 0;
    border: 2px dashed #d2ebfd;
  }
  .search-reslut-content {
    color: #555;
    cursor: pointer;
    border-bottom: 1px dashed #ccc;
    padding: 5px 0;
    line-height: 2;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
  }
</style>
