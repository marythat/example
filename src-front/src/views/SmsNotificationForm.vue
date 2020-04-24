<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :close-on-click-modal="false"
    append-to-body
    width="600px">
    <div>
      <el-table :data="tableData" ref="multipleTable" @select="handleSelectionChange">
        <el-table-column type="selection"/>
        <el-table-column prop="username" label="用户账号"/>
        <el-table-column prop="mobile" label="电话"/>
      </el-table>
      <div class="block" v-show="display">
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
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style>
</style>

<script>
  export default {
    props:["notification"],
    data() {
      return {
        tableData: [],
        visible:false,
        title: '发送短信通知人员列表',
        total: 0,
        currentPage: 1,
        pageSize:10,
        dptId:'',
        display:true
      }
    },
    created() {

    },
    methods: {
      handleSizeChange(val) {
        this.pageSize = val;
        this.selectDatas();
      },
      async selectDatas(){
        if(this.dptId!=""){
          let param = {dptId:this.dptId,pageSize:this.pageSize,pageNo:this.currentPage}
          await this.$http.get("/epspBaseData/departmentUser",{params:param}).then((res)=>{
            if(res.data.success){
              this.tableData = res.data.data.records;
              this.total = res.data.data.total;
            }
          })
        }
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.selectDatas();
      },
      handleSelectionChange(val,row) {

      },
      handleCancel(){
        this.visible=false;
      },
      handleSubmit(){
        this.notification(this.$refs.multipleTable.selection);
        this.visible=false;
      },
      async add(type,dptId,rowIds){
        this.tableData=[];
        switch (type) {
          case 0:
            this.dptId=dptId;
            this.display=true;
            await this.selectDatas();
            break;
          case 1:
            let param = {ids:dptId}
            this.display=false;
            await this.$http.get("/epspBaseData/searchUser",{params:param}).then((res)=>{
              if(res.data.success){
                this.tableData=res.data.data;
                this.total = res.data.data.length;
              }
            })
            break;
        }
        if(rowIds){
          this.tableData.forEach(row=>{
            let Ids=rowIds.split(",");
            Ids.forEach(id=>{
              if (row.userid==id){
                this.$nextTick(()=>{
                  this.$refs.multipleTable.toggleRowSelection(row,true);
                })
              }
            })
          })
        }
      }
    }
  };
</script>
