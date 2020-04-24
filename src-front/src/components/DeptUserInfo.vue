<template>
  <el-card :bordered="false">
    <!-- table区域-begin -->
    <div>

      <el-table :data="tableData" ref="multipleTable" @select="handleSelectionChange">
        <el-table-column type="selection"/>
        <el-table-column prop="username" label="用户账号"/>
        <el-table-column prop="mobile" label="电话"/>
      </el-table>
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </el-card>
</template>

<script>

  export default {
    name: "DeptUserInfo",
    components: {
    },
    props:{
      dptId:{
      }
    },
    data() {
      return {
        description: '用户信息',
        total: 0,
        currentPage: 1,
        currentSelectionIds: [],
        currentSelectionNames: [],
        multipleSelectionIds: [],
        multipleSelectionNames: [],
        multipleSelectionRows: [],
        pageSize:10,
        tableData: [],
        url: {
          list: "/sys/user/departUserList",
          edit: "/sys/user/editSysDepartWithUser",
          delete: "/sys/user/deleteUserInDepart",
          deleteBatch: "/sys/user/deleteUserInDepartBatch",
        }
      }
    },
    created() {
    },
    watch: {
      dptId (val) {
        if(val){
          this.dptId = val
          this.selectDatas();
        }
      },
      multipleSelectionRows(val){
        val.forEach((item) => {
          this.$nextTick(() => {
            this.$refs.multipleTable.toggleRowSelection(item);
          })
        });
      }
    },
    methods: {
      // this.$refs.multipleTable.selection
      handleSizeChange(val) {
        this.pageSize = val;
        this.selectDatas();
      },
      selectDatas(){
        if(this.dptId){
          let param = {dptId:this.dptId,pageSize:this.pageSize,pageNo:this.currentPage}
          this.multipleSelectionRows = []
          let ids = []
          this.$http.get("/epspBaseData/departmentUser",{params:param}).then((res)=>{
            if(res.data.success){
              this.tableData = res.data.data.records;
              this.total = res.data.data.total;
              let multipleSelect = this.$refs.multipleTable.selection
              for(let temp of multipleSelect){
                if(this.multipleSelectionIds.indexOf(temp.userid.toString())==-1) {
                  this.multipleSelectionIds.push(temp.userid.toString())
                  this.multipleSelectionNames.push(temp.username)
                }
              }
              ids = this.multipleSelectionIds.length>0?this.multipleSelectionIds:this.currentSelectionIds
              let  l = this
              this.tableData.forEach((item1) => {
                ids.forEach((item2) => {
                  if(item1.userid==item2){
                    this.multipleSelectionRows.push(item1)
                  }
                });
              });
            }
          })
        }
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.selectDatas();
      },
      open(multipleSelectionIds,multipleSelectionNames) {
        // this.currentDeptId = record.id;
        this.multipleSelectionIds = multipleSelectionIds
        this.multipleSelectionNames = multipleSelectionNames
        this.currentSelectionIds = multipleSelectionIds
        this.currentSelectionNames = multipleSelectionNames
      },
      toggleSelection() {
          this.$refs.multipleTable.clearSelection();
          this.multipleSelectionIds = [];
          this.multipleSelectionNames = [];
          this.currentSelectionIds = [];
          this.currentSelectionNames = [];
          this.tableData = [];
          this.total = 0;
          this.currentPage = 1;
          this.pageSize = 10;
      },
      handleSelectionChange(val,row) {
        let multipleSelect = this.$refs.multipleTable.selection
        this.currentSelectionIds = []
        this.currentSelectionNames = []
        for(let temp of multipleSelect){
            this.currentSelectionIds.push(temp.userid.toString())
            this.currentSelectionNames.push(temp.username)
        }
        if(this.currentSelectionIds.indexOf(row.userid.toString())==-1&&this.multipleSelectionIds.indexOf(row.userid.toString())!=-1){
          this.multipleSelectionIds.splice(this.multipleSelectionIds.indexOf(row.userid.toString()),1)
        }
        if(this.currentSelectionNames.indexOf(row.username)==-1&&this.multipleSelectionNames.indexOf(row.username)!=-1){
            this.multipleSelectionNames.splice(this.multipleSelectionNames.indexOf(row.username), 1)
        }
      }
    }
  }
</script>
<style scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .ant-card {
    margin-left: -30px;
    margin-right: -30px;
  }

  .table-page-search-wrapper {
    margin-top: -16px;
    margin-bottom: 16px;
  }
</style>
