<template>
  <el-dialog
    :width="modalWidth"
    :visible.sync="visible"
    title="部门搜索"
    append-to-body
    >
    <div class="tree">
      <el-tree
        ref="tree"
        :data="departTree"
        :check-strictly="true"
        show-checkbox
        node-key="key"
        @check="onCheck"
        :props="defaultProps">
      </el-tree>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "DepartWindow",
    data () {
      return {
        checkedKeys:[], // 存储选中的部门id
        model:{}, // 存储SysUserDepartsVO表
        departList:[], // 存储部门信息
        modalWidth:"600px",
        departTree:[],
        title:"操作",
        visible: false,
        headers:{},
        defaultProps: {
          children: 'children',
          label: 'title'
        },
      }
    },
    created(){
      this.queryDepartTree();
    },
    methods: {
      add (checkedDepartKeys) {
        let that = this
        that.edit({});
        that.$nextTick(() => {
          that.$refs.tree.setCheckedKeys(checkedDepartKeys);
          that.checkedKeys = checkedDepartKeys;
        })
      },
      edit (record) {
        this.visible = true;
        this.model = Object.assign({}, record);
      },
      close () {
        this.visible = false;
      },
      handleSubmit () {
        const that = this;
        let formData = {departList:this.departList}
        that.$emit('ok', formData);
        that.confirmLoading = false;
        that.close();
      },
      handleCancel () {
        this.close()
      },

      // 选择部门时作用的API
      onCheck(checkedKeys, info){
        this.$refs.tree.setCheckedKeys([checkedKeys.key]);
        this.departList = [];
        this.checkedKeys = [];
        this.departList.push(checkedKeys);
        this.checkedKeys.push(checkedKeys.key)
/*        let checkedNodes = info.checkedNodes;
        for (let i = 0; i < checkedNodes.length; i++) {
          let de = checkedNodes[i];
          let depart = {key:"",value:"",title:""};
          this.checkedKeys.push(de.key)
          depart.key = de.key;
          depart.value = de.key;
          depart.title = de.title;
          this.departList.push(depart);
        }*/
        //console.log('onCheck', checkedKeys, info);
      },
      queryDepartTree(){
        this.$http.get("/epspBaseData/departmentTree").then((res)=>{
          if(res.data.success){
            this.departTree = res.data.data;
          }
        })
      },
      },
  }
</script>

<style scoped>
  .ant-table-tbody .ant-table-row td{
    padding-top:10px;
    padding-bottom:10px;
  }
  .el-tree {
    min-width:100%;
    font-size:14px;
    display: inline-block;;
  }
  .tree {
    overflow-y:auto;
    overflow-x: auto;
    height: 350px;
  }
</style>
