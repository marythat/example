<template>
  <div>
    <v-chart :forceFit="true" :height="height" :data="dataSource" :scale="scale">
      <v-tooltip />
      <v-axis />
      <v-bar position="item*value" />
    </v-chart>
  </div>
</template>

<script>
  const DataSet = require('@antv/data-set');
  const data = [
    { year: '1951 年', sales: 38 },
    { year: '1952 年', sales: 52 },
    { year: '1956 年', sales: 61 },
    { year: '1957 年', sales: 145 },
    { year: '1958 年', sales: 48 },
    { year: '1959 年', sales: 38 },
    { year: '1960 年', sales: 38 },
    { year: '1962 年', sales: 38 },
  ];

  const scale = [{
    dataKey: 'value',
    tickInterval: 10,
  }];

  export default {
    props: {
      dataSource: {
        type: Array,
        default: () => []
      }
    },
    data() {
      return {
        scale,
        height: 500,
      };
    },
    computed: {
      data() {
        let data=this.dataSource
        let dv = new DataSet.View().source(data);
        dv.transform({
          type: 'fold',
          fields: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          key: '月份',
          value: '数量',
        });
        return dv.rows;
      }
    }
  };
</script>
<!--
<template>
  <div>
    <v-chart :forceFit="true" :height="height" :data="data">
      <v-tooltip />
      <v-axis />
      <v-legend />
      <v-stack-bar position="月份*数量" color="name" />
    </v-chart>
  </div>
</template>

<script>
  const DataSet = require('@antv/data-set');

  const sourceData = [
    { name: 'London', 'Jan.': 18.9, 'Feb.': 28.8, 'Mar.': 39.3, 'Apr.': 81.4, 'May': 47, 'Jun.': 20.3, 'Jul.': 24, 'Aug.': 35.6 },
    { name: 'Berlin', 'Jan.': 12.4, 'Feb.': 23.2, 'Mar.': 34.5, 'Apr.': 99.7, 'May': 52.6, 'Jun.': 35.5, 'Jul.': 37.4, 'Aug.': 42.4 },
  ];


  export default {
    props: {
      dataSource: {
        type: Array,
        default: () => []
      }
    },
    data() {
      return {
        height: 400,
      };
    },
    computed: {
      data() {
        let data=this.dataSource
        let finishObj={name:'按期完成'}
        let otherObj={name:'计划完成'}
        for (let i = 0; i < data.size; i++) {
          finishObj[data[i].item]=data[i].finish
          finishObj[data[i].item]=data[i].finish
          otherObj[data[i].item]=data[i].other
          otherObj[data[i].item]=data[i].other
        }
        data=[]
        data.push(otherObj)
        data.push(finishObj)
        let dv = new DataSet.View().source(data);
        dv.transform({
          type: 'fold',
          fields: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          key: '月份',
          value: '数量',
        });
        return dv.rows;
      }
    }
  };
</script>-->
