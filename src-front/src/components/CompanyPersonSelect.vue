<template>
  <el-dialog
    :width="modalWidth"
    :visible.sync="visible"
    :title="title"
    append-to-body
  >
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        prop="personname"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="mobile"
        label="电话">
      </el-table-column>
    </el-table>
<!--  <div style="margin-top: 20px">
    <el-button @click="toggleSelection()">确定</el-button>
  </div>-->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        id: null,
        index: null,
        title: null,
        tableData: [],
        multipleSelection: [],
        multipleSelectionRows: [],
        modalWidth:"1200px",
        visible: false
      }
    },
    watch: {
      multipleSelectionRows(val){
        val.forEach((item) => {
          this.$nextTick(() => {
            this.$refs.multipleTable.toggleRowSelection(item);
          })
        });
      }
    },
    methods: {
      loadData(row,index) {
        this.id = row.id
        this.index = index
        this.multipleSelection = []
        this.multipleSelectionRows = []
        this.$http.get("epspBaseData/companyPersons").then(response => {
          this.tableData = response.data.data;
          if(row.permissionKeys){
            let ids = row.permissionKeys.split(",")
            this.tableData.forEach((item1) => {
              ids.forEach((item2) => {
                if(item1.mobile==item2){
                  this.multipleSelection.push(item2)
                  this.multipleSelectionRows.push(item1)
                }
              });
            });
          }
          this.visible = true
        })
      },
      handleSelectionChange(val) {
        let multipleSelection = this.$refs.multipleTable.selection
        this.multipleSelection = []
        multipleSelection.forEach((item) => {
          this.multipleSelection.push(item.mobile)
        });
      },
      handleSubmit () {
        let params = {permissionKeys:this.multipleSelection.toString(),id:this.id,index:this.index}
        this.$emit('ok', params)
        this.close();
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.visible = false;
      },
    }
  }
</script>
