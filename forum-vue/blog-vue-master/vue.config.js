'use strict'
const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://47.101.213.150:8081',
        // target: 'http://127.0.0.1:8081',
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    before: app => { }
  },
  chainWebpack: config => {
    config.module.rule('svg').exclude.add(resolve('src/assets/icons')).end()
    config.module.rule('icons').test(/\.svg$/).include.add(resolve('src/assets/icons')).end().use('svg-sprite-loader').loader('svg-sprite-loader').options({
      symbolId: 'icon-[name]'
    }).end()
    // 发布模式
    config.when(process.env.NODE_ENV === 'production', config => {
      // 默认是main.js要清空换成自己的
      config.entry('app').clear().add('./src/main-prod.js')

      config.set('externals', {
        // vue: 'Vue',
        'vue-router': 'VueRouter',
        axios: 'axios',
        lodash: '_',
        echarts: 'echarts',
        nprogress: 'NProgress'
        // 'mavon-editor': 'mavon-editor' // 配置有问题，不知道如何修改
      })

      config.plugin('html').tap(args => {
        args[0].isProd = true
        args[0].title = '法律在线'
        return args
      })
    })

    // 开发模式
    config.when(process.env.NODE_ENV === 'development', config => {
      // 默认是main.js要清空换成自己的
      config.entry('app').clear().add('./src/main-dev.js')
      config.plugin('html').tap(args => {
        args[0].isProd = false
        args[0].title = '法律在线'
        return args
      })
    })
  }
}
