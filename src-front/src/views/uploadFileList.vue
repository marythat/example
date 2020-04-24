<template>
  <el-dialog
    :title="title"
    @close="close"
    :visible.sync="visible"
    :close-on-click-modal="false"
    width="1050px">
  <el-container>
    <el-main>
      <el-row style="width: 100%;min-width: 1000px">
        <el-form ref="searchForm" :model="searchForm" label-width="80px">
          <el-col :span="6">
            <el-form-item label="文件名称" prop="projectName">
              <el-input v-model="searchForm.fileName" placeholder="请输入文件名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="10" align="right">
            <el-form-item>
              <template slot-scope="scope">
                <el-button type="primary" @click="executeQuery" icon="el-icon-search">查询</el-button>
                <el-button type="primary"  @click="upLoadFile(scope.row)" icon="el-icon-upload2">上传附件</el-button>
              </template>

            </el-form-item>

          </el-col>
         </el-form>
       </el-row>
      <el-row>
        <p style="padding-bottom: 20px; width: 100%;">
          <span style="float:left;">附件表</span>
        </p>
        <el-table :data="tableData" :header-cell-style="headClass"
          border style="width: 100%"  max-height="500px"
        >
          <el-table-column type="index" label="附件" width="50px">
          </el-table-column>
          <el-table-column prop="relatedTable" label="关联表名称" >
          </el-table-column>
          <el-table-column prop="fileName" label="文件名称">
          </el-table-column>
          <el-table-column label="操作" width="160px;">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
              <el-button
                size="mini"
                type="primary"
                @click="downloadFile(scope.$index, scope.row)">下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <!--分页-->
      <table-pagination :pagination="pagination" @current-change="pageChange"/>
    </el-main>
    <upload-file ref="modalForm" :refreshFunction="loadData"/>
  </el-container>
  </el-dialog>
</template>

<style>
  div.el-dialog {
    color:blue;
  }
</style>

<script>
  import uploadFile from './uploadFile';
  export default {
    components: {
      uploadFile
    },
    data() {
      return {
        title:'',
        visible:false,
        tableData: [],
        searchForm: {},
        pagination: {currentPage: 1, currentSize: 10, total: 0}
      }
    },
    methods: {
      add() {
        this.loadData();
        this.visible=true;
      },
      loadData() {
        this.searchForm.pageNo = this.pagination.currentPage;
        this.searchForm.pageSize = this.pagination.currentSize;
        this.$http.get("commAttachment/list", {params: this.searchForm}).then(response => {
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
      handleEdit(idx, row){
        this.$ele.MessageBox(row.name);
      },
      handleDelete(idx, row){
        this.$http.delete("commAttachment/remove/" + row.id).then(response => {
          this.loadData();
          this.$message.success(response.data.message);
        })
      },
      executeQuery(){
        this.loadData();
      },
      onSubmit(){
        this.$ele.Message("submit")
      },
      close () {
        this.visible = false;
      },
       // 表头样式设置
      headClass () {
        return 'text-align: center;background:#eef1f6;'
      },
      pageChange(pageNo) {
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
       upLoadFile(row) {
        this.$refs.modalForm.title = "附件上传";
        this.$refs.modalForm.searchForm.related_id=this.searchForm.related_id;//该id为执行流程id
         this.$refs.modalForm.searchForm.relatedTable=this.searchForm.relatedTable;
        this.$refs.modalForm.visible = true;
      },
      downloadFile(idx, row){
        this.$http.get("commAttachment/download/" + row.filePath).then(response => {
          const content = response.data
          const blob = new Blob([content])
          const fileName =fileName
          if ('download' in document.createElement('a')) { // 非IE下载
            const elink = document.createElement('a')
            elink.download = row.fileName;
            elink.style.display = 'none'
            elink.href = URL.createObjectURL(blob)
            document.body.appendChild(elink)
            elink.click()
            URL.revokeObjectURL(elink.href) // 释放URL 对象
            document.body.removeChild(elink)
          } else { // IE10+下载
            navigator.msSaveBlob(blob, fileName)
          }
        })
      }
    }
  };
</script>
<style>
  .el-dialog__body {
    padding: 10px 10px;
  }
</style>
