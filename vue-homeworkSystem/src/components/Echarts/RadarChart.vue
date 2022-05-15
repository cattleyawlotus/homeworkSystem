<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import { debounce } from '@/utils'
require('echarts/theme/macarons') // echarts theme

const animationDuration = 3000

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data () {
    return {
      chart: null,
      data: [],
      viewmax: 0,
      thumbmax: 0,
      collectmax: 0,
      commentmax: 0,
      totalmax: 0
    }
  },
  mounted () {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart () {
      this.$http.get('/api/forum/manager/blogrank').then(res => {
        this.data = res.data.data
        for (var i in this.data) {
          this.thumbmax = Math.max(this.thumbmax, this.data[i].thumbs)
          this.viewmax = Math.max(this.viewmax, this.data[i].views)
          this.collectmax = Math.max(this.collectmax, this.data[i].collects)
          this.commentmax = Math.max(this.commentmax, this.data[i].comments)
          this.totalmax = Math.max(this.totalmax, this.data[i].total)
        }
        this.chart = echarts.init(this.$el, 'macarons')
        this.chart.setOption({
          tooltip: {
            trigger: 'item',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            backgroundColor: 'rgba(255,255,255,0.8)' // 通过设置rgba调节背景颜色与透明度
          },
          radar: {
            radius: '66%',
            center: ['50%', '42%'],
            splitNumber: 8,
            splitArea: {
              areaStyle: {
                color: 'rgba(127,95,132,.3)',
                opacity: 1,
                shadowBlur: 45,
                shadowColor: 'rgba(0,0,0,.5)',
                shadowOffsetX: 0,
                shadowOffsetY: 15
              }
            },
            indicator: [
              // { name: 'Sales', max: 10000 },
              // { name: 'Administration', max: 20000 },
              // { name: 'Information Techology', max: 20000 },
              // { name: 'Customer Support', max: 20000 },
              // { name: 'Development', max: 20000 },
              // { name: 'Marketing', max: 20000 }
              { name: '点赞', max: this.thumbmax },
              { name: '评论', max: this.commentmax },
              { name: '收藏', max: this.collectmax },
              { name: '浏览', max: this.viewmax },
              { name: '评分', max: this.totalmax }
            ]
          },
          legend: {
            left: 'center',
            bottom: '10',
            data: [this.data[0].title, this.data[1].title, this.data[2].title]
          },
          series: [{
            type: 'radar',
            symbolSize: 0,
            areaStyle: {
              normal: {
                shadowBlur: 13,
                shadowColor: 'rgba(0,0,0,.2)',
                shadowOffsetX: 0,
                shadowOffsetY: 10,
                opacity: 1
              }
            },
            data: [
              // {
              //   value: [5000, 7000, 12000, 11000, 15000, 14000],
              //   name: 'Allocated Budget'
              // },
              // {
              //   value: [4000, 9000, 15000, 15000, 13000, 11000],
              //   name: 'Expected Spending'
              // },
              // {
              //   value: [5500, 11000, 12000, 15000, 12000, 12000],
              //   name: 'Actual Spending'
              // }
              {
                value: [this.data[0].thumbs, this.data[0].comments, this.data[0].collects, this.data[0].views, this.data[0].total],
                name: this.data[0].title
              },
              {
                value: [this.data[1].thumbs, this.data[1].comments, this.data[1].collects, this.data[1].views, this.data[1].total],
                name: this.data[1].title
              },
              {
                value: [this.data[2].thumbs, this.data[2].comments, this.data[2].collects, this.data[2].views, this.data[2].total],
                name: this.data[2].title
              }
            ],
            animationDuration: animationDuration
          }]
        })
      })
    }
  }
}
</script>
