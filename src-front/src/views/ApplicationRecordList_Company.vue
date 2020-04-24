<template>
  <el-container>
    <el-main>
      <!-- 查询条件区域 -->
      <el-row style="height: 89px;width: 100%; min-width: 900px;">
        <el-form ref="searchForm" :model="searchForm" label-width="80px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="searchForm.projectName" placeholder="请输入项目名称"/>
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
            <el-col :span="6" align="right">
              <el-form-item>
                <el-button type="primary" @click="executeQuery" icon="el-icon-search">查询</el-button>
                <el-button type="reset" icon="el-icon-refresh-left" @click="resetForm('searchForm')">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-row>

      <!-- 数据展示区域 -->
      <el-row>
        <el-table :data="tableData">
          <el-table-column type="index" label="#"/>
          <el-table-column prop="projectName" label="项目名称"/>
          <el-table-column prop="acceptCode" label="受理编号"/>
          <el-table-column prop="projectType" label="项目分类">
            <template slot-scope="scope">
              {{ getprojectType(scope.row.projectType)}}
            </template>
          </el-table-column>
          <el-table-column prop="commitDate" label="提交时间"/>
          <el-table-column prop="status" label="审批状态">
            <template slot-scope="scope">
              {{getStatus(scope.row.status)}}
            </template>
          </el-table-column>
          <el-table-column prop="flowName" label="完成环节"/>
          <el-table-column prop="belongDept" label="项目所属科室"/>
          <el-table-column prop="askPhone" label="科室电话"/>
          <el-table-column label="操作" width="250px">
            <template slot-scope="scope">
              <router-link :to="'/workFlowOperationCompany?id='+scope.row.id+'&acceptCode='+scope.row.acceptCode+'&projectId='+scope.row.projectId+'&firstindustypeid='+firstindustypeid">
                <el-button  size="small"  plain :disabled="scope.row.projectStatus === 'CLOSED'"
                            :type="scope.row.projectStatus === 'CLOSED' ? 'info':'primary'">查看</el-button>
              </router-link>
              <el-button size="small" type="primary"   @click="handleSelect(scope.row,scope.$index)" v-show="visible"  plain>权限设置</el-button>
              <el-popconfirm title="即将删除记录, 是否继续?" @onConfirm="handleDelete(scope.row)">
                <el-button  slot="reference" size="small" type="danger" plain>删除</el-button>
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
    <companyPersonSelect ref="companyPersonSelect" @ok="modalFormOk"></companyPersonSelect>
  </el-container>
</template>

<style>
</style>

<script>
  import ApplicationRecordForm from "./ApplicationRecordForm";
  import companyPersonSelect from '../components/CompanyPersonSelect'
  import store from '@/store'
  export default {
    components: {ApplicationRecordForm,companyPersonSelect},
    data() {
      return {
        firstindustypeid: store.getters.userInfo.companyUser.firstindustypeid,
        visible: (localStorage.phone == "undefined" || !localStorage.phone),
        tableData: [],
        searchForm: {column: 'updateTime,companyName', order: 'desc',
          projectName:'',
          projectCode:'',
          belongDept:'',
          commitDate:[],
          status:'',
          projectType:'',
          commitDate_start:'',
          commitDate_end:'',
        },
        pagination: {currentPage: 1, currentSize: 10, total: 0},
        projectStatusOptions: [
          {value: 'DRAFT', label: '草稿'},
          {value: 'APPROVAL', label: '审批中'},
          {value: 'PASSED', label: '通过'},
          {value: 'RETURN', label: '退回修改'}
        ],
        projectTypeOptions:[],
        projectTypeData:'',
        routerParams:[]
      }
    },
    created() {

    },
    methods: {
      loadData() {
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

        if(searchParams.commitDate!=""&&searchParams.commitDate!=undefined){
           searchParams.commitDate_start=searchParams.commitDate[0];
           searchParams.commitDate_end= searchParams.commitDate[1];
          searchParams.commitDate=[];
        }

        this.$http.get("applicationRecord/list",{params: searchParams}).then(response => {
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
      getDistType() {
        let param = {"dict_type":"sys_project"}
        this.$http.get("sysDistType/list",{params:param}).then(response => {
          this.projectTypeOptions = response.data.data.records;
          this.projectTypeData= response.data.data.records;
        })
      },
      handleSelect(row,index){
        this.$refs.companyPersonSelect.title = "选择可视用户";
        this.$refs.companyPersonSelect.loadData(row,index);
      },
      modalFormOk(params){
        let param = Object.assign({}, params)
        delete param["index"]
        this.$http.post("applicationRecord/save",param).then(response => {
          if (response.data.code == 200) {
            this.tableData[params.index].permissionKeys = params.permissionKeys
            this.$message.success('提交成功');
          } else {
            this.$message.error('提交失败');
          }
        })
      },
      handleDelete(row) {
        this.$http.delete("applicationRecord/remove/" + row.id).then(response => {
          this.loadData();
          this.$message.success(response.data.message);
        })
      },
      executeQuery() {
        this.loadData();
      },
      pageChange(pageNo) {
        this.pagination.currentPage = pageNo;
        this.loadData();
      },
      resetForm(formName) {
        // this.$refs[formName].resetFields();
        this.searchForm={};
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
    },
    mounted() {
      this.routerParams= this.$route.query;
      if(this.routerParams!=null&&this.routerParams!=undefined&&this.routerParams!=''){
        this.searchForm.status=this.routerParams.status;
        this.searchForm.projectId=this.routerParams.projectId;
      }
      this.loadData();
      this.getDistType();//获取项目分类字典
    }
  };
</script>
