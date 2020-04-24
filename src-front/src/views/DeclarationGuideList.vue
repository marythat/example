<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :close-on-click-modal="false"
    width="600px">
    <el-row>
      <el-table :data="tableData">
        <el-table-column type="index" label="#"/>
        <el-table-column prop="id" label="文件ID" width="120px;" v-if="false"/>
        <el-table-column prop="fileName" label="文件名称"/>
        <el-table-column prop="fileName" label="操作" width="200px;" align="center">
          <template slot-scope="scope" >
            <el-button size="small" type="primary"  @click="downloadFile(scope.row)">点击下载</el-button>
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
    data() {
      return {
        tableData: [],
        visible:false,
        title: '申报指南文件列表',
        pagination: {currentPage: 1, currentSize: 10, total: 0},
        declarationGuidData:{}
      }
    },
    created() {

    },
    methods: {
      pageChange(pageNo) {
        this.pagination.currentPage = pageNo;
        this.getCommAttachmentList(this.declarationGuidData);
      },
      downloadFile(row){
        let name=row.fileName;
        let filePath=row.filePath;
        let elink = document.createElement('a')
        elink.download =name;
        elink.style.display = 'none';
        elink.href = this.$api_host+"/commAttachment/ftpDownload/"+filePath;
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href); // 释放URL 对象
        document.body.removeChild(elink);
      },
      loadData(data){
        this.tableData=[];
        this.declarationGuidData=data;
        this.pagination={currentPage: 1, currentSize: 10, total: 0};
        this.getCommAttachmentList(data);
      },
      getCommAttachmentList(data){
        let queryData = {pageNo:this.pagination.currentPage,pageSize:this.pagination.currentSize,relatedId: data.id,related_table:"nst_project_information",type:"2"};
        let self = this;
        this.$http.get("commAttachment/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            self.tableData=response.data.data.records;
            self.pagination.total=response.data.data.total;
          }
        }).catch(e=>{
        });
      }
    }
  };
</script>
