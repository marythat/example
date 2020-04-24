<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :close-on-click-modal="false"
    width="900px">
    <el-row>
      <el-form ref="projectSearchForm" :model="searchForm" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="searchForm.projectName" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="margin-left:20px">
            <el-button type="primary" @click="executeQuery" icon="el-icon-search" style="float: left">查询</el-button>
            <el-button type="reset" icon="el-icon-refresh-left" @click="resetForm('projectSearchForm')" >重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-row>
    <el-row>
      <el-table :data="tableData">
        <el-table-column type="index" label="#"/>
        <el-table-column prop="projectId" label="项目ID" v-if="false"/>
        <el-table-column prop="projectName" label="项目名称"/>
        <el-table-column prop="count" label="项目申请总数">
          <template slot-scope="scope">
            <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,0)">{{scope.row.count}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="draft" label="草稿">
          <template slot-scope="scope">
            <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,1)">{{scope.row.draft}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="approval" label="审批中">
          <template slot-scope="scope">
            <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,2)">{{scope.row.approval}}</span>
          </template>
        </el-table-column>
        <template v-for='(col) in tableColumn'>
          <el-table-column :prop="col.prop" :label="col.label">
            <template slot-scope="scope">
              <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,99,col.prop,col.label)">{{scope.row[col.prop]?scope.row[col.prop]:0}}</span>
            </template>
          </el-table-column>
        </template>
        <el-table-column prop="passed" label="通过">
          <template slot-scope="scope">
            <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,3)">{{scope.row.passed}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="return" label="退回修改">
          <template slot-scope="scope">
            <span style="cursor: pointer;color: #409EFF" @click="query(scope.row,5)">{{scope.row.return}}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <table-pagination :pagination="pagination" @current-change="pageChange"/>
  </el-dialog>
</template>

<style>
</style>

<script>
  import ApplicationRecordForm from "./ApplicationRecordForm";

  export default {
    components: {ApplicationRecordForm},
    props: ['refreshFunction'],
    data() {
      return {
        tableData: [],
        visible:false,
        title: '',
        searchForm:{
          projectName:'',
          projectId:''
        },
        tableColumn:[],
        pagination: {currentPage: 1, currentSize: 10, total: 0},
      }
    },
    created() {

    },
    methods: {
      add(){
        this.tableColumn=[];
        this.searchForm={
          projectName:'',
          id:''
        };
        this.loadData();
      },
      addOne(projectId,tableColumn){
        this.tableColumn=tableColumn;
        this.searchForm={
          projectName:'',
          projectId:''
        };
        this.searchForm.projectId=projectId;
        this.loadData();
      },
      executeQuery() {
          this.loadData();
      },
      pageChange(pageNo) {
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      loadData(){
        this.searchForm.pageNo = this.pagination.currentPage;
        this.searchForm.pageSize = this.pagination.currentSize;
        let searchForm=Object.assign({},this.searchForm);
        if(searchForm.projectName!=""&&searchForm.projectName!=undefined){
          searchForm.projectName = "*"+searchForm.projectName+"*";
        }
        this.$http.get("applicationRecord/details",{params: searchForm}).then(response => {
          if (response.data.success) {
            this.tableData = response.data.data.records;
            this.pagination = {
              total: response.data.data.total,
              currentPage: response.data.data.current,
              currentSize: response.data.data.size
            };
          } else {
            this.$message(response.data.message);
          }
        })
      },
      query(data,type,next,nextName){
        this.refreshFunction(data,type,next,nextName);
        this.visible=false;
      }
    }
  };
</script>
