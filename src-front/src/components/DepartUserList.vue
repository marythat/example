<template>
  <el-dialog
    :width="modalWidth"
    :visible.sync="visible"
    @close="close"
    title="部门搜索"
    append-to-body
  >
  <el-row :gutter="10" style="overflow: scroll;height: 500px;">
    <el-col :md="8" :sm="24">
      <el-card :bordered="false">
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <!-- 树-->

          <template>

            <el-tree
              ref="tree"
              :data="departTree"
              node-key="key"
              @node-click="handleCheckChange"
              @check-change="handleCheckChange"
              :props="defaultProps">
            </el-tree>

          </template>

        </div>
      </el-card>
    </el-col>
    <el-col :md="16" :sm="24">
      <el-card :bordered="false">
            <Dept-User-Info ref="deptUserInfo" :dptId="dptId"></Dept-User-Info>
      </el-card>
    </el-col>
  </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import DeptUserInfo from './DeptUserInfo'

  export default {
    name: 'DepartUserList',
    components: {
      DeptUserInfo
    },
    data() {
      return {
        model:{}, // 存储SysUserDepartsVO表
        departList:[], // 存储部门信息
        multipleSelectionIds:[],
        multipleSelectionNames:[],
        modalWidth:"1200px",
        departTree:[],
        title:"操作",
        dptId:'',
        visible: false,
        headers:{},
        defaultProps: {
          children: 'children',
          label: 'title'
        },
      }
    },
    watch: {
      multipleSelectionIds(val){
        this.$nextTick(() => {
          this.$refs.deptUserInfo.open(val,this.multipleSelectionNames)
        })
      }
    },
    methods: {
      add (multipleSelectionIds,multipleSelectionNames) {
        this.visible = true;
        if(multipleSelectionIds&&multipleSelectionIds!=undefined)this.multipleSelectionIds = multipleSelectionIds.split(',');
        if(multipleSelectionNames&&multipleSelectionNames!=undefined)this.multipleSelectionNames = multipleSelectionNames.split(',');
      },
      handleCheckChange(data, checked, indeterminate) {
        this.dptId = data.key
/*        let param = {dptIds:data.key}
        this.$http.get("/epspBaseData/departmentTree",{params:param}).then((res)=>{
          if(res.data.success){
            this.departTree = res.data.data;
          }
        })*/
      },
      clearSelect(){
        if(this.$refs.deptUserInfo) {
          this.$refs.deptUserInfo.toggleSelection()
        }
      },
      handleSubmit () {
/*        const that = this;
        let formData = {departList:this.departList}
        console.log(formData)
        that.$emit('ok', formData);
        that.departList = [];
        that.confirmLoading = false;*/
        const that = this;
        let multipleSelectionIds = this.$refs.deptUserInfo.multipleSelectionNames?this.$refs.deptUserInfo.multipleSelectionIds:[]
        let multipleSelectionNames = this.$refs.deptUserInfo.multipleSelectionNames?this.$refs.deptUserInfo.multipleSelectionNames:[]
        for(let temp of this.$refs.deptUserInfo.currentSelectionIds){
          if(multipleSelectionIds.indexOf(temp.toString())==-1) {
            multipleSelectionIds.push(temp.toString())
          }
        }
        for(let temp of this.$refs.deptUserInfo.currentSelectionNames){
          if(multipleSelectionNames.indexOf(temp)==-1) {
            multipleSelectionNames.push(temp)
          }
        }
        that.$emit('ok', {ids:multipleSelectionIds,names:multipleSelectionNames});
        that.close();
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.visible = false;
        this.dptId = '';
        this.clearSelect()
      },
      queryDepartTree(){
        this.$http.get("/epspBaseData/departmentTree").then((res)=>{
          if(res.data.success){
            this.departTree = res.data.data;
            if(res.data.data.length>0){
              this.dptId = res.data.data[0].key
            }
          }
        })
      },
    },
    created(){
      this.queryDepartTree();
    },
  }
</script>
