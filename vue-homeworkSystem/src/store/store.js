import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    msg: ''
  },
  mutations: {
    msg (state, msg) {
      state.msg = msg
    }
  }

})

export default store
