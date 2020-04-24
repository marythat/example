<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :modal="false"
    :close-on-click-modal="false"
    width="450px"
     style="margin-top: 20px">
    <el-form ref="form" :model="model"  label-width="150px">
      <el-row >
        <el-col>
          <el-form-item label="请选择要上传的附件">
            <el-upload
              ref="upload"
              class="upload-demo"
              :action=host
              :on-preview="handlePreview"
              :on-progress="handleProgress"
              :limit="1"
              :data="{relatedTable:searchForm.relatedTable,relatedId:searchForm.related_id}"
              :auto-upload=true
              :on-success="uploadSuccess"
              :on-exceed="handleExceed">
              <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
  export default {
    name: 'uploadFile',
    props: ['refreshFunction'],
    data() {
      return {
        tableIsShow: true,
        addIsShow: true,
        visible: false,//弹出框是否显示
        disableSubmit: false,//提交按钮
        model:{},
        searchForm:{},
       // uploadParams:{'projectId':projectId},
        title: '',
        host:this.$api_host+'/commAttachment/ftpStore',
      };
    },
    methods: {
      handlePreview(file) {
      },
      handleExceed(files, fileList) {
        this.$message.warning({
          showClose: true,
          message: '当前限制选择 1 个文件'
        });
      },
      handleProgress(event, file, fileList){

      },
      uploadSuccess(response, file, fileList) {
        if (response.code == 200) {
          this.$message.success({
            showClose: true,
            message: "上传成功！"
          });
          this.visible=false;
          //刷新父页面
          if(this.refreshFunction){
            this.refreshFunction();
          }
        }
      },
      stopScrollFun(evt) {
        evt = evt || window.event;
        if (evt.preventDefault) {
          // Firefox
          evt.preventDefault();
          evt.stopPropagation();
        } else {
          // IE
          evt.cancelBubble = true;
          evt.returnValue = false;
        }
        return false;
      },

    },
  }
</script>
