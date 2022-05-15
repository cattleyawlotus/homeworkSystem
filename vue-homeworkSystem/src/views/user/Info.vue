<template>
<div>
  <div class="info-banner banner">
      <h1 class="banner-title">
        个人中心
      </h1>
  </div>
  <div class="container">
      <div class="left">
        <div class="info">
          <div class="portrait"><img :src="this.userInfo.portrait" width="80" height="80" /></div>
            <div class="user-info">
              <div class="nick-level">
                <div class="nick" v-text="this.userInfo.nick"></div>
                <div :class="'level level'+levelClass">
                  <i :class="'icons icon_level_'+levelClass"></i>
                  <span v-text="this.userInfo.level"></span>
                </div>
              </div>
              <div class="uid" v-text="'ID:'+this.userInfo.uid"></div>
              <div class="wealth">
                <div class="w-star"><span v-text="this.userInfo.point"></span> <i class="icons star-sm"></i></div><span class="cut">|</span>
                <div class="w-diamond"><span v-text="this.userInfo.gold"></span> <i class="icons diamond-sm"></i></div>
              </div>
            </div>
        </div>
        <div class="menu">
          <ul class="menu-ul">
            <router-link class="menu-link" to="/myinfo" tag="li">个人中心</router-link>
            <router-link class="menu-link" :to="{ path: '/notify'}" tag="li">消息通知</router-link>
          </ul>
        </div>
      </div>
      <div class="content">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
  </div>
</div>
</template>

<script>
export default {
  data () {
    return {
      isLogin: false,
      userInfo: {
        nick: null,
        ulevel: null,
        uid: null,
        gold: null,
        point: null,
        portrait: null
      }
    }
  },
  props: ['nick', 'level', 'uid', 'starNumber', 'diamondNumber', 'portrait'],
  computed: {
    levelClass () {
      var level = this.level
      if (level >= 1 && level <= 7) {
        return 1
      } else if (level >= 8 && level <= 16) {
        return 2
      } else if (level >= 16 && level <= 31) {
        return 3
      } else if (level >= 32 && level <= 63) {
        return 4
      } else if (level >= 64 && level <= 127) {
        return 5
      } else if (level >= 128 && level <= 254) {
        return 6
      } else {
        return 6
      }
    }
  },
  created () {
    this.getUserInfo()
  },
  methods: {
    getUserInfo () {
      this.userInfo.nick = window.sessionStorage.getItem('user')
      this.userInfo.ulevel = 120
      this.userInfo.uid = window.sessionStorage.getItem('userid')
      this.userInfo.gold = '2186'
      this.userInfo.point = '8864'
      this.userInfo.portrait = window.sessionStorage.getItem('avatar')
      // console.log(this.userInfo)
    }
  }
}
</script>

<style scoped>
.info-banner {
  height:  67vh;
  background: url('../../assets/images/infobanner.jpg') center center /
  cover no-repeat;
  background-color: #49b1f5;
}

.left{display: inline-block;}
.info{background: #fff; width: 300px; height: 180px; text-align: left;}

/* .container{text-align: center; padding-top: 650px; overflow: hidden;} */
.container{padding: 650px 250px 30px 250px; width:100%;overflow: hidden;}
.content{background: #fff;vertical-align: top; display: inline-block;width: 1090px;  height: 735px;margin-left: 20px; overflow-y: auto;overflow: hidden;}
/* .content{background: #fff;vertical-align: top; display: inline-block; width: 800px; height: 935px; margin-left: 20px; overflow-y: auto;overflow: hidden;} */

.menu{background: #fff; width: 300px; height: 540px; margin-top: 15px; text-align: left; color: #737373}
.menu-ul{padding: 0; margin: 0;}
.router-link-active{font-weight: bold; color: #353535; background: #DCF9F5;}
.menu-link{list-style-type: none; height: 60px; cursor: pointer; line-height: 60px; padding: 0 0 0 25px; transition: all .4s;}

.content-main{margin: 20px; text-align: left;}
.portrait{width: 80px; height: 80px; overflow:hidden; -webkit-border-radius: 40px;
-moz-border-radius: 40px;
-ms-border-radius: 40px;
-o-border-radius: 40px;
border-radius: 40px; background-color: #CCCCCC; margin: 40px 18px 0 15px;}
.user-info{margin: 38px 0 0 0; vertical-align: top;}
.portrait, .user-info, .w-star, .w-diamond, .nick, .level{display: inline-block;}
.nick{white-space:nowrap; overflow:hidden; text-overflow:ellipsis; max-width: 110px;}
.nick,.level{vertical-align: middle;}
.cut{padding: 0 10px; color:#E9E9E9; font-size: 15px;}

.uid,.wealth{margin-top: 14px;}
.uid{font-size: 13px;}
.level{ padding:0 6px; color:#fff; font-size:12px;margin-left: 5px;-webkit-border-radius: 10px;
-moz-border-radius: 10px;
-ms-border-radius: 10px;
-o-border-radius: 10px;
border-radius: 10px;height:20px; line-height:20px;}
.level .icons{display:inline-block;}
.level1{background:#50e4ce;}
.level2{background:#4a87f6;}
.level3{background:#fa9f47;}
.level4{background:#fad247;}
.level5{background:#5061e4;}
.level6{background:#ac47fa;}
.level .num{display:inline-block;}
.wealth{font-size: 12px; white-space: nowrap;}
.wealth .icons{margin-top: -3px;vertical-align: middle;}

body,ul,ol,p,h1,h2,h3,h4,h5,dl,dd,form,input,textarea,td,th,button,strong,em,select,video,canvas{margin:0;padding:0;}
li{list-style:none;vertical-align:top;}
table{ border-collapse:collapse;}
textarea{resize:none;overflow:auto;}
img{ border:none; vertical-align:middle;}
em{ font-style:normal;}
a{ text-decoration:none;}
a,input{ -webkit-tap-highlight-color:rgba(0,0,0,0);/*ios android去除自带阴影的样式*/}
input{outline:0px;}
body{background:#f1f1f1; margin: 0; padding: 0; font-family:"Microsoft YaHei",Helvetica;}

.block{display: block;}
.bold{font-weight: bold; color: #3C3C3C;}

.dib{display: inline-block;}
.ml20{margin-left: 20px;}
.ml19{margin-left: 19px;}
.ml10{margin-left: 10px;}
.mt10{margin-top: 10px;}
.fs12{font-size: 12px;}
.btn-loading{background-color: #a5f0e6 !important;}

.input{width: 278px; padding: 0 10px; margin: 0 auto; margin-top:25px;background: #fff;color: #333; height: 39px; line-height: 39px; border-radius: 5px; border: 1px solid #E3E3E3;}
.empty{border: 1px solid #f97a7a;}
.btn-lg{ display: block;width:300px; margin: 0 auto;background: #50E3CE;color: #fff;height: 41px;line-height: 41px;border-radius: 5px;margin-top:20px; text-align: center;}

.icons{background:url(../../assets/images/icons.png)  no-repeat; display: inline-block;}
.login-fb{height:29px;width:13px;background-position:0 0;}
.close{height:16px;width:16px;background-position:-13px 0;}
.info-focus{height:18px;width:18px;background-position:-47px 0;}
.star{height:24px;width:25px;background-position:-65px 0;}
.star-sm{height:18px;width:18px; background-position: -48px -1px;background-size: 360px 22px;}
.login-vk{height:15px;width:27px;background-position:-90px 0;}
.login-email{height:17px;width:29px;background-position:-117px 0;}
.login-tw{height:23px;width:29px;background-position:-146px 0;}
.diamond{height:22px;width:29px;background-position:-175px 0;}
.diamond-sm{height: 18px;width: 21px;background-position: -127px 1px;background-size: 360px 23px;}
.process-i2{height:30px;width:30px;background-position:-204px 0;}
.login-kakao{height:28px;width:31px;background-position:-234px 0;}
.process-i3{height:31px;width:31px;background-position:-265px 0;}
.process-i1{height:29px;width:31px;background-position:-296px 0;}
.icon_level_1{height:15px;width:15px;background-position:-333px 3px;}
.icon_level_2{height:15px;width:15px;background-position:-353px 3px;}
.icon_level_4{height:15px;width:15px;background-position:-393px 4px;}
.icon_level_5{height:15px;width:15px;background-position:-411px 3px;}
.icon_level_3{height:15px;width:15px;background-position:-375px 3px;}
.icon_level_6{height:15px;width:15px;background-position:-430px 3px;}
.icon-prev{height:18px;width:9px;background-position:-451px 0;}
.icon-next{height:18px;width:9px;background-position:-466px 0;}

/* transition animate */
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s
}
.fade-enter, .fade-leave-active {
  opacity: 0
}

.slide-enter-active {
  transition: all .3s ease;
}
.slide-enter, .slide-leave-active {
  transform: translateX(10px);
  opacity: 0;
}

</style>
