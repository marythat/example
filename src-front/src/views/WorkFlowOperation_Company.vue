<template>
  <el-container style="background-color: #F0F2F5">
    <!--顶部流程信息-->
    <div style="width:100%;background: #F0F2F5;">
      <div style="background: #FFFFFF;">
        <div style="width: 80%;margin-left:10%;padding-top:20px;padding-bottom:10px">
          <el-steps :active="arthurSteps">
            <el-step v-for="(item,index) in projectFlowNodeData" :title="item.flowName" :key="index" :status="index==arthurSteps-1?processStatus:index<arthurSteps-1?'success':'process '"></el-step>
          </el-steps>
        </div>
      </div>
      <div>
        <el-row>
          <div v-if="show">
            <el-col style="width:33%;background: #FFFFFF;margin-top:1%;height: 740px;float: right" v-if="show">
              <template>
                <div class="stepsTitle" style="margin: 20px" id="arthurClass2">
                  <div style="float:left;width:2px;height:20px; background:#219AFF;"></div>
                  <span >审批流程</span>
                </div>
                <div class="stepComponent" style="overflow-y:scroll;height:650px">
                  <!--填报审批信息 begin-->
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
                                      <span>{{tag.curNode==flowExecuteRecordData[flowExecuteRecordData.length-1].curNode?"提交人：":"审批人："}}{{tag.createPhone==null?tag.createName:tag.createName+" "+tag.createPhone}}</span><br>
                                      <span class="textType">操作：{{tag.approvalResultStr}}</span><br>
                                      <span class="textType">{{tag.curNode==flowExecuteRecordData[flowExecuteRecordData.length-1].curNode?"企业名称："+tag.createBelongDept:"意见："+tag.comments}}</span><br>
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
          </div>

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
    <MustUploadForm ref="mustUploadForm"/>
  </el-container>
</template>

<script>
  import uploadFile from './uploadFile';
  import MustUploadForm from './MustUploadForm';
  export default {
    components: {
      uploadFile,
      MustUploadForm,
    },
    data() {
      return {
        table: true,
        dialog: false,
        loading: false,
        show:false,//是否显示审批信息
        viewInfo1:true,
        viewInfo2:false,
        detailed:false,//审批详细内容
        tableSize:80,//表单起始百分比宽度
        flowPathSize:20,//审批流程百分比宽度
        activeNames:[''],
        scrollHeight:'0px',
        applicationCss:"width:90%;background: #FFFFFF;margin: 1%;height: 740px;margin-left:5%",
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
        processStatus:'process',
      };
    },
    created () {
      this.getProjectFlowNodeData();
      if(this.$route.query.id){
        this.getApplicationRecordFormData();
      }
      if(this.$route.query.projectId){
        this.getProjectInformationData();
      }
      this.getHtml();
    },
    watch: {
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
            let iframe = document.getElementById("show-iframe");
            var idoc = iframe.contentDocument || iframe.contentWindow.document
            idoc.open()
            idoc.writeln(response.data)
            idoc.close()
          })
        }
      }
    },
    methods: {
      validate(cb) {
        return this.$refs.fm.validate(cb);
      },
      clearValidate() {
        this.$refs.fm.resetFields();
        this.$refs.fm.clearValidate();
      },
      headClass () {
        return 'text-align: center;background:#eef1f6;'
      },
      handleDelete(){
        this.$ele.Message("delete")
      },
      addUpLoading(){
        this.dialogUpLoading=true;//默认页面不显示为false,点击按钮将这个属性变成true
      },
      viewShow(){    //详情
        this.arthurCss="width:33%;background: #FFFFFF;margin-top: 20px;height: 740px;float: right";
        this.show=true;//审批意见填报
        this.viewInfo1=false;//文字显示
        this.viewInfo2=true;//文字显示
        this.detailed=true;//审批详情内容
        this.tableSize=60;//表单宽度百分比
        this.flowPathSize=40;//流程框百分比
      },
      viewShow2(){   //收起
        this.arthurCss="width:10%;background: #FFFFFF;margin-top: 20px;margin-left:40px;height: 740px;float: right";
        this.show=false;
        this.viewInfo1=true;
        this.viewInfo2=false;
        this.detailed=false;//审批详情内容
        this.tableSize=80;
        this.flowPathSize=20;
      },
      handleChange(val) {
      },
      getHtml(){
        let self=this;
        let id =this.$route.query.id;
        let queryData = {relatedId: this.$route.query.projectId,relatedTable:'nst_project_information',type:"1"};
        this.$http.get("/commAttachment/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            if(response.data.data.records.length>0){
              self.commAttachment=response.data.data.records[0];
              if(self.commAttachment!=null){
                self.url= "/commAttachment/download/"+self.commAttachment.filePath+"?id="+id;
              }
            }
          }
        })
      },
      getProjectFlowNodeData(){
        let self=this;
        let projectId =this.$route.query.projectId;
        let queryData = {projectId: projectId,"column":"flowSeq"};
        this.$http.get("/projectFlowNode/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            self.projectFlowNodeData=response.data.data.records;
          }
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
              self.getFlowExecuteRecordData(self);
              for(let i=0;i<this.projectFlowNodeData.length;i++){
                if(self.projectFlowNodeData[i].id==self.applicationRecordFormData.curNode){
                  self.projectFlowNode=self.projectFlowNodeData[i];
                  self.arthurSteps=i+1;
                }
              }
            }
          }
        })
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
              self.applicationCss="width:90%;background: #FFFFFF;margin: 1%;height: 740px;margin-left:5%";
              self.show=false;
            }
          }
        })
      },
      approvalProcessing(type){
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
          if(this.projectFlowNodeData[i].id==this.projectFlowNode.id){
            if(i+1<this.projectFlowNodeData.length){
              nextProjectFlowNode=this.projectFlowNodeData[i+1];
            }
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
        this.flowExecuteRecord.curNode=nextProjectFlowNode.id;
        this.flowExecuteRecord.flowName=nextProjectFlowNode.flowName;
        let message="";
        switch (type) {
          case "1":
            this.flowExecuteRecord.approvalResult="PASS";
            message="受理提交成功！";
            break;
          case "2":
            this.flowExecuteRecord.approvalResult="CLOSE";
            message="不受理提交成功！";
            break;
          case "3":
            this.flowExecuteRecord.approvalResult="BACK";
            message="回滚提交成功！";
            break;
        }
        this.$http.post("/flowExecute/commit",this.flowExecuteRecord).then(response=>{
          if (response.data.code == 200) {
            self.applicationRecordFormData=response.data.data;
            self.getFlowExecuteRecordData(self);
            self.projectFlowNode=nextProjectFlowNode;
            self.arthurSteps=nextProjectFlowNode.flowSeq;
            self.$message.success({showClose:true,message:message});
          }else{
            self.$message.warning({showClose:true,message:response.data.message});
          }
        })
      },
      flowExecuteRecordData1 () {
        let elsteps=document.getElementsByClassName("el-step__icon-inner");
        let count=1;
        for(let i=elsteps.length-1;i>elsteps.length-this.flowExecuteRecordData.length-1;i--){
          elsteps[i].innerText=count;
          count++;
        }
      },
      getProjectInformationData(){
        let projectId=this.$route.query.projectId;
        this.$http.get("/projectInformation/queryOne",{params:{id:projectId}}).then(response=>{
          this.projectInformation=response.data.data;
        })
      },
      messageListener(event){
        const data = event.data;
        switch (data.cmd) {
          case 1:
            // 业务逻辑
            this.$router.push({path: '/applicationRecordListCompany'});
            break;
          case 2:
            this.$refs.mustUploadForm.visible = true;
            this.$refs.mustUploadForm.loadData(this.projectInformation.necessaryUpload);
            break;
          case 3:
            this.$confirm(data.params.data.tips, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              let tempForm = document.createElement("form");
              tempForm.id = "tempForm1";
              tempForm.method = "post";
              tempForm.action = data.params.data.url;
              tempForm.target = name;    // _blank - URL加载到一个新的窗口

              var hideInput = document.createElement("input");
              hideInput.type = "hidden";
              hideInput.name = "content";
              hideInput.value = data.params.url;
              tempForm.appendChild(hideInput);
              if(document.all){    // 兼容不同浏览器
                tempForm.attachEvent("onsubmit",function(){});        //IE
              }else{
                tempForm.addEventListener("submit",function(){},false);    //firefox
              }
              document.body.appendChild(tempForm);
              if(document.all){    // 兼容不同浏览器
                tempForm.fireEvent("onsubmit");
              }else{
                tempForm.dispatchEvent(new Event("submit"));
              }
              tempForm.submit();
              document.body.removeChild(tempForm);
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消'
              });
            });
            break;
        }
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
    },
    mounted() {
      window.addEventListener('message',this.messageListener);
    },
    destroyed () {
      window.removeEventListener('message', this.messageListener);
    }
  }


</script>


<style>
  .text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }
</style>
