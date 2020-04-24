<template>
  <el-dialog
    :append-to-body="appendToBody"
    :title="title"
    @close="close"
    :visible.sync="visible"
    :close-on-click-modal="false"
    width="600px">
    <div class="el-input__inner1">
      <el-form ref="form" :model="model" :rules="validateRules" label-width="150px">
        <el-form-item label="节点名称" prop="flowName">
          <el-input class="el-input__inner2" v-model="model.flowName"/>
        </el-form-item>
        <el-form-item label="序号" prop="flowSeq">
          <el-input class="el-input__inner2" v-model="model.flowSeq"/>
        </el-form-item>
        <el-form-item label="节点类型" prop="type">
          <el-select class="el-input__inner2" v-model="model.type" filterable placeholder="请选择节点类型">
            <el-option
              v-for="item in options1"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审批类型" prop="applyType">
          <el-select class="el-input__inner2" v-model="model.applyType" filterable placeholder="审批类型" @change="applyTypeChange">
            <el-option
              v-for="item in options2"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户" v-show="model.applyType=='USER'" class="selectInput">
          <el-input placeholder="点击右侧按钮选择用户" v-model="multipleSelectionNames" :disabled="true"
                    style="float: left;width: 220px;">
            <el-button slot="append" icon="el-icon-search" @click="onSearch1" style="height:20px">选择</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="执行部门" v-show="model.applyType=='DEPART'" class="selectInput">
          <el-checkbox v-model="checked">默认镇街</el-checkbox>
          <el-input placeholder="点击右侧按钮选择执行部门" v-model="checkedDepartNameString" :disabled="true"
                    style="float: left;width: 220px;margin-right: 5px" v-if="!checked">
            <el-button slot="append" icon="el-icon-search" @click="onSearch" style="height:20px">选择</el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="短信通知" v-if="!checked" class="selectInput">
          <el-input placeholder="点击右侧按钮选择用户" v-model="smsNotificationNames" :disabled="true"
                    style="float: left;width: 220px;">
            <el-button slot="append" icon="el-icon-search" @click="onSearch2" style="height:20px">选择</el-button>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :disabled="disableSubmit">保 存</el-button>
          <el-button @click="visible = false">取 消</el-button>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer"></span>
    <depart-window ref="departWindow" @ok="modalFormOk"></depart-window>
    <departUserList ref="departUserList" @ok="modalFormOk1"></departUserList>
    <sms-notification-form ref="smsNotification" :notification="notification"></sms-notification-form>
  </el-dialog>
</template>

<script>
  import departUserList from '../components/DepartUserList'
  import departWindow from '../components/DepartWindow'
  import smsNotificationForm from './SmsNotificationForm'
  export default {
    props:[
      "refreshFunction"
    ],
    components: {
      departWindow,
      departUserList,
      smsNotificationForm
    },
    data() {
      return {
        dptId:'',
        checked: true,
        smsNotificationNames:'',
        smsNotificationIds:'',
        multipleSelectionNames:'',
        multipleSelectionIds:'',
        checkedDepartNameString:"", // 保存部门的名称 =>title
        selectedDepartKeys:[], //保存用户选择部门id
        checkedDepartKeys:[],
        checkedDepartNames:[], // 保存部门的名称 =>title
        title: "编辑",
        visible: false,
        appendToBody: true,
        disableSubmit: false,
        model:{},
        options1:[
          {value:'START',label:'开始节点'},
          {value:'END',label:'结尾节点'},
          {value:'NORMAL',label:'中间节点'}
        ],
        options2:[
          {value:'USER',label:'用户'},
          {value:'DEPART',label:'部门'}
        ],
        validateRules: {
          flowName: [
            { required: true, message: '请输入公司名称', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
          ],
          flowSeq: [
            { required: true, message: '请输入流程序列号', trigger: 'blur' },
            { validator: isNum, trigger: 'blur' }
          ],
        },
      };
    },

    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.resetForm('form');
        this.model = Object.assign({projectId:this.model.projectId}, record);
        if(this.model.smsNotificationNames){
          this.smsNotificationNames=this.model.smsNotificationNames.split("-")[0];
          this.smsNotificationIds=this.model.smsNotificationNames.split("-")[1];
        }
        if(this.model.enforceDepart){
          this.dptId=this.model.enforceDepart;
        }
        if(this.model.isChecked == 1){
          this.checked = true
        }else {
          this.checked = false
        }
        this.checkedDepartKeys = [];
        this.loadCheckedDeparts();
        this.visible = true;
      },
      onSubmit() {
        this.model.smsNotificationNames="";
        this.disableSubmit = true;
        let l = this;
        this.model.typeStr="";
        this.$refs.form.validate((valid) => {
          if (valid) {
            let itemInfo=this;
            if(itemInfo.multipleSelectionNames==""&&itemInfo.checkedDepartNameString==""&&!this.checked){
              this.$message.error('请选择用户或者执行部门相关信息');
              return false;
            }
            if(!this.checked&&this.smsNotificationIds!=""&&this.smsNotificationNames!=""){
              this.model.smsNotificationNames=this.smsNotificationNames+"-"+this.smsNotificationIds;
            }
            if(this.checked){
              l.model.enforceDepart = null
              l.model.executorId = null
              l.model.isChecked=1
            }else{
              l.model.isChecked=0
            }
            this.$http.post("projectFlowNode/save",l.model).then(response => {
              if(response.data.success){
                this.$message.success(response.data.message);
              }else{
                this.$message.error(response.data.message);
              }

              if(this.refreshFunction){
                this.refreshFunction();
              }
            })
          } else {
            this.$message.error('请先检查表单数据的合法性');
            return false;
          }
        });
        this.visible=false;
      },
      resetForm(formName) {
        if(this.$refs[formName]) {
          this.$refs[formName].resetFields();
        }
      },
      close () {
        this.visible = false;
        this.checked = true;
        this.disableSubmit = false;
        this.checkedDepartNames = [];
        this.checkedDepartNameString='';
        this.checkedDepartKeys = [];
        this.selectedDepartKeys = [];
        this.multipleSelectionNames = ''
        this.multipleSelectionIds = ''
        this.smsNotificationNames='',
        this.smsNotificationIds='',
        this.dptId='';
        this.$refs.departUserList.clearSelect()
      },
      // 搜索部门API
      onSearch(){
        this.$refs.departWindow.add(this.checkedDepartKeys);
      },
      loadCheckedDeparts(){
        let that = this;
        if(this.model.applyType=='USER'&&this.model.executorId) {
          let param = {ids:this.model.executorId}
          that.multipleSelectionIds=this.model.executorId
          this.$http.get("/epspBaseData/searchUser",{params:param}).then((res)=>{
            if(res.data.success){
              for (let i = 0; i < res.data.data.length; i++) {
                if(i!=res.data.data.length-1) {
                  that.multipleSelectionNames += res.data.data[i].username+",";
                }else{
                  that.multipleSelectionNames += res.data.data[i].username
                }
              }
            }
          })
        }else if(that.model.enforceDepart){
          let param = {dptIds: that.model.enforceDepart}
          this.$http.get("/epspBaseData/getDepartments", {params: param}).then((res) => {
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
        this.checkedDepartKeys = this.selectedDepartKeys  //更新当前的选择keys
        this.model.enforceDepart = this.checkedDepartKeys.join(",");
        this.dptId=this.checkedDepartKeys.join(",");
      },
      onSearch1(){
        this.$refs.departUserList.add(this.multipleSelectionIds,this.multipleSelectionNames);
      },
      onSearch2(){
        this.$refs.smsNotification.visible=true;
        if(this.model.applyType=='USER') {
          this.$refs.smsNotification.add(1,this.multipleSelectionIds,this.smsNotificationIds);
        }
        if(this.model.applyType=='DEPART'){
          this.$refs.smsNotification.add(0,this.dptId,this.smsNotificationIds);
        }
      },
      modalFormOk1 (formData) {
        this.multipleSelectionIds = formData.ids.join(",");
        this.model.executorId = this.multipleSelectionIds
        this.multipleSelectionNames = formData.names.join(",");
      },
      notification(rows){
        this.smsNotificationNames="";
        this.smsNotificationIds="";
        if(rows){
          rows.forEach(row=>{
            this.smsNotificationNames+=row.username+","
            this.smsNotificationIds+=row.userid+",";
          })
          this.smsNotificationNames=this.smsNotificationNames.substring(0,this.smsNotificationNames.length-1);
        }
      },
      applyTypeChange(){
        this.smsNotificationNames="";
        this.smsNotificationIds="";
        this.dptId="";
      }
    }
  };

  const isNum = (rule, value, callback) => {
    const age= /^[0-9]*$/
    if (!age.test(value)) {
      callback(new Error('流程序号只能为数字'))
    }else{
      callback()
    }
  }

</script>

<style lang="scss">
  .el-input__inner1 {
    .el-input__inner {
      width: 200px;
    }
  }

  .el-input__inner2{
    .el-input__inner {
      width: 290px;
    }
  }
  .selectInput {

    .el-input__inner {
      float: left;
    }

  }

  .el-dialog__body {
    padding: 30px 20px 0px 20px;
  }

  .el-input-group__append {
    height: 38px;
    width: 50px;
  }

</style>
