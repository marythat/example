<template>
  <div>
    <el-row>
      <el-col :span="6">
        <el-tag effect="dark" color="#f56c6c" style="border-color:#f56c6c">
          <div style="margin-top: 30px;font-size: 15px">累计申报</div>
          <div style="font-size: 30px">{{map.first}}</div>
        </el-tag>
      </el-col>
      <el-col :span="6">
        <el-tag effect="dark" color="#40e5ff" style="border-color:#40e5ff">
          <div style="margin-top: 30px;font-size: 15px">累计企业</div>
          <div style="font-size: 30px">{{map.second}}</div>
        </el-tag>
      </el-col>
      <el-col :span="6">
        <el-tag effect="dark" color="#409eff" style="border-color:#409eff">
          <div style="margin-top: 30px;font-size: 15px">开通业务</div>
          <div style="font-size: 30px">{{map.third}}</div>
        </el-tag>
      </el-col>
      <el-col :span="6">
        <el-tag effect="dark" color="#67c23a" style="border-color:#67c23a">
          <div style="margin-top: 30px;font-size: 15px">已通过</div>
          <div style="font-size: 30px">{{map.fourth}}</div>
        </el-tag>
      </el-col>
    </el-row>
    <div style="margin-top: 50px">
      <el-row>
        <el-col :span="12">
          <div style="margin-left: 50%">
            <span>申报量按月份统计</span>
            <el-select v-model="year" placeholder="请选择" size="mini" @change="changeValue1">
              <el-option
                v-for="item in options"
                :key="item.createTime"
                :value="item.createTime">
              </el-option>
            </el-select>
            <el-button type="primary" size="mini" @click="chartSelect1">搜索</el-button>
          </div>
          <Chart :dataSource="dataSource1"></Chart>
        </el-col>
        <el-col :span="12">
          <div style="margin-left: 50%;">
            <span style="margin-right: 20px">申报量按镇街统计</span>
            <el-input v-model="street" placeholder="请输入" size="mini" style="width: 173px">
            </el-input>
            <el-button type="primary" size="mini" style="float: right;margin-right: 24px" @click="chartSelect2">搜索</el-button>
          </div>
          <Chart :dataSource="dataSource2"></Chart>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import Chart from '@/components/Chart'

  export default {
    name: "Analysis",
    components: {
      Chart
    },
    data() {
      return {
        dataSource1:[],
        dataSource2:[],
        street:null,
        year:null,
        loading: true,
        map:[],
        options: [],
        value: ''
      }
    },
    created() {
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
      this.initRadar()
    },
    methods: {
      initRadar() {
        let that = this
        this.$http.get('/report/selectDates').then(res => {
          if(res.data.success&&res.data.data.length>0) {
            this.options = res.data.data
            this.year = this.options[0].createTime
            that.chartSelect1(this.options[0].createTime)
          }
        });
        that.chartSelect2()
        this.$http.get('/report/reportCount').then(res => {
          if(res.data.success) {
            this.map = res.data.data
          }
        });
      },
      changeValue1(year){
        this.year = year
      },
      chartSelect1() {
        this.$http.get('/report/applyReportByMonth', {
          params: Object.assign({}, {year:this.year})
        }).then(res => {
          if(res.data.success&&res.data.data.length>0) {
            this.dataSource1 = res.data.data
          }
        });
      },
      chartSelect2(year) {
        this.$http.get('/report/applyReportByStreet', {
          params: Object.assign({}, {street:this.street})
        }).then(res => {
          if(res.data.success&&res.data.data.length>0) {
            this.dataSource2 = res.data.data
          }
        });
      },
    }
  }
</script>
<style>
  .el-tag--dark{
    width: 98%;
    text-align: center;
    height: 150px;
  }
</style>
