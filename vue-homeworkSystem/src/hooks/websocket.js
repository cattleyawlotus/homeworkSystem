//  这里需要引入vuex
import store from '../store/store'
const wsConnection = {
  $ws: null,
  lockReturn: false,
  timeout: 60 * 1000 * 5,
  timeoutObj: null,
  timeoutNum: null,
  serverTimeoutObj: null,
  // 初始化webSocket长连接
  initWebSocket: function () {
    const uid = window.sessionStorage.getItem('userid')
    this.$ws = new WebSocket(`ws://47.101.213.150:8081/websocket/${uid}`)//  写入地址 这里的地址可以在initWebSocket方法加入参数
    this.$ws.onopen = this.wsOpen
    this.$ws.onclose = this.wsClose
    this.$ws.onmessage = this.wsMsg
    this.$ws.onerror = this.wsError
  },
  //  打开websocket
  wsOpen: function (e) {
    //  开始websocket心跳
    wsConnection.startWsHeartbeat()
    console.log('ws success')
  },
  wsClose: function (e) {
    console.log(e, 'ws close')
  },
  wsMsg: function (msg) {
    //  每次接收到服务端消息后 重置websocket心跳
    wsConnection.resetHeartbeat()
    // 服务端发送来的消息存到vuex
    store.commit('msg', msg.data)
  },
  wsError: function (err) {
    console.log(err, 'ws error')
    wsConnection.reconnect()
  },
  //  重启websocket
  reconnect: function () {
    const _this = this
    if (_this.lockReturn) {
      return
    }
    _this.lockReturn = true
    _this.timeoutNum && clearTimeout(_this.timeoutNum)
    _this.timeoutNum = setTimeout(function () {
      _this.initWebSocket()
      _this.lockReturn = false
    }, 3000)
  },
  startWsHeartbeat: function () {
    const _this = this
    _this.timeoutObj && clearTimeout(_this.timeoutObj)
    _this.serverTimeoutObj && clearTimeout(_this.serverTimeoutObj)
    _this.timeoutObj = setInterval(function () {
    //  判断websocket当前状态
      if (_this.$ws.readyState !== 1) {
        _this.reconnect()
      }
    }, _this.timeout)
  },
  // 重置websocket心跳
  resetHeartbeat: function () {
    const _this = this
    clearTimeout(_this.timeoutObj)
    clearTimeout(_this.serverTimeoutObj)
    _this.startWsHeartbeat()
  }
}

//  抛出websocket对象
export default wsConnection
