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
        <el-table-column prop="isMust" label="必要附件">
          <template slot-scope="scope" >
            <span style="color: #dc0015">{{scope.row.isMust}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isUpload" label="是否上传"/>
        <el-table-column label="操作" width="200px;" align="center">
          <template slot-scope="scope" >
            <row-operation icon="el-icon-upload2" @click="mustUploadFile(scope.row)" :disabled="scope.row.isUpload=='是'?true:false" tips="点击上传"/>
          </template>
        </el-table-column>
      </el-table>
      <table-pagination :pagination="pagination" @current-change="pageChange"/>
      <div style="margin-top:20px;float: right">
        <el-button type="primary" @click="uploadFile">其它文件上传</el-button>
      </div>
    </el-row>
  </el-dialog>
</template>

<style>
</style>

<script>
  export default {
    data() {
      return {
        tableData: [],
        tableDataAll:[],
        visible:false,
        title: '上传文件列表',
        pagination: {currentPage: 1, currentSize: 10, total: 0},
      }
    },
    created() {

    },
    methods: {
      mustUploadFile(row){
        document.getElementById("show-iframe").contentWindow.mustFileExtendName=row.fileName;
        document.getElementById("show-iframe").contentDocument.getElementById("MustUploadFile").click();
        this.visible=false;
      },
      uploadFile(){
        document.getElementById("show-iframe").contentDocument.getElementById("uploadFile").click();
        this.visible=false;
      },
      loadData(data){
        let mustFileExtendNames=document.getElementById("show-iframe").contentWindow.mustFileExtendNames;
        this.tableDataAll=[];
        if(data!=null&&data!=undefined){
          let filds=data.split(",");
          for(let i=0;i<filds.length;i++){
            let isUpload='否';
            if(mustFileExtendNames&&mustFileExtendNames.indexOf(filds[i])!=-1){
              isUpload="是";
            }
            this.tableDataAll.push({fileName:filds[i],isMust:"是",isUpload:isUpload});
          }
          this.pageChange(this.pagination.currentPage);
          this.pagination.total=this.tableDataAll.length;
        }
      },
      pageChange(val){
        this.pagination.currentPage=val;
        this.tableData=[];
        for(let i=0;i<this.tableDataAll.length;i++){
          let start=(val-1)*10;
          let end=val*10;
          if(i>=start&&i<end)this.tableData.push(this.tableDataAll[i]);
        }
      }
    }
  };
</script>
