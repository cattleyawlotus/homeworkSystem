<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import { debounce } from '@/utils'

require('echarts/theme/macarons') // echarts theme

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
      chart: null
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
      this.$http.get('/api/forum/manager/blogdata').then(res => {
        const typeName = []
        for (var item in res.data.data) {
          typeName.push(res.data.data[item].name)
        }
        this.chart = echarts.init(this.$el, 'macarons')
        this.chart.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)',
            backgroundColor: 'rgba(255,255,255,0.8)' // 通过设置rgba调节背景颜色与透明度
          },
          legend: {
            left: 'center',
            bottom: '10',
            data: typeName
            // data: ['Industries', 'Technology', 'Forex', 'Gold', 'Forecasts']
          },
          calculable: true,
          series: [
            {
              name: '数量',
              type: 'pie',
              roseType: 'radius',
              radius: [15, 95],
              center: ['50%', '38%'],
              // data: [
              //   { value: 320, name: 'Industries' },
              //   { value: 240, name: 'Technology' },
              //   { value: 149, name: 'Forex' },
              //   { value: 100, name: 'Gold' },
              //   { value: 59, name: 'Forecasts' }
              // ],
              data: res.data.data,
              animationEasing: 'cubicInOut',
              animationDuration: 2600
            }
          ]
        })
      })
    }
  }
}
</script>
