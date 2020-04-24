<template>
  <el-drawer
    size="800px"
    :visible.sync="visible"
    :with-header="false">
    <el-main>
      <h3 style="margin-top: 0">项目【{{projectName}}】的流程节点</h3>
      <div style="border-bottom: 2px solid #dddddd;width: 100%;margin-bottom: 12px;" />
      <!-- 操作按钮区域 -->
      <el-row>
        <el-button size="small" type="primary" @click="createNew" icon="el-icon-plus" plain
                   style="margin-bottom: 10px">新增</el-button>
      </el-row>
      <!-- 数据展示区域 -->
      <el-row>
        <el-table  :data="tableData">
          <el-table-column type="index" label="#"/>
          <el-table-column prop="flowName"  label="节点名称" />
          <el-table-column prop="flowSeq" label="序号"/>
          <el-table-column prop="typeStr" label="节点类型"/>
          <el-table-column label="操作" width="140px;">
            <template slot-scope="scope">
              <el-button size="small" type="primary"  @click="handleEdit(scope.row)"  plain>编辑</el-button>
              <el-popconfirm title="即将删除记录, 是否继续?" @onConfirm="handleDelete(scope.row)">
                <el-button  slot="reference" size="small" type="danger" plain>删除</el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <!--分页-->
      <table-pagination :pagination="pagination" @current-change="pageChange" />
    </el-main>

    <!-- 其他模态组件引入区域 -->
    <project-flow-node-form ref="modalForm" :refreshFunction="loadData"/>
  </el-drawer>
</template>

<style>
</style>

<script>
  import ProjectFlowNodeForm from "./ProjectFlowNodeForm";

  export default {
    components: {ProjectFlowNodeForm},
    data() {
      return {
        visible:false,
        projectName:'',
        tableData: [],
        searchForm: {column:'flowSeq',order:'asc'},
        pagination: {currentPage: 1,currentSize: 10,total: 0}
      }
    },
    methods: {
      loadData() {
        this.searchForm.pageNo = this.pagination.currentPage;
        this.searchForm.pageSize = this.pagination.currentSize;
        this.$http.get("projectFlowNode/list", {params:this.searchForm}).then(response => {
          this.tableData = response.data.data.records;
          this.pagination = {total:response.data.data.total,currentPage:response.data.data.current,currentSize:response.data.data.size};
        })
      },
      createNew() {
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.model.projectId = this.searchForm.projectId;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.add();
      },
      handleEdit(row){
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit(row);
        },
      handleDelete(row) {
        this.$http.delete("projectFlowNode/remove/" + row.id).then(response => {
          this.loadData();
          this.$message.success(response.data.message);
        })
      },
      executeQuery() {
        this.loadData();
      },
      pageChange(pageNo){
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  };
</script>
