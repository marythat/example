<template>
    <el-container>
      <el-main>
        <el-row>
          <el-table  :data="tableData">
            <el-table-column type="index" label="#"/>
            <el-table-column prop="flowName"  label="流程名称" />
            <el-table-column prop="comments" label="审批意见"/>
            <el-table-column prop="executorName" label="执行人名称"/>
            <el-table-column prop="createTime" label="执行时间"/>
            <el-table-column prop="approvalResult" label="批准结果"/>
            <el-table-column label="操作" width="140px;">
              <template slot-scope="scope">
                <el-popconfirm title="即将删除记录, 是否继续?" @onConfirm="handleDelete(scope.row)">
                  <row-operation slot="reference" icon="el-icon-delete" type="danger" tips="删除" />
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
        <!--分页-->
        <table-pagination :pagination="pagination" @current-change="pageChange"/>
      </el-main>
    </el-container>
</template>

<script>
  export default {
    data() {
      return {
        visible:false,
        projectName:'',
        tableData: [],
        searchForm: {column:'flowName,createTime',order:'desc'},
        pagination: {currentPage: 1,currentSize: 10,total: 0}
      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData() {
        let id =this.$route.query.id;
        let queryData = {applicationId:id,column:"createTime",order:"DESC"};
        this.$http.get("/flowExecuteRecord/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            this.tableData=response.data.data.records;
          }
        }).catch(e=>{
          this.$message(e.responseText);
        })
      },
      handleDelete(row) {
        this.$http.delete("projectFlowNode/remove/" + row.id).then(response => {
          this.loadData();
          this.$message.success(response.data.message);
        })
      },
      pageChange(pageNo){
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
    }
  };
</script>
