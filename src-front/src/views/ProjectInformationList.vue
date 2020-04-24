<template>
  <div style="min-width: 1000px;">
    <el-container>
      <el-main>
        <el-row style="width: 100%;min-width: 1000px">
          <el-form ref="searchForm" :model="searchForm" label-width="80px">
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
                  <el-form-item label="申报时间" prop="commitDate">
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
                  <el-select v-model="searchForm.projectStatus" placeholder="请选择" >
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
                <el-form-item label="项目名称" prop="projectName">
                  <el-input v-model="searchForm.projectName" placeholder="请输入项目名称"/>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="项目编码" prop="projectCode">
                  <el-input v-model="searchForm.projectCode" placeholder="请输入项目编码" />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="所属科室" prop="belongDept">
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
          <el-button size="small" type="primary" @click="createNew" icon="el-icon-plus" plain>新增</el-button>
        </el-row>
        <!-- 数据展示区域 -->
        <el-row>
          <el-table :data="tableData">
            <el-table-column type="index" label="#"/>
            <el-table-column prop="id" label="项目ID" width="120px;" v-if="false"/>
            <el-table-column prop="projectName" label="项目名称"/>
            <el-table-column prop="projectCode" label="项目编码"/>
            <el-table-column prop="applyStartTime" label="申报时间"/>
            <el-table-column prop="applyEndTime" label="结束时间"/>
            <el-table-column prop="projectStatusStr" label="项目状态"/>
            <el-table-column prop="belongDept" label="项目所属科室"/>
            <el-table-column label="操作" width="350px;" align="center">
              <template slot-scope="scope">
                <el-button size="small" type="primary" @click="handleShow(scope.row)" style="margin-left: 0">预览模板</el-button>
                <el-button size="small" type="primary"  @click="handleEdit(scope.row)"  plain>编辑</el-button>
                <el-button size="small" type="primary"   @click="handleNodeConfig(scope.row)"  plain>流程配置</el-button>
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
      <project-information-form ref="modalForm" :refreshFunction="loadData"/>
      <project-flow-node-list ref="flowNodeList"/>
    </el-container>
  </div>
</template>

<style>
</style>

<script>
  import ProjectInformationForm from './ProjectInformationForm';
  import ProjectFlowNodeList from './ProjectFlowNodeList';
  import "@/components/listpage";
  import DeclarationGuideList from "./DeclarationGuideList";

  export default {
    components: {
      DeclarationGuideList,
      ProjectInformationForm, ProjectFlowNodeList
    },
    data() {
      return {
        tableData: [],
        searchForm: {
          column: 'projectName,projectCode',
          order: 'desc',
          applyStartTime_start:'',
          applyEndTime_end:'',
        },
        pagination: {currentPage: 1, currentSize: 10, total: 0},
        projectStatusOptions: [
          {value: 'ROUGHDRAFT', label: '草稿'},
          {value: 'AVAILABLE', label: '上线'},
          {value: 'CLOSED', label: '下线'}
        ],
        projectTypeOptions: [],
        tagOptions:[],
      }
    },
    created() {
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

        this.$http.get("projectInformation/list", {params: searchParams}).then(response => {
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
        })
      },
      loadDataTag() {
        let searchParams =  Object.assign({}, this.searchForm);
        if(searchParams.commitDate!=""&&searchParams.commitDate!=undefined){
          searchParams.commitDate=[];
        }
        this.$http.get("/epspBaseData/companyBusinessList", {params: searchParams}).then(response => {
          this.tagOptions = response.data.data;
        })
      },
      createNew() {
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.model.projectCode=this.getTime();//获取当前时间
        this.$refs.modalForm.add();
      },
      handleEdit(row) {
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit(row);
      },
      handleDelete(row) {
        this.$http.delete("projectInformation/remove/" + row.id).then(response => {
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
        this.searchForm={};
      },
      handleNodeConfig(row) {
        this.$refs.flowNodeList.searchForm.projectId = row.id;
        this.$refs.flowNodeList.projectName = row.projectName;
        this.$refs.flowNodeList.loadData();
        this.$refs.flowNodeList.visible = true;
      },
       getTime(){
        var date = new Date();
        //以下代码依次是获取当前时间的年月日时分秒
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var minute = date.getMinutes();
        var hour = date.getHours();
        var second = date.getSeconds();
        //固定时间格式
        if (month >= 1 && month <= 9) {
          month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
          strDate = "0" + strDate;
        }
        if (hour >= 0 && hour <= 9) {
          hour = "0" + hour;
        }
        if (minute >= 0 && minute <= 9) {
          minute = "0" + minute;
        }
        if (second >= 0 && second <= 9) {
          second = "0" + second;
        }
        var currentdate = year + month + strDate+ hour + minute + second;
        return currentdate;
      },
      handleShow(row){
        this.$http.get("/commAttachment/list",{params:{relatedId:row.id,type:"1"}}).then(response=>{
          if (response.data.data.records.length>0){
            window.open(this.$api_host+"/commAttachment/download/"+response.data.data.records[0].filePath+"?preview=true", "_blank");
          }else{
            this.$message.warning("该项目还没有上传模板文件，请先上传！")
          }
        });

      }
    }
  };
</script>
