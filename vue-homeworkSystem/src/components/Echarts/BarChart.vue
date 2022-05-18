<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import { debounce } from '@/utils'
require('echarts/theme/macarons') // echarts theme

const animationDuration = 6000

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
      days: [],
      data: [],
      chart: null
    }
  },
  created () {
    for (var i = 1; i <= 7; i++) {
      this.days.push(this.getDay(-i))
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
    getDay (day) {
      var today = new Date()
      // eslint-disable-next-line camelcase
      var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day
      today.setTime(targetday_milliseconds)
      var tMonth = today.getMonth()
      var tDate = today.getDate()
      tMonth = this.doHandleMonth(tMonth + 1)
      tDate = this.doHandleMonth(tDate)
      return tMonth + '-' + tDate
    },
    doHandleMonth (month) {
      var m = month
      if (month.toString().length === 1) {
        m = '0' + month
      }
      return m
    },
    initChart () {
      this.$http.get('/api/hs/manager/last7daysblogdata').then(res => {
        this.data = res.data.data
        this.chart = echarts.init(this.$el, 'macarons')
        this.chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            backgroundColor: 'rgba(255,255,255,0.8)' // 通过设置rgba调节背景颜色与透明度
          },
          grid: {
            top: 10,
            left: '2%',
            right: '2%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [{
            type: 'category',
            data: this.days,
            axisTick: {
              alignWithLabel: true
            }
          }],
          yAxis: [{
            type: 'value',
            axisTick: {
              show: false
            }
          }],
          series: [{
            name: '点赞量',
            type: 'bar',
            stack: 'vistors',
            barWidth: '60%',
            // data: [79, 52, 200, 334, 390, 330, 220],
            data: this.data.thumbs,
            animationDuration
          }, {
            name: '收藏量',
            type: 'bar',
            stack: 'vistors',
            barWidth: '60%',
            // data: [80, 52, 200, 334, 390, 330, 220],
            data: this.data.collects,
            animationDuration
          }, {
            name: '评论量',
            type: 'bar',
            stack: 'vistors',
            barWidth: '60%',
            // data: [30, 52, 200, 334, 390, 330, 220],
            data: this.data.comments,
            animationDuration
          }]
        })
      })
    }
  }
}
</script>
