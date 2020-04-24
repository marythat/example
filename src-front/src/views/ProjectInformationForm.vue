<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    @close="close"
    :close-on-click-modal="false"
    width="1000px">
    <el-form ref="form" :model="model" :rules="validateRules" label-width="100px">
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="model.projectName"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编码" prop="projectCode">
            <el-input v-model="model.projectCode"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="项目所属科室" prop="belongDept">
            <el-input v-model="model.belongDept"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-input placeholder="点击右侧按钮选择部门" v-model="checkedDepartNameString" :disabled="true" style="float: left">
            <el-button slot="append" icon="el-icon-search" @click="onSearch" style="height:20px">选择</el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="科室咨询电话" prop="askPhone" >
            <el-input v-model="model.askPhone" placeholder="请输入手机号"  style="width:220px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目状态" prop="projectStatus" >
            <el-select v-model="model.projectStatus"  placeholder="请选择"  v-bind:disabled="statusDisabled" style="width: 220px">
              <el-option
                v-for="item in projectStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="申报开始日期" prop="applyStartTime">
            <el-date-picker
              v-model="model.applyStartTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申报结束日期" prop="applyEndTime">
            <el-date-picker
              v-model="model.applyEndTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="23:59:59"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="项目分类" prop="projectType">
            <el-select v-model="model.projectType" placeholder="请选择" style="width: 220px">
              <el-option
                v-for="item in projectTypeOptions"
                :key="item.dictVal"
                :label="item.dictName"
                :value="item.dictVal">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">

          <el-form-item label="关联标签" prop="companyTag">
            <el-select
              v-model="model.companyTag"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择">
              <el-option
                v-for="item in companyTagList"
                :key="item.businesstypeid"
                :label="item.businesstype"
                :value="item.businesstypeid">
              </el-option>
            </el-select>
          </el-form-item>

        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="业务名称" prop="businessName" >
            <el-input v-model="model.businessName" placeholder="业务名称"  style="width:220px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="必须上传文件" prop="necessaryUpload" >
            <el-input v-model="model.necessaryUpload" placeholder="必须上传文件"  style="width:220px"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="可申报次数" prop="declareCount" >
            <el-input v-model="model.declareCount" placeholder="可申报次数"  style="width:220px"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="24">
          <el-form-item label="申报条件" prop="applyConditions">
            <el-input  type="textarea"  :rows="4"
                       placeholder="请输入申报条件"  v-model="model.applyConditions">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :span="24">
          <el-form-item label="额外保存字段" prop="autoSaveProperties" >
            <el-input v-model="model.autoSaveProperties" placeholder="额外保存字段以逗号分割" />
          </el-form-item>
        </el-col>
      </el-row>
<!--      <el-row :gutter="24">-->
        <el-col :span="24" v-show="false">
          <el-form-item label="申报指南" prop="applyFormUrl">
            <el-input v-model="model.applyFormUrl"/>
          </el-form-item>
        </el-col>
<!--      </el-row>-->

      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="申报指南">
            <el-upload
              ref="declareUpload"
              class="upload-demo"
              :action=ftpHost
              :headers="headers"
              :on-remove="declareHandleRemove"
              :file-list="declareFileList"
              :show-file-list=true
              :auto-upload=true
              :before-upload="declareBeforeUpload"
              :disabled="declareDisabled"
              :on-success="declareUploadSuccess">
              <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目申报表单">
            <el-upload
              ref="upload"
              class="upload-demo"
              :action=host
              :headers="headers"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :limit="1"
              :file-list="fileList"
              :show-file-list=true
              :auto-upload=true
              :disabled="declareDisabled"
              :on-success="uploadSuccess"
              :on-exceed="handleExceed"
              :before-upload="beforeUpload">
              <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传html文件</div>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-form-item align="center">
          <el-button type="primary" @click="onSubmit" :disabled="disableSubmit">保 存</el-button>
          <el-button @click="visible = false">取 消</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <depart-window ref="departWindow" @ok="modalFormOk"></depart-window>
  </el-dialog>
</template>

<script>
  import departWindow from '../components/DepartWindow'

  export default {
    name: 'ProjectInformationForm',
    props: ['refreshFunction'],
    components: {
      departWindow
    },
    data() {
      let checkAskPhone = (rule, value, callback) => {
        if (value == '') {
          callback(new Error('电话不可为空'));
        } else {
          if (value != '') {
            let reg =/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
            if(!reg.test(value)){
              callback(new Error('请输入正确的手机号或者座机号格式为：0000-0000000'));
            }
          }
          callback();
        }
      };
       //校验可申报次数
      const isNum = (rule, value, callback) => {
        if(value!=undefined){
          const age= /^[0-9]*$/
          if (!age.test(value)) {
            callback(new Error('可申报次数只能为数字'))
          }else{
            callback();
          }
        }else{
          callback();
        }

      };



      return {
        searchForm:{},
        checkedDepartNameString:"", // 保存部门的名称 =>title
        selectedDepartKeys:[], //保存用户选择部门id
        checkedDepartKeys:[],
        checkedDepartNames:[], // 保存部门的名称 =>title
        headers:{token:window.localStorage.token,iv:window.localStorage.iv},
        dialogTableVisible: false,
        tableIsShow: true,
        addIsShow: false,
        statusDisabled:false,//新增时项目状态初始化置灰
        visible: false,//弹出框是否显示
        disableSubmit: false,//提交按钮
        projectStatusOptions: [
          {value: 'ROUGHDRAFT', label: '草稿'},
          {value: 'AVAILABLE', label: '上线'},
          {value: 'CLOSED', label: '下线'}
        ],
        projectTypeOptions:[],
        title: '',
        commAttachment:{},
        host:this.$api_host+'/commAttachment/store',
        ftpHost:this.$api_host+'/commAttachment/ftpStore',
        fileList:[],
        declareFileList:[],
        companyTagList: [], //分公司列表
        model:{
          companyTag:[],
        },
        validateRules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
          ],
          projectCode: [
            { required: true, message: '请输入项目编码', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
          ],
          askPhone: [
            { validator: checkAskPhone, trigger: 'blur' }
          ],
          declareCount: [
            { validator: isNum, trigger: 'blur' }
          ]
        },
        declareDisabled:false,

      };
    },
    created() {
      this.loadData();
      this.getDistType();//获取项目分类字典
    },
    methods: {
      close () {
        this.visible = false;
        this.statusDisabled=false;
        this.checkedDepartNames = [];
        this.checkedDepartNameString = "";
        this.checkedDepartKeys = [];
      },
      add () {
        this.model.projectType=[];
        this.model.companyTag=[];
        this.statusDisabled=true;
        this.addIsShow=true;
        this.edit({projectCode:this.model.projectCode,projectStatus:"ROUGHDRAFT"});
      },
      edit (record) {
        this.resetForm('form')
        this.model = Object.assign({}, record);
        this.checkedDepartKeys = [];
        this.loadCheckedDeparts();
        this.visible = true;
        this.getCommAttachment();
        this.handleTagEdit(record);//回显下来框选中事件
      },
      loadData() {
        this.$http.get("epspBaseData/companyBusinessList").then(response => {
          this.companyTagList = response.data.data;
        })
      },
      getDistType() {
        let param = {"dict_type":"sys_project"}
        this.$http.get("sysDistType/list",{params:param}).then(response => {
          this.projectTypeOptions = response.data.data.records;
        })
      },
      //多选框显示编辑界面
      handleTagEdit: function (row) {
        let companyTagIds=row.companyTag;
        if(companyTagIds!=null&&companyTagIds!=undefined&&companyTagIds!=""){
          let peoData=companyTagIds.split(',');
          for(var i=0;i<peoData.length;i++)
          {
            peoData[i]=parseInt(peoData[i]);
          }
          this.model.companyTag=peoData
        }
      },
      onSubmit() {
        let self=this;
        //上线的时候判断是否有审批流程节点
        if(self.model.projectStatus=="AVAILABLE"){
          let param = {"project_id":self.model.id}
          this.$http.get("projectFlowNode/list",{params:param}).then(response => {
            if(response.data.data.records<=0){
              self.$message.warning({showClose:true,message:"请先添加流程节点!"});
              return false;
            }else{
              self.save();
            }
          })
        }else{
          self.save();
        }

      },
       save(){
          let self=this;
          //self.disableSubmit = true;
          self.statusDisabled=false;
          self.$refs.form.validate((valid) => {
            //多选框数据处理begin
            if(self.model.companyTag&&self.model.companyTag.length>0){
              let s = []
              for(var i = 0; i < self.model.companyTag.length; i++) {
                s.push(this.model.companyTag[i])
              }
              self.model.companyTag = s.join();
            }else{
              self.model.companyTag="";
            }
            //end
            let ProjectStatusStr =self.getType(self.model.projectStatus);
            self.model.projectStatusStr=ProjectStatusStr;
            let projectInformationData=self.model;
            let param={
              projectInformation:projectInformationData,//基础信息
              commAttachment:self.commAttachment//上传附件
            };
            if (valid) {
              self.$http.post("projectInformation/save",param).then(response => {
                self.$message.success(response.data.message);
                if(self.refreshFunction){
                  self.refreshFunction();
                }
              })
            } else {
              self.$message.error('请先检查表单数据的合法性');
              return false;
            }
          });
          self.visible=false;
      },
      resetForm(formName) {
        if(this.$refs[formName]) {
          this.$refs[formName].resetFields();
        }
      },
      handleRemove(file, fileList) {
        let self=this;
        this.$http.delete("commAttachment/remove/"+file.id).then(response=>{
          if(response.data.code==200){
            self.commAttachment={};
          }
        }).catch(e=>{
        });
      },
      declareHandleRemove(file, fileList){
        let self=this;
        this.$http.delete("commAttachment/remove/"+file.id).then(response=>{
          if(response.data.code==200){
            self.model.applyFormUrl=self.model.applyFormUrl.replace(file.id+",","");
          }
        }).catch(e=>{
        });
      },
      handlePreview(file) {
       // console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning({
          showClose: true,
          message: '当前限制选择 1 个文件'
        });
      },
      beforeUpload(file){
        var testmsg=file.name.substring(file.name.lastIndexOf('.')+1)
        const extension = testmsg === 'HTML'
        const extension2 = testmsg === 'html'
        const isLt2M = file.size / 1024 / 1024 < 30
        if(!extension&&!extension2) {
          this.$message({
            message: '上传文件只能是HTML格式!',
            type: 'warning'
          });
        }
        if(!isLt2M) {
          this.$message({
            message: '上传文件大小不能超过 30MB!',
            type: 'warning'
          });
        }
        return extension||extension2&& isLt2M
      },
      declareBeforeUpload(file){
        const isLt2M = file.size / 1024 / 1024 < 30;
        if(!isLt2M) {
          this.$message({
            message: '上传文件大小不能超过 30MB!',
            type: 'warning'
          });
          return isLt2M;
        }
        this.declareDisabled=true;
        this.disableSubmit=true;
      },
      uploadSuccess(response, file, fileList) {
        if (response.code == 200) {
          this.$message.success({
            showClose: true,
            message: "上传成功！"
          });
          this.commAttachment = response.data;
        }
      },
      declareUploadSuccess(response, file, fileList){
        if (response.code == 200) {
          this.$message.success({
            showClose: true,
            message: "上传成功！"
          });
          file.id=response.data.id;
          if(this.model.applyFormUrl==null||this.model.applyFormUrl==undefined){
            this.model.applyFormUrl = response.data.id+",";
          }else{
            this.model.applyFormUrl += response.data.id+",";
          }
        }
        this.declareDisabled=false;
        this.disableSubmit=false;
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
      getCommAttachment() {
        this.commAttachment={};
        this.fileList=[];
        this.declareFileList=[];
        if(this.model.id==null||this.model.id==undefined||this.model.id=="")return;
        let queryData = {relatedId: this.model.id,related_table:"nst_project_information"};
        let self = this;
        this.$http.get("commAttachment/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            if(response.data.data.records.length>0){
              for(let i=0;i<response.data.data.records.length;i++){
                if(response.data.data.records[i].type=="1"){
                  self.commAttachment=response.data.data.records[i];
                  self.fileList=[{name: response.data.data.records[i].fileName, url: "/commAttachment/download/"+response.data.data.records[i].filePath,id:response.data.data.records[i].id}];
                }else{
                  self.declareFileList.push({name: response.data.data.records[i].fileName, url: "/commAttachment/download/"+response.data.data.records[i].filePath,id:response.data.data.records[i].id});
                }
              }
            }
          }
        }).catch(e=>{
          //console.log(e.status);
          //console.log(e.responseText);
        });
      },
      onSearch(){
        this.$refs.departWindow.add(this.checkedDepartKeys);
      },
      loadCheckedDeparts(){
        let that = this;
        if(that.model.belongDeptId) {
          let param = {dptIds: that.model.belongDeptId}
          this.$http.get("epspBaseData/getDepartments", {params: param}).then((res) => {
            that.checkedDepartNames = [];
            if (res.data.success) {
              for (let i = 0; i < res.data.data.length; i++) {
                that.checkedDepartNames.push(res.data.data[i].dptname);
                that.checkedDepartNameString = that.checkedDepartNames.join(",");
                that.checkedDepartKeys.push(res.data.data[i].dptid);
              }
            } else {
             // console.log(res.message);
            }
          })
        }
      },
      modalFormOk (formData) {
        this.checkedDepartNames = [];
        this.selectedDepartKeys = [];
        this.checkedDepartNameString = '';
        for (let i = 0; i < formData.departList.length; i++) {
          this.selectedDepartKeys.push(formData.departList[i].key);
          this.checkedDepartNames.push(formData.departList[i].title);
          this.checkedDepartNameString = this.checkedDepartNames.join(",");
        }
        this.$set(this.model,'belongDept',this.checkedDepartNameString)
        this.checkedDepartKeys = this.selectedDepartKeys  //更新当前的选择keys
        this.model.belongDeptId = this.checkedDepartKeys.join(",");
      },
      getType(val){
        if(val=="AVAILABLE"){
          return "上线";
        }else if(val=="CLOSED"){
          return "下线";
        }else{
          return "草稿";
        }
      }
    },
  }
</script>
