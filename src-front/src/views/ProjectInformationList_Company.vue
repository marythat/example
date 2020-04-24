<template>
  <div style="min-width: 1000px;">
  <el-container>
    <el-main >
      <el-row style="width: 100%;min-width: 1000px">
        <el-form ref="searchForm" :model="searchForm" label-width="80px">
          <el-row>
            <el-col :span="7">
              <el-form-item label="申报时间" prop="applyStartTime">
                <el-date-picker
                  v-model="searchForm.commitDate"
                  type="datetimerange"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  :default-time="['00:00:00', '23:59:59']"
                  range-separator="至"
                  start-placeholder="申报开始时间"
                  end-placeholder="申报结束时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="项目状态" prop="projectStatus">
                <el-select v-model="searchForm.projectStatus" placeholder="请选择" style="width: 100%">
                  <el-option
                    v-for="item in projectStatusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="项目分类" prop="projectType">
                <el-select v-model="searchForm.projectType" placeholder="请选择" style="width: 100%">
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
            <el-col :span="7">
              <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="searchForm.projectName" placeholder="请输入项目名称" style="width: 350px"/>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="项目编码" prop="projectCode">
                <el-input v-model="searchForm.projectCode" placeholder="请输入项目编码" />
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="所属科室" prop="projectCode">
                <el-input v-model="searchForm.belongDept" placeholder="请输入项目所属科室" />
              </el-form-item>
            </el-col>
            <el-col :span="5" align="right">
              <el-form-item >
                <el-button type="primary" @click="executeQuery" icon="el-icon-search">查询</el-button>
                <el-button type="reset" icon="el-icon-refresh-left" @click="resetForm('searchForm')" >重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-row>
      <!-- 操作按钮区域 -->
      <el-row>
      </el-row>
      <!-- 数据展示区域 -->
      <el-row>
        <el-table :data="tableData">
          <el-table-column type="index" label="#"/>
          <el-table-column prop="projectName" label="项目名称"/>
          <el-table-column prop="projectCode" label="项目编码"/>
          <el-table-column prop="applyStartTime" label="申报时间"/>
          <el-table-column prop="applyEndTime" label="结束时间"/>
          <el-table-column prop="projectStatusStr" label="项目状态"/>
          <el-table-column prop="belongDept" label="项目所属科室"/>
          <el-table-column prop="askPhone" label="科室电话"/>
          <el-table-column label="操作" width="320px;" align="center">
            <template slot-scope="scope">
              <el-button size="small" type="primary"  @click="sendApplication(scope.row,'1')"  plain>申报指南详情</el-button>
              <el-button size="small" type="primary"  plain
                         :disabled="scope.row.projectStatus === 'CLOSED'" @click="sendApplication(scope.row,'0')">立即申报</el-button>
              <el-button size="small" type="primary" plain @click="handleSelect(scope.row,scope.$index)"
                         v-show="!(scope.row.projectStatus === 'CLOSED'|| visible)">申报授权</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <!--分页-->
      <table-pagination :pagination="pagination" @current-change="pageChange" />
    </el-main>

    <!-- 其他模态组件引入区域 -->
    <project-information-form ref="modalForm" :refreshFunction="loadData"/>
    <project-flow-node-list ref="flowNodeList" />
    <declaration-guide-list ref="declarationGuideList"/>
    <companyPersonSelect ref="companyPersonSelect" @ok="modalFormOk"></companyPersonSelect>
  </el-container>
  </div>
</template>

<style>
</style>

<script>
  import ProjectInformationForm from './ProjectInformationForm';
  import ProjectFlowNodeList from './ProjectFlowNodeList';
  import DeclarationGuideList from './DeclarationGuideList';
  import companyPersonSelect from '../components/CompanyPersonSelect'
  import store from '@/store'
  import "@/components/listpage";
    export default {
      components: {
        ProjectInformationForm,ProjectFlowNodeList,DeclarationGuideList,companyPersonSelect
      },
      data() {
        return {
          firstindustypeid: store.getters.userInfo.companyUser.firstindustypeid,
          visible: !(localStorage.phone == "undefined" || !localStorage.phone),
          tableData: [],
          searchForm: {
            column:'projectName,projectCode',
            order:'desc',
            applyStartTime_start:'',
            applyEndTime_end:'',
          },
          pagination: {currentPage: 1,currentSize: 10,total: 0},
          projectStatusOptions: [
            {value: 'ROUGHDRAFT', label: '草稿'},
            {value: 'AVAILABLE', label: '上线'},
            {value: 'CLOSED', label: '下线'}
          ],
          projectTypeOptions: [],
        }
      },
      created () {
        this.loadData();
        this.getDistType();//获取项目分类字典
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

          if(searchParams.companyTag!=""&&searchParams.companyTag!=undefined){
            searchParams.companyTag = "*"+searchParams.companyTag+"*";
          }

          if(searchParams.commitDate!=""&&searchParams.commitDate!=undefined){
            searchParams.applyStartTime_start=searchParams.commitDate[0];
            searchParams.applyEndTime_end= searchParams.commitDate[1];
            searchParams.commitDate=[];
          }
          this.$http.get("projectInformation/list", {params:searchParams}).then(response => {
            if (response.data.success) {
              this.tableData = response.data.data.records;
              this.pagination = {
                total: response.data.data.total,
                currentPage: response.data.data.current,
                currentSize: response.data.data.size
              };
            }else {
              this.$message(response.data.message);
            }
            })
        },
        getDistType() {
          let param = {"dict_type":"sys_project"}
          this.$http.get("sysDistType/list",{params:param}).then(response => {
            this.projectTypeOptions = response.data.data.records;
          })
        },
        sendApplication(row,type){
          if(type=="0"){
            if(store.getters.userInfo.companyUser.businesstypeid&&store.getters.userInfo.companyUser.businesstypeid.split("、").indexOf("45")!=-1){
              this.verification(row,true);
            }else{
              this.verification(row);
              // this.$router.push("WorkFlowOperationCompany?projectId="+row.id+"&firstindustypeid="+this.firstindustypeid);
            }
          }
         if(type=="1"){
           this.$refs.declarationGuideList.visible=true;
           this.$refs.declarationGuideList.loadData(row);
         }
        },
        verification(row,flag){//判断是否达到可申请数
          /*let param = {"project_id":row.id};
          this.$http.get("applicationRecord/queryApplyCount",{params:param}).then(response => {
            if(response.data.code=="200"&&response.data.data.bool){
              if(companyExcelBusinessMap){
                this.$router.push({
                  path:"WorkFlowOperationCompany?projectId="+row.id+"&firstindustypeid="+this.firstindustypeid,
                  query:{
                    companyExcelBusinessMap:JSON.stringify(companyExcelBusinessMap)
                  }
                })
              }else {
                this.$router.push("WorkFlowOperationCompany?projectId=" + row.id + "&firstindustypeid=" + this.firstindustypeid);
              }
            }else{
              this.$message.warning(response.data.data.message);
            }
          })*/


          let param = {"project_id":row.id};
          this.$http.get("applicationRecord/queryApplyCount",{params:param}).then(response => {

            if(response.data.code=="200"&&response.data.data.bool){
              //判断是否为第一次申请
              if(response.data.data.count>=1){
                this.$confirm("项目申请存在申请（或草稿）记录，是否进入查看", '提示', {
                  confirmButtonText: '不进入，立即申报',
                  cancelButtonText: '进入申报记录',
                  type: 'warning'
                }).then(() => {
                    if(flag){
                      this.$router.push({
                        path:"WorkFlowOperationCompany?projectId="+row.id+"&firstindustypeid="+this.firstindustypeid+"&flag="+flag,
                      })
                    }else {
                      this.$router.push("WorkFlowOperationCompany?projectId=" + row.id + "&firstindustypeid=" + this.firstindustypeid);
                    }
                }).catch(() => {
                  this.$router.push({path: '/applicationRecordListCompany',query: {status: null}});
                });
              }else{
                  if(flag){
                    this.$router.push({
                      path:"WorkFlowOperationCompany?projectId="+row.id+"&firstindustypeid="+this.firstindustypeid+"&flag="+flag,
                    })
                  }else {
                    this.$router.push("WorkFlowOperationCompany?projectId=" + row.id + "&firstindustypeid=" + this.firstindustypeid);
                  }
              }
            }else{
              //申报数加草稿累计达到限定可申报数
              if(response.data.data.draftBool){
                this.$message.warning(response.data.data.message);
              }else{
                this.$confirm(response.data.data.message, '提示', {
                  confirmButtonText: '进入存稿记录',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.$router.push({path: '/applicationRecordListCompany', query: {status: 'DRAFT',"projectId":row.id}})
                }).catch(() => {
                  this.$message({type: 'info', message: '已取消操作'});
                });
              }

            }

          })


        },
        executeQuery(){
          this.loadData();
        },
        pageChange(pageNo){
          this.pagination.currentPage = pageNo;
          this.loadData();
        },
        resetForm(formName){
            //this.$refs[formName].resetFields();
          this.searchForm={};
        },
        handleNodeConfig(row){
          this.$refs.flowNodeList.searchForm.projectId=row.id;
          this.$refs.flowNodeList.projectName=row.projectName;
          this.$refs.flowNodeList.loadData();
          this.$refs.flowNodeList.visible=true;
        },
        handleSelect(row,index){
          this.$refs.companyPersonSelect.title = "授权对象";
          let param = Object.assign({}, row)
          param.id = row.ncpId
          this.$refs.companyPersonSelect.loadData(param,index);
        },
        modalFormOk(params){
          let param = Object.assign({}, params)
          param["projectId"] = this.tableData[params.index].id
          delete param["index"]
          this.$http.post("companyPermission/save",param).then(response => {
            if (response.data.code == 200) {
              this.tableData[params.index].permissionKeys = params.permissionKeys
              this.tableData[params.index].ncpId = response.data.data
              this.$message.success('提交成功');
            }else{
              this.$message.error('提交失败');
            }
          })
        },
      }
    };
</script>
