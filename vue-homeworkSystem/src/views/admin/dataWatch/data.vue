<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <!-- <github-corner class="github-corner" /> -->

      <panel-group @handleSetLineChartData="handleSetLineChartData" />

      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <div class="title">{{this.title}}</div>
        <line-chart :chart-data="lineChartData" />
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <div class="title">博客热度榜</div>
            <radar-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <div class="title">博客分类统计</div>
            <pie-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <div class="title">近七日博客数据</div>
            <bar-chart />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
// import GithubCorner from '@/components/GithubCorner'
import PanelGroup from '../../../dashboard/PanelGroup'
import LineChart from '../../../dashboard/LineChart'
import RadarChart from '@/components/Echarts/RadarChart'
import PieChart from '@/components/Echarts/PieChart'
import BarChart from '@/components/Echarts/BarChart'

const lineChartData = {
  views: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  users: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  blogs: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}
const titles = {
  views: '浏览量',
  messages: '留言量',
  users: '用户量',
  blogs: '博客量'
}
export default {
  name: 'Dashboard',
  components: {
    // GithubCorner,
    PanelGroup,
    LineChart,
    RadarChart,
    PieChart,
    BarChart
  },
  data () {
    return {
      lineChartData: lineChartData.views,
      title: '近七日数据'
    }
  },
  methods: {
    async handleSetLineChartData (type) {
      this.title = '近七日新增' + titles[type]
      const { data: res } = await this.$http.get(`/api/forum/manager/last7daysdata?type=${type}`)
      if (!res.success) {
        return this.$message.error('获取数据失败!')
      } else {
        lineChartData[type].expectedData = res.data[1]
        lineChartData[type].actualData = res.data[0]
      }
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .title {
    line-height: 18px;
    color: #ff9933;
    font-size: 16px;
    margin-bottom: 12px;
  }
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
