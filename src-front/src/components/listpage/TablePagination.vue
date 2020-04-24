<template>
  <el-row style="text-align: right;padding-top: 10px;">
    <el-pagination
      background
      layout="prev, pager, next"
      @current-change="pageChange"
      :hide-on-single-page="true"
      :current-page="pagination.currentPage"
      :page-size="pagination.currentSize"
      :total="pagination.total">
    </el-pagination>
  </el-row>
</template>

<script>
  export default {
    props:{
      pagination:{
        type:Object,
        default(){
          return {
            currentPage: 1,
            currentSize: 10,
            total: 0
          }
        }
      },
      currentChange:{
        type:Function
      }
    },
    methods:{
      pageChange (pageNo){
        this.$emit("current-change",pageNo);
      }
    }
  }
</script>
