<template>
  <el-menu
    :default-active="defaultActive"
    :collapse="isCollapse"
    @select="handleSelect">
    <el-submenu index="government" v-if="visible">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">政府端</span>
      </template>
      <el-menu-item index="report" v-if="visible3">统计</el-menu-item>
      <el-menu-item index="projectInformationList" v-if="visible1">项目管理</el-menu-item>
      <el-menu-item index="applicationRecordList" v-if="visible2">申请管理</el-menu-item>
    </el-submenu>
    <el-submenu index="enterprise" v-else>
      <template slot="title">
        <i class="el-icon-s-shop"></i>
        <span>企业端</span>
      </template>
      <el-menu-item index="projectInformationListCompany">项目列表</el-menu-item>
      <el-menu-item index="applicationRecordListCompany">申请记录</el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
  import store from '@/store'
  export default {
    data(){return {
      defaultActive:this.$route.name,
      visible: store.getters.userInfo.companyUser?false:true,
      visible1: store.getters.userInfo.epspUser.per!=null&&store.getters.userInfo.epspUser.per.indexOf('managerProject')!=-1,
      visible2: store.getters.userInfo.epspUser.per!=null&&(store.getters.userInfo.epspUser.per.indexOf('managerApply')!=-1||store.getters.userInfo.epspUser.per.indexOf('managerApplyAdmin')!=-1),
      visible3: store.getters.userInfo.epspUser.per!=null&&store.getters.userInfo.epspUser.per.indexOf('managerCount')!=-1,
      isCollapse:false
    }},
    watch:{
      '$route':function(to,from){
        this.defaultActive=to.name;
      }
    },
    methods:{
      handleSelect: function(key, keyPath) {
        this.defaultActive=key
        this.$router.push(key).catch(err=>{})
      }
    }
  }
</script>
