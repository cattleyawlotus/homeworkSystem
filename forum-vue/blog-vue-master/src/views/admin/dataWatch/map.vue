 <template>
  <div>
    <div id='main' style='width: 700px; height: 500px; margin: 0 auto;background-color: #fff' align='center'></div>
  </div>
</template>

<script>
// 1. 导入 echarts
import * as echarts from 'echarts'
const chinaJson = require('../../../../src/assets/map/china.json')
var provinces = ['310000', '130000', '140000', '150000', '210000', '220000', '230000', '320000', '330000', '340000', '350000', '360000', '370000', '410000', '420000', '430000', '440000', '450000', '460000', '510000', '520000', '530000', '540000', '610000', '620000', '630000', '640000', '650000', '110000', '120000', '500000', '810000', '820000', '710000']
var provincesText = ['上海', '河北省', '山西省', '内蒙古自治区', '辽宁省', '吉林省', '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '广西壮族自治区', '海南省', '四川省', '贵州省', '云南省', '西藏自治区', '陕西省', '甘肃省', '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '北京市', '天津市', '重庆市', '香港', '澳门', '台湾省']
var provincesName = ['shanghai', 'hebei', 'shanxi', 'neimenggu', 'liaoning', 'jilin', 'heilongjiang', 'jiangsu', 'zhejiang', 'anhui', 'fujian', 'jiangxi', 'shandong', 'henan', 'hubei', 'hunan', 'guangdong', 'guangxi', 'hainan', 'sichuan', 'guizhou', 'yunnan', 'xizang', 'shanxi1', 'gansu', 'qinghai', 'ningxia', 'xinjiang', 'beijing', 'tianjin', 'chongqing', 'xianggang', 'aomen', 'taiwan']
export default {
  data () {
    return {
      myChart: '',
      distributionOptions: '',
      list: []
    }
  },
  created () {},
  methods: {
    changeOptions (name) {
      // 图标
      const series = [this.list].map(v => {
        return {
          type: 'scatter', //  配置显示方式为用户自定义
          coordinateSystem: 'geo',
          data: v,
          itemStyle: {
            color: '#EE2C2C'
          }
        }
      })
      // options
      this.distributionOptions = {
        tooltip: { // 提示框组件
          show: true, // 显示提示框组件
          trigger: 'item', // 触发类型
          triggerOn: 'mousemove', // 出发条件
          formatter: '名称:{b}<br/>坐标:{c}'
        },
        series, // 数据
        geo: {
          map: name || 'china', // 引入地图 省份或者 国家
          layoutCenter: ['50%', '50%'], // 设置后left/right/top/bottom等属性无效
          layoutSize: '40%',
          roam: true, // 开启鼠标缩放和漫
          zoom: 2,
          label: {
            normal: {
            // 静态的时候展示样式
              show: true, // 是否显示地图省份得名称
              textStyle: {
                color: '#fff',
                fontSize: 10,
                fontFamily: 'Arial'
              }
            },
            emphasis: { // 高亮状态下的样式
            // 动态展示的样式
              color: '#fff'
            }
          },
          itemStyle: { // 地图区域的多边形 图形样式。
            normal: {
              borderColor: '#07919e', // 边框颜色
              areaColor: '#4169E1', //  区域颜色
              textStyle: { // 文字颜色
                color: '#fff'
              },
              shadowBlur: 10, // 图形阴影的模糊大小
              shadowOffsetX: 10 // 阴影水平方向上的偏移距离。
            },
            emphasis: {
              areaColor: '#EEAD0E',
              color: '#fff'
            }
          }
        }
      }
    },
    // 初始化地图数据
    init () {
      this.$http.get('/api/forum/manager/location').then(s => {
        this.list = s.data.data
        echarts.registerMap('china', chinaJson)
        this.changeOptions('china')
        this.myChart = echarts.init(document.getElementById('main'))
        this.myChart.setOption(this.distributionOptions)
        // 上边应该都懂，下面就是创建了一个点击事件，目的是点击省份子区域的时候可以切换到省份地图，省份的json文件自己下载。
        this.myChart.on('click', chinaParam => {
          const code = provinces[provincesText.indexOf(chinaParam.name)] || 100000
          this.getProvinceMapOpt(code, provincesName[provincesText.indexOf(chinaParam.name)])
        })
        window.onresize = function () {
          this.myChart.resize()
        }
      })
    },
    // 显示各省地图
    getProvinceMapOpt (provinceAlphabet, name) {
      const newJson = require('../../../../src/assets/map/' + provinceAlphabet + '.json')
      echarts.registerMap(name, newJson)
      this.changeOptions(name)
      this.myChart.setOption(this.distributionOptions, true)
    }
  },
  mounted () {
    this.init()
  }

}
</script>

<style scoped>

</style>
