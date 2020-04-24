<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :close-on-click-modal="false"
    width="800px">
    <el-form ref="form" :model="model" :rules="validateRules" label-width="80px">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="model.companyName"></el-input>
      </el-form-item>
      <el-form-item label="创建人" prop="creatorName">
        <el-input v-model="model.creatorName"></el-input>
      </el-form-item>
      <el-form-item label="活动区域">
        <el-select v-model="model.region" placeholder="请选择活动区域">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动时间">
        <el-col :span="11">
          <el-date-picker type="date" placeholder="选择日期" v-model="model.date1" style="width: 100%;"></el-date-picker>
        </el-col>
        <el-col class="line" :span="2">-</el-col>
        <el-col :span="11">
          <el-time-picker placeholder="选择时间" v-model="model.date2" style="width: 100%;"></el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="即时配送">
        <el-switch v-model="model.delivery"></el-switch>
      </el-form-item>
      <el-form-item label="特殊资源">
        <el-radio-group v-model="model.resource">
          <el-radio label="线上品牌商赞助"></el-radio>
          <el-radio label="线下场地免费"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="活动形式">
        <el-input type="textarea" v-model="model.desc"></el-input>
      </el-form-item>
      <el-form-item :span="24">
        <el-button type="primary" @click="onSubmit" :disabled="disableSubmit">保 存</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
  </span>
  </el-dialog>
</template>

<script>
  export default {
    props:[
      "refreshFunction"
    ],
    data() {
      return {
        title: "编辑",
        visible: false,
        disableSubmit: false,
        model:{},
        validateRules: {
          companyName: [
            { required: true, message: '请输入公司名称', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.resetForm('form')
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      onSubmit() {
        this.disableSubmit = true;
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.$http.post("applicationRecord/save",this.model).then(response => {
              this.$message.success(response.data.message);
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
      }
    }
  };
</script>
