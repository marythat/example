<template>
  <el-container style="background-color: #F0F2F5">
    <!--顶部流程信息-->
    <div style="width:100%;background: #F0F2F5;">
      <div style="background: #FFFFFF;">
        <div style="width: 62%;margin-left:10%;padding-top:20px;padding-bottom:10px">
          <el-steps :active="arthurSteps">
            <el-step v-for="(item,index) in projectFlowNodeData" :title="item.flowName" :key="index" :status="index==arthurSteps-1?processStatus:index<arthurSteps-1?'success':'process '"></el-step>
          </el-steps>
        </div>
      </div>

      <div class="el-input__inner1">
        <el-row>
          <el-col style="width:33%;background: #FFFFFF;margin-top:1%;height: 740px;float: right" v-if="show">
            <template>
              <div class="stepsTitle" style="margin: 20px" id="arthurClass2">
                <div style="float:left;width:2px;height:20px; background:#219AFF;"></div>
                <span >审批流程</span>
              </div>
              <div class="stepComponent" style="overflow-y:scroll;height:650px">
                <!--填报审批信息 begin-->
                <div v-show="auditInfo&&showPer">
                  <el-form :model="formData" status-icon label-width="100px">
                    <el-form-item label="意见:">
                      <el-input v-model="formData.comments" id="comments" :autosize="{ minRows:5, maxRows:10}"
                                type="textarea" placeholder="请输入意见" style="width: 90%;margin-top: 15px;">
                      </el-input>
                    </el-form-item>

                    <el-form-item label="转办至:">
                      <el-input placeholder="点击右侧按钮选择用户" v-model="multipleSelectionNames" :disabled="true" style="float: left;width: 220px;">
                        <el-button  slot="append" icon="el-icon-search" @click="onSearch" style="height:20px">选择</el-button>
                      </el-input>
                    </el-form-item>

                    <el-form-item label="审批附件:">
                      <el-upload
                        ref="upload"
                        class="upload-demo"
                        :action="host"
                        :headers="headers"
                        :file-list="fileList"
                        :disabled="uploadDisabled"
                        :show-file-list=true
                        :on-success="uploadSuccess"
                        :on-remove="onRemove"
                        :before-upload="beforeUpload"
                        :auto-upload=true>
                        <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
                      </el-upload>
                    </el-form-item>

                    <el-form-item>
                      <el-button type="primary" @click="approvalProcessing('1')" v-if="!showTurnTo"
                      :disabled="applicationRecordFormData.status === 'CLOSED'||applicationRecordFormData.status === 'REJECTED'||applicationRecordFormData.status === 'RETURN'">受理</el-button>
                      <el-button type="primary" @click="approvalProcessing('1')" v-else
                      :disabled="applicationRecordFormData.status === 'CLOSED'||applicationRecordFormData.status === 'REJECTED'||applicationRecordFormData.status === 'RETURN'">转办</el-button>
<!--                      <el-button type="primary" @click="approvalProcessing('2')"
                      :disabled="applicationRecordFormData.status === 'CLOSED'||applicationRecordFormData.status === 'REJECTED'||applicationRecordFormData.status === 'RETURN'">不受理</el-button>-->
                      <el-button type="primary" @click="approvalProcessing('3')"
                      :disabled="applicationRecordFormData.status === 'CLOSED'||applicationRecordFormData.status === 'REJECTED'||applicationRecordFormData.status === 'RETURN'">退回企业</el-button>
                    </el-form-item>
                  </el-form>
                </div>

                <el-card class="box-card">
                  <div >
                    <span style="text-align: left">执行过程</span>
                  </div>

                  <!--填报审批信息 end-->
                  <div class="examineInfo" style="margin-top: 10px" >
                    <el-steps  direction="vertical" :active="arthurSteps" >
                      <el-step :id="tag.id" :title="tag.label" v-for="(tag,index) in flowExecuteRecordData" :key="index">
                        <!--表信息内容-->
                        <template slot="description" >
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="processing_content">
                            <tr>
                              <td style="color:#98A6BE">
                                <div class="processing_content_detail" style="float:left;width:90%">
                                    <el-card class="box-card">
                                      <span style="font-size:15px;color:blue;">
                                        <i class="el-icon-circle-check"></i>{{tag.flowName}}
                                      </span><br>
                                    </el-card>
                                  <div>
                                    <span>{{index==flowExecuteRecordData.length-1?"提交人：":"审批人："}}{{tag.createPhone==null?tag.createName:tag.createName+" "+tag.createPhone}}</span><br>
                                    <span class="textType">操作：{{tag.approvalResultStr}}</span><br>
                                    <span class="textType">{{index==flowExecuteRecordData.length-1?"企业名称：":"意见："}}{{tag.comments}}</span><br>
                                    <span>&nbsp;&nbsp;&nbsp;<i class="el-icon-time"></i>{{tag.createTime}}</span>
                                    <div v-for="(tagItem,indexItem) in flowExecuteRecordData[index].commAttachmentList" :key="indexItem">
                                      <span style="font-size:15px;color:#34C6B3;cursor: pointer"  @click="downloadFile(tagItem.fileName,tagItem.filePath)">
                                        <i class="el-icon-document"></i>
                                        {{tagItem.fileName}}</span><br>
                                    </div>

                                  </div>
                                </div>
                              </td>
                            </tr>
                          </table>
                        </template>
                        <!--表信息结束-->
                      </el-step>
                    </el-steps>
                  </div>
                </el-card>
              </div>
            </template>
          </el-col>
          <el-col :style="applicationCss">
            <el-row >
              <el-col id="tableView" >
                <el-main >
                  <el-row>
                    <el-col style="height: 700px;">
                      <iframe align="left" v-show="true" id="show-iframe"  frameborder=0 name="showHere"
                              scrolling=auto height="100%" width="100%"></iframe>
                    </el-col>
                  </el-row>
                </el-main>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </div>
    <departUserList ref="departUserList" @ok="modalFormOk"></departUserList>
    <uploadFileList ref="modalForm" :refreshFunction="init"/>
  </el-container>

</template>

<script>
  import departUserList from '../components/DepartUserList'
  import uploadFileList from './uploadFileList'
  export default {
    name: 'FlowPath',
    created(){
      //项目初始化
      this.init();
    },
    watch: {
      multipleSelectionNames(val){
        if(val){
          this.showTurnTo = true
        }else{
          this.showTurnTo = false
        }
      },
      flowExecuteRecordData(val){
        if(val){
          this.$nextTick(() => {
            this.flowExecuteRecordData1 ();
          });
        }
      },
      url (val) {
        let that = this
        if(val){
          that.$http.get(val).then(response=>{
            let iframe = document.getElementById("show-iframe")
            var idoc = iframe.contentDocument || iframe.contentWindow.document
            idoc.open()
            idoc.writeln(response.data)
            idoc.close()
          })
        }
      }
    },
    components: {
      uploadFileList,
      departUserList
    },
    data() {
      return {
        showTurnTo:false,
        multipleSelectionNames:'',
        multipleSelectionIds:'',
        table: true,
        dialog: false,
        loading: false,
        show:false,//是否显示审批信息
        auditInfo:true,//审批信息操作是否填写
        viewInfo1:true,
        viewInfo2:false,
        detailed:false,//审批详细内容
        tableSize:80,//表单起始百分比宽度
        flowPathSize:20,//审批流程百分比宽度
        activeNames:[''],
        scrollHeight:'0px',
        dialogUpLoading:false,
        applicationCss:"width:65%;background: #FFFFFF;margin: 1%;height: 740px;margin-left:15%",
        arthurSteps:0,
        projectFlowNodeData:[],
        projectFlowNode:{},
        projectInformation:{},
        commAttachment:{},
        applicationRecordFormData:{},
        flowExecuteRecordData:[],
        flowExecuteRecord:{},
        url:'',
        formData:{},
        headers:{token:window.localStorage.token,iv:window.localStorage.iv},
        fileList:[],
        host:this.$api_host+'/commAttachment/ftpStore',
        uploadDisabled:false,
        processStatus:'process',
        searchForm: {
          column: '',
          order: '',
          companyTag:'',
          projectName:'',
          projectId:'',
          projectCode:'',
          companyName:'',
          belongDept:'',
          commitDate:[],
          status:'',
          projectType:'',
          commitDate_start:'',
          commitDate_end:'',
          checked: '',
          nextNodeName:'',
          nextNodeId:''
        },
      };
    },
    created () {
      this.showPer = this.$route.query.show != "false";
      for(let key in this.searchForm){
        if(this.$route.query[key]&&this.$route.query[key]!=undefined&&this.$route.query[key]!="")this.searchForm[key]=this.$route.query[key];
      }
      this.getApplicationRecordFormData();
    },
    methods: {
      init(arg){
        this.$http.post('/projectFlowNode/selectList').then(response => {
          this.options1=response.data;
        }).catch(function (response) {
        });

      },
      validate(cb) {
        return this.$refs.fm.validate(cb);
      },
      clearValidate() {
        this.$refs.fm.resetFields();
        this.$refs.fm.clearValidate();
      },
      handleEdit(idx, row){
        this.$ele.MessageBox(row.name);
      },// 表头样式设置
      headClass () {
        return 'text-align: center;background:#eef1f6;'
      },
      handleDelete(){
        this.$ele.Message("delete")
      },
      addUpLoading(){
        this.$refs.modalForm.title = "附件管理";
        this.$refs.modalForm.searchForm.related_id=this.applicationRecordFormData.id;//应该为执行id
        this.$refs.modalForm.searchForm.relatedTable="nst_flow_execute_record";
        this.$refs.modalForm.add();
      },
      handleChange(val) {
        //console.log(val);
      },
      getHtml(self,projectId,id){
        let queryData = {relatedId: projectId,type:"1"};
        this.$http.get("/commAttachment/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            if(response.data.data.records.length>0){
              self.commAttachment=response.data.data.records[0];
              if(self.commAttachment!=null){
                self.url= self.$api_host+"/commAttachment/download/"+self.commAttachment.filePath+"?id="+id;
              }
            }
          }
        }).catch(e=>{
          //console.log(e.responseText);
          this.$message(e.responseText);
        })
      },
      getProjectFlowNodeData(self,projectId){
        let queryData = {projectId: projectId,column:'flow_seq',order:'ASC'};
        this.$http.get("/projectFlowNode/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            self.projectFlowNodeData=response.data.data.records;
            for(let i=0;i<self.projectFlowNodeData.length;i++){
              if(self.projectFlowNodeData[i].id==self.applicationRecordFormData.curNode){
                self.projectFlowNode=self.projectFlowNodeData[i];
                self.arthurSteps=i+1;
              }
            }
          }
        }).catch(e=>{
          //console.log(e.responseText);
          this.$message(e.responseText);
        })
      },
      getApplicationRecordFormData(){
        let id =this.$route.query.id;
        let queryData = {id: id};
        let self=this;
        this.$http.get("/applicationRecord/queryOne",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            if(response.data.data.records.length>0){
              self.applicationRecordFormData=response.data.data.records[0];
              if(self.applicationRecordFormData.status=="RETURN"||self.applicationRecordFormData.status=="REJECTED") {
                self.processStatus = "error";
              }else{
                self.processStatus="success";
              }
              self.getHtml(self,self.applicationRecordFormData.projectId,self.applicationRecordFormData.id);
              self.getProjectFlowNodeData(self,self.applicationRecordFormData.projectId);
              self.getFlowExecuteRecordData(self);
              if(response.data.data.records[0].status  =="PASSED"||response.data.data.records[0].status  == "REJECTED"||response.data.data.records[0].status  == "RETURN"){
                self.auditInfo=false;
              }
            }
          }
        }).catch(e=>{
         // console.log(e.responseText);
          this.$message(e.responseText);
        })
      },
      beforeUpload(file){
        const isLt2M = file.size / 1024 / 1024 < 30;
        if(!isLt2M) {
          this.$message({
            message: '上传文件大小不能超过 30MB!',
            type: 'warning'
          });
          return isLt2M;
        }
        this.uploadDisabled=true;
      },
      getFlowExecuteRecordData(self){
        let queryData = {applicationId: self.applicationRecordFormData.id,column:"createTime",order:"DESC"};
        this.$http.get("/flowExecuteRecord/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            self.flowExecuteRecordData=response.data.data.records;
            if(self.flowExecuteRecordData.length>0){
              self.applicationCss="width:65%;background: #FFFFFF;margin: 1%;height: 740px";
              self.show=true;
            }else{
              self.applicationCss="width:65%;background: #FFFFFF;margin: 1%;height: 740px;margin-left:15%";
              self.show=false;
            }
          }
        }).catch(e=>{
         // console.log(e.responseText);
          this.$message(e.responseText);
        })
      },
      approvalProcessing(type){
        let pointInfo="请确认提交信息";
        switch (type) {
          case "1":
            pointInfo = "请确认是否提交受理，提交后不可更改";
            break;
          case "3":
            pointInfo = "该提交将退回企业，提交后不可更改";
            break;
        }
        this.$confirm(pointInfo, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          if(this.applicationRecordFormData.status=="REJECTED"){
            this.$message.warning({showClose:true,message:"流程已不受理!"});
            return;
          }
          if(this.applicationRecordFormData.status=="RETURN"){
            this.$message.warning({showClose:true,message:"流程已退回，请重新提交!"});
            return;
          }
          let self=this;
          this.flowExecuteRecord.projectId=this.applicationRecordFormData.projectId;
          this.flowExecuteRecord.applicationId=this.applicationRecordFormData.id;
          this.flowExecuteRecord.formJson=this.applicationRecordFormData.formJson;
          this.flowExecuteRecord.comments=document.getElementById("comments").value;
          if(this.projectFlowNode==null||this.projectFlowNode==undefined){
            this.$message.warning({showClose:true,message:"请先提交流程!"});
            return;
          }
          //获取当前节点的下一节点信息
          let nextProjectFlowNode;
          for(let i=0;i<this.projectFlowNodeData.length;i++){
            if(this.projectFlowNodeData[i].flowSeq > this.projectFlowNode.flowSeq){
              nextProjectFlowNode=this.projectFlowNodeData[i];
              break;
            }
          }
          if(document.getElementById("comments").value==null||document.getElementById("comments").value==""){
            this.$message.warning({showClose:true,message:"审批意见不能为空!"});
            return;
          }
          if(nextProjectFlowNode==null||nextProjectFlowNode==undefined){
            this.$message.warning({showClose:true,message:"流程已结束!"});
            return;
          }
          this.flowExecuteRecord.executorId=this.multipleSelectionIds;
          let message="";
          switch (type) {
            case "1":
              if(this.multipleSelectionIds) {
                this.flowExecuteRecord.curNode = this.projectFlowNode.id;
                this.flowExecuteRecord.flowName = this.projectFlowNode.flowName;
                this.flowExecuteRecord.approvalResult="TURN";
                message="转办提交成功！";
              }else{
                this.flowExecuteRecord.curNode = nextProjectFlowNode.id;
                this.flowExecuteRecord.flowName = nextProjectFlowNode.flowName;
                this.flowExecuteRecord.approvalResult="PASS";
                message="受理提交成功！";
              }
              break;
            case "2":
              this.flowExecuteRecord.curNode=nextProjectFlowNode.id;
              this.flowExecuteRecord.flowName=nextProjectFlowNode.flowName;
              this.flowExecuteRecord.approvalResult="CLOSE";
              message="不受理提交成功！";
              break;
            case "3":
              this.flowExecuteRecord.curNode=nextProjectFlowNode.id;
              this.flowExecuteRecord.flowName=nextProjectFlowNode.flowName;
              this.flowExecuteRecord.approvalResult="BACK";
              message="回退提交成功！";
              break;
          }
          this.$http.post("/flowExecute/commit",this.flowExecuteRecord).then(response=>{
            if (response.data.code == 200) {
              self.applicationRecordFormData=response.data.data;
              self.getFlowExecuteRecordData(self);
              self.$message.success({showClose:true,message:message});
              this.$router.push({path: '/applicationRecordList',query:self.searchForm})
            }else{
              self.$message.warning({showClose:true,message:response.data.message});
            }
          }).catch(e=>{
           // console.log(e.responseText);
            this.$message(e.responseText);
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      },
      onSearch(){
        this.$refs.departUserList.add(this.departUserList);
      },
      loadCheckedDeparts(){
        let that = this;
        let param = {dptIds:that.model.enforceDepart}
        this.$http.get("/epspBaseData/getDepartments",{params:param}).then((res)=>{
          that.checkedDepartNames = [];
          if(res.data.success){
            for (let i = 0; i < res.data.data.length; i++) {
              that.checkedDepartNames.push(res.data.data[i].dptname);
              that.checkedDepartNameString = that.checkedDepartNames.join(",");
              that.checkedDepartKeys.push(res.data.data[i].dptid);
            }
          }else{
           // console.log(res.message);
          }
        })
      },
      modalFormOk (formData) {
        this.multipleSelectionIds = formData.ids.join(",");
        this.multipleSelectionNames = formData.names.join(",");
      },
      downloadFile(name, filePath){
        let elink = document.createElement('a');
        elink.download =name;
        elink.style.display = 'none';
        elink.href = this.$api_host+"/commAttachment/ftpDownload/"+filePath;
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href);// 释放URL 对象
        document.body.removeChild(elink);
      },
      uploadSuccess(response, file, fileList) {
        if (response.code == 200) {
          this.$message.success({
            showClose: true,
            message: "上传成功！"
          });
          if(this.flowExecuteRecord.commAttachmentList==null||this.flowExecuteRecord.commAttachmentList==undefined){
            this.flowExecuteRecord.commAttachmentList=[];
          }
          this.flowExecuteRecord.commAttachmentList.push(response.data);
        }
        this.uploadDisabled=false;
      },
      onRemove(file, fileList){
        for(let i=0;i<this.flowExecuteRecord.commAttachmentList.length;i++){
          if(file.name==this.flowExecuteRecord.commAttachmentList[i].fileName){
            this.flowExecuteRecord.commAttachmentList.splice(i);
            break;
          }
        }
      },
      flowExecuteRecordData1 () {
        let elsteps=document.getElementsByClassName("el-step__icon-inner");
        let count=1;
        for(let i=elsteps.length-1;i>elsteps.length-this.flowExecuteRecordData.length-1;i--){
          elsteps[i].innerText=count;
          count++;
        }
      }
    },
    mounted(){
      // 在外部vue的window上添加postMessage的监听，并且绑定处理函数handleMessage
      window.addEventListener('message', event => {
        // 根据上面制定的结构来解析iframe内部发回来的数据
        const data = event.data
        switch (data.cmd) {
          case 1:
            // 业务逻辑
            this.getApplicationRecordFormData()
            break
        }
      });
    }
  }
</script>


<style lang="scss">

  .text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }
  .examineInfo{
    .el-card__body {
      padding: 10px;
    }

    .el-card.is-always-shadow,
    .el-card.is-hover-shadow:focus,
    .el-card.is-hover-shadow:hover {
      box-shadow: 0px 0px 4px 0px rgba(0,0,0,0.1);
    }
    .el-card {
      background-color: #FFF;
      color: #303133;
      -webkit-transition: .3s;
      transition: .3s;
    }


  }

</style>
