<template>
  <el-container>
    <el-main>
      <!-- 查询条件区域 -->
      <el-row style="height: 180px;width: 100%; min-width: 900px;">
        <el-form ref="searchForm" :model="searchForm" label-width="100px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="关联标签" prop="companyTag">
                <el-select v-model="searchForm.companyTag" placeholder="请选择">
                  <el-option
                    v-for="item in tagOptions"
                    :key="item.businesstypeid"
                    :label="item.businesstype"
                    :value="item.businesstypeid">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="提交时间" prop="commitDate">
                <el-date-picker
                  v-model="searchForm.commitDate"
                  type="datetimerange"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  :default-time="['00:00:00', '23:59:59']"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="审批状态" prop="status">
                <el-select v-model="searchForm.status" placeholder="请选择" >
                  <el-option
                    v-for="item in projectStatusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="项目分类" prop="projectType">
                <el-select v-model="searchForm.projectType" placeholder="请选择">
                  <el-option
                    v-for="item in projectTypeOptions"
                    :key="item.dictVal"
                    :label="item.dictName"
                    :value="item.dictVal">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="企业名称" prop="companyName">
                <el-input v-model="searchForm.companyName" placeholder="请输入企业名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="项目名称">
                <el-input v-model="searchForm.projectName" placeholder="请输入项目名称" @focus="details(0)"/>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="项目编码" prop="projectCode">
                <el-input v-model="searchForm.projectCode" placeholder="请输入项目编码" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属科室" prop="belongDept">
                <el-input v-model="searchForm.belongDept" placeholder="请输入项目所属科室" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="待处理申请" prop="checked">
                <el-select v-model="searchForm.checked" placeholder="请选择">
                  <el-option
                    v-for="item in checkBoxData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            <!--              <div style="margin-left: 30px">-->
            <!--                <el-checkbox-->
            <!--                  v-model="searchForm.checked"-->
            <!--                  @change="changeCheck"/>-->
            <!--                  待处理申请-->
            <!--              </div>-->
            </el-col>
            <el-col :span="6">
              <el-form-item label="待审批环节" prop="nextNodeName">
                <el-input v-model="searchForm.nextNodeName" placeholder="待审批环节"/>
              </el-form-item>
            </el-col>
            <el-row style="text-align:right;width:400px;float:right">
              <el-button type="primary" @click="executeQuery(1)" icon="el-icon-search">查询</el-button>
              <el-button type="reset" icon="el-icon-refresh-left" @click="resetForm('searchForm')" >重置</el-button>
              <el-button type="primary" @click="export2Excel" icon="el-icon-download">导出</el-button>
            </el-row>
          </el-row>
        </el-form>
      </el-row>

      <!-- 数据展示区域 -->
      <el-row>
        <el-table :data="tableData">
          <el-table-column type="index" label="#"/>
          <el-table-column prop="companyName" label="企业名称"/>
          <el-table-column prop="projectName" label="项目名称"/>
          <el-table-column prop="projectCode" label="项目编码"/>
          <el-table-column prop="projectType" label="项目分类">
            <template slot-scope="scope">
              {{getprojectType(scope.row.projectType)}}
            </template>
          </el-table-column>
          <el-table-column prop="commitDate" label="提交时间"/>
          <el-table-column prop="status" label="审批状态">
            <template slot-scope="scope">
              {{getStatus(scope.row.status)}}
            </template>
          </el-table-column>
          <el-table-column prop="nextNodeName" label="待审批环节"/>
          <el-table-column prop="flowName" label="完成环节"/>
          <el-table-column prop="belongDept" label="项目所属科室"/>

          <el-table-column label="操作" width="140px">
            <template slot-scope="scope">
              <router-link :to="{path:'/workFlowOperation?id='+scope.row.id+'&acceptCode='+scope.row.acceptCode+'&projectId='+scope.row.projectId+'&dptUser=true&show='
                           +(scope.row.projectStatus != 'CLOSED'&& (curUserId!=null&&isContain(scope.row.executorId,scope.row))),query:searchForm}">
                <el-button size="small" type="primary"
                               :type="scope.row.projectStatus === 'CLOSED' ? 'info':'primary'">审批</el-button>
              </router-link>
              <el-popconfirm title="即将删除记录, 是否继续?" @onConfirm="handleDelete(scope.row)" v-show="delPer">
                <row-operation slot="reference" icon="el-icon-delete" type="danger" tips="删除"/>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-row>

      <!--分页-->
      <table-pagination :pagination="pagination" @current-change="pageChange"/>
    </el-main>

    <!-- 其他模态组件引入区域 -->
    <application-record-form ref="modalForm" :refreshFunction="loadData"/>

    <!-- 其他模态组件引入区域 -->
    <application-record-details-list ref="detailsList" :refreshFunction="loadData"/>

  </el-container>
</template>

<style>
</style>

<script>
  import ApplicationRecordForm from "./ApplicationRecordForm";
  import store from '@/store'
  import ApplicationRecordDetailsList from "./ApplicationRecordDetailsList";

  export default {
    components: {ApplicationRecordDetailsList, ApplicationRecordForm},
    data() {
      return {
        tableData: [],
        tableData1: [],
        curDepart: store.getters.userInfo.department.dptid,
        curUserId: store.getters.userInfo.epspUser.userid,
        delPer: store.getters.userInfo.epspUser.per.indexOf('managerApplyAdmin')!=-1,
        searchForm: {
          column: 'updateTime',
          order: 'desc',
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
        projectSearchForm:{
          projectName:''
        },
        pagination: {currentPage: 1, currentSize: 10, total: 0},
        pagination1: {currentPage: 1, currentSize: 10, total: 0},
        projectStatusOptions: [
          {value: 'DRAFT', label: '草稿'},
          {value: 'APPROVAL', label: '审批中'},
          {value: 'PASSED', label: '通过'},
          {value: 'RETURN', label: '退回修改'}
        ],
        projectTypeOptions:[],
        projectTypeData:'',
        tagOptions:[],
        checkBoxData:[
          {label:'是',value:"true"},
          {label:'否',value:"false"}
        ]
      }
    },
    created() {
      this.getDistType();//获取项目分类字典
    },
    methods: {
      loadData(row,type,next,nextName) {
        this.searchForm.pageNo = this.pagination.currentPage;
        this.searchForm.pageSize = this.pagination.currentSize;
        let searchParams =  Object.assign({}, this.searchForm);
        if(searchParams.projectName!=""&&searchParams.projectName!=undefined){
           searchParams.projectName = "*"+searchParams.projectName+"*";
        }
        if(searchParams.projectCode!=""&&searchParams.projectCode!=undefined){
          searchParams.projectCode = "*"+searchParams.projectCode+"*";
        }
        if(searchParams.belongDept!=""&&searchParams.belongDept!=undefined){
          searchParams.belongDept = "*"+searchParams.belongDept+"*";
        }
        if(searchParams.companyName!=""&&searchParams.companyName!=undefined){
          searchParams.companyName = "*"+searchParams.companyName+"*";
        }
        if(searchParams.companyTag!=""&&searchParams.companyTag!=undefined){
          searchParams.companyTag = "*"+searchParams.companyTag+"*";
        }

        if(searchParams.commitDate!=""&&searchParams.commitDate!=undefined){
          searchParams.commitDate_start=searchParams.commitDate[0];
          searchParams.commitDate_end= searchParams.commitDate[1];
          searchParams.commitDate=[];
        }

        if(type!=null&&type!=undefined&&row!=null&&row!=""&&row!=undefined){
          this.searchForm.projectName=row.projectName;
          this.searchForm.projectId=row.projectId;
          searchParams.projectName="";
          searchParams.projectId=row.projectId;
          searchParams.pageNo=1;
          this.searchForm.status="";
          searchParams.status="";
          this.searchForm.nextNodeId="";
          searchParams.nextNodeId="";
          this.searchForm.nextNodeName="";
          searchParams.nextNodeName="";
          switch (type) {
            case 0:
              break;
            case 1:
              this.searchForm.status="DRAFT";
              searchParams.status="DRAFT";
              break;
            case 2:
              this.searchForm.status="APPROVAL";
              searchParams.status="APPROVAL";
              break;
            case 3:
              this.searchForm.status="PASSED";
              searchParams.status="PASSED";
              break;
            case 4:
              this.searchForm.status="REJECTED";
              searchParams.status="REJECTED";
              break;
            case 5:
              this.searchForm.status="RETURN";
              searchParams.status="RETURN";
              break;
            case 99:
              this.searchForm.nextNodeName=nextName;
              searchParams.nextNodeId=next;
          }
        }

        this.$http.get("applicationRecord/list", {params: searchParams}).then(response => {
          if (response.data.success) {
            this.tableData = response.data.data.records;
            this.pagination = {
              total: response.data.data.total,
              currentPage: response.data.data.current,
              currentSize: response.data.data.size
            };
            this.loadDataTag();
          } else {
            this.$message(response.data.message);
          }
        })
      },
      getDistType() {
        let param = {"dict_type":"sys_project"}
        this.$http.get("sysDistType/list",{params:param}).then(response => {
          this.projectTypeOptions = response.data.data.records;
          this.projectTypeData= response.data.data.records;
        })
      },
      isContain(value,e){
        let ll = (this.curDepart!=null&&e.enforceDepart == this.curDepart)
        return ll||(value!=null&&value.split(',').indexOf(this.curUserId.toString())!=-1)||(this.curDepart!=null&&e.belongDeptId==this.curDepart)
      },
      loadDataTag() {
        let searchParams =  Object.assign({}, this.searchForm);
        searchParams.commitDate=[];
        this.$http.get("/epspBaseData/companyBusinessList", {params: searchParams}).then(response => {
          this.tagOptions = response.data.data;
        })
      },
      handleDelete(row) {
        this.$http.delete("applicationRecord/remove/" + row.id).then(response => {
          this.loadData();
          this.$message.success(response.data.message);
        })
      },
      executeQuery(type) {
        switch (type) {
          case 0:
            this.getDetailsList();
            break;
          case 1:
            this.loadData();
            break;
        }
      },
      pageChange(pageNo) {
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
      pageChange1(pageNo) {
        this.pagination1.currentPage = pageNo;
        this.getDetailsList();
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        // this.searchForm={};
      },
      getprojectType(type){
        for (let i = 0; i < this.projectTypeData.length; i++) {
          let hh=this.projectTypeData[i].dictVal;
          if(this.projectTypeData[i].dictVal==type){
            return this.projectTypeData[i].dictName;
            break;
          }
        }

      },
      getStatus(status){
        if(status=="DRAFT"){
          return "草稿";
        }else if(status=="APPROVAL"){
          return "审批中";
        }else if(status=="PASSED"){
          return "通过";
        }else if(status=="REJECTED"){
          return "不通过";
        }else if(status=="RETURN"){
          return "退回修改";
        }else{
          return "";
        }
      },
      async details(type){
         switch (type) {
          case 0:
            if(this.searchForm.projectId&&this.searchForm.projectId!=undefined&&this.searchForm.projectId!=""){
              let tableColumn=await this.getProjectFlowNodeData(this.searchForm.projectId);
              this.$refs["detailsList"].visible=true;
              this.$refs["detailsList"].addOne(this.searchForm.projectId,tableColumn);
            }else{
              this.$refs["detailsList"].visible=true;
              this.$refs["detailsList"].add();
            }
            break;
          case 1:
            break;
        }
      },
      async getProjectFlowNodeData(projectId){
        let tableColumn=[];
        let queryData = {projectId: projectId,column:'flow_seq',order:'ASC'};
        await this.$http.get("/projectFlowNode/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            let projectFlowNodeData=response.data.data.records;
            for(let i=0;i<projectFlowNodeData.length;i++){
              tableColumn.push({prop:projectFlowNodeData[i].id+"",label:projectFlowNodeData[i].flowName})
            }
          }
        }).catch(e=>{
          //console.log(e.responseText);
          this.$message(e.responseText);
        })
        return tableColumn;
      },
      getDetailsList(){
        this.projectSearchForm.pageNo = this.pagination1.currentPage;
        this.projectSearchForm.pageSize = this.pagination1.currentSize;
        let projectSearchParams=Object.assign({},this.projectSearchForm);
        if(projectSearchParams.projectName!=""&&projectSearchParams.projectName!=undefined){
          projectSearchParams.projectName = "*"+projectSearchParams.projectName+"*";
        }
        this.$http.get("applicationRecord/details",{params: projectSearchParams}).then(response => {
          if (response.data.success) {
            this.tableData1 = response.data.data.records;
            this.pagination1 = {
              total: response.data.data.total,
              currentPage: response.data.data.current,
              currentSize: response.data.data.size
            };
          } else {
            this.$message(response.data.message);
          }
        })
      },
      export2Excel() {
        require.ensure([], async () => {
          const {export_json_to_excel} = require('../store/Export2Excel');
          const {format_json} = require('../store/Export2Excel');
          const tHeader = ['受理编号', '企业名称', '镇街', '项目分类', '项目编码', '项目名称', '提交时间', '项目所属科室', '完成环节', '审批状态', '操作流水', '操作人所属', '操作人', '操作日期', '操作的环节', '操作意见'];
          const filterVal = ['acceptCode', 'companyName', 'street', 'projectType', 'projectCode', 'projectName', 'commitDate', 'belongDept', 'flowName', 'status', 'flowNumber', 'createBelongDept', 'createName', 'createTime1', 'flowName1', 'comments'];
          let merges = [];
          let excelData = [];
          let rowStart=1;
          let tableDataExcel=[];

          //查询条件
          this.searchForm.pageNo = this.pagination.currentPage;
          this.searchForm.pageSize = 1000;
          let searchParams =  Object.assign({}, this.searchForm);
          if(searchParams.projectName!=""&&searchParams.projectName!=undefined){
            searchParams.projectName = "*"+searchParams.projectName+"*";
          }
          if(searchParams.projectCode!=""&&searchParams.projectCode!=undefined){
            searchParams.projectCode = "*"+searchParams.projectCode+"*";
          }
          if(searchParams.belongDept!=""&&searchParams.belongDept!=undefined){
            searchParams.belongDept = "*"+searchParams.belongDept+"*";
          }
          if(searchParams.companyName!=""&&searchParams.companyName!=undefined){
            searchParams.companyName = "*"+searchParams.companyName+"*";
          }
          if(searchParams.companyTag!=""&&searchParams.companyTag!=undefined){
            searchParams.companyTag = "*"+searchParams.companyTag+"*";
          }

          if(searchParams.commitDate!=""&&searchParams.commitDate!=undefined){
            searchParams.commitDate_start=searchParams.commitDate[0];
            searchParams.commitDate_end= searchParams.commitDate[1];
            searchParams.commitDate=[];
          }


          await this.$http.get("applicationRecord/list",{params: searchParams}).then(response => {
            if (response.data.success) {
              tableDataExcel = response.data.data.records;
            } else {
              this.$message(response.data.message);
            }
          })
          for (let j = 0; j < tableDataExcel.length; j++) {
            let flowExecuteRecordData = await this.getFlowExecuteRecordData(tableDataExcel[j].id);
            if (flowExecuteRecordData.length > 0) {
              for (let i = 0; i < flowExecuteRecordData.length; i++) {
                flowExecuteRecordData[i].flowName1=flowExecuteRecordData[i].flowName;
                flowExecuteRecordData[i].createTime1=flowExecuteRecordData[i].createTime;
                flowExecuteRecordData[i].flowNumber=i+1+"";
                delete flowExecuteRecordData[i].flowName;
                delete flowExecuteRecordData[i].createTime;
                delete flowExecuteRecordData[i].id;
                excelData.push(Object.assign({},tableDataExcel[j],flowExecuteRecordData[i]));
              }
            }else{
              excelData.push(Object.assign({},tableDataExcel[j]));
            }
            if(flowExecuteRecordData.length>1){
              for(let k=0;k<10;k++){
                let merge1= {
                  s: { c: k, r: rowStart },
                  e: { c: k, r: rowStart+flowExecuteRecordData.length-1}
                };
                merges.push(merge1);
              }
              rowStart+=flowExecuteRecordData.length;
            }else{
              rowStart+=1;
            }
          }
          const list = Object.assign(excelData);  //把data里的tableData存到list
          for (let i = 0; i < list.length; i++) {
            list[i].projectType = this.getprojectType(list[i].projectType);
            list[i].status = this.getStatus(list[i].status);
          }
          const data = format_json(filterVal, list);
          export_json_to_excel(tHeader, data, '项目申请记录列表',merges);
        })
      },
      async getFlowExecuteRecordData(applicationId){
        let flowExecuteRecordData=[];
        let queryData = {applicationId: applicationId,column:"createTime",order:"ASC"};
        await this.$http.get("/flowExecuteRecord/list",{params:queryData}).then(response=>{
          if (response.data.code == 200) {
            flowExecuteRecordData=response.data.data.records;
          }
        }).catch(e=>{
          console.log(e.responseText);
          this.$message(e.responseText);
        })
        return flowExecuteRecordData;
      },
    },
    mounted(){
      let flag=true;
      for(let key in this.searchForm){
        if(this.$route.query[key]&&this.$route.query[key]!=undefined&&this.$route.query[key]!=""){
          this.searchForm[key]=this.$route.query[key];
          flag=false;
        }
      }
      if(flag){
        this.details(0);
      }else{
        this.loadData();
      }
    },
    watch: {
      searchForm:{
        handler(newValue, oldValue){
          this.$router.push({
            query: this.searchForm
          });
        },
        deep: true
      }
    }
  };
</script>
