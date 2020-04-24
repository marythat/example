<template>
  <el-container>
    <el-header style="background: #3a8ee6;">
      <h3 style="color: white">项目申报系统</h3>
    </el-header>
    <el-container>
      <el-aside width="210px" style="border-right: #dddddd 1px solid">
        <admin-sidebar style="border-right: none"></admin-sidebar>
      </el-aside>
      <el-container  style="min-width: 900px;">
        <el-main >
          <router-view/>
        </el-main>
        <el-footer style="text-align: center">
          <h6 style="color:#bbbbbb;">CopyRight&copy;2019 All Rights Reserved</h6>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
  import AdminSidebar from '@/components/AdminSidebar'
  export default {
    name: 'AdminLayout',
    components: {
      AdminSidebar,
    }
  }
</script>

<style>
  html {
    overflow-x: hidden;
    overflow-y: scroll;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
    margin: 0;
    padding: 0;
    height: 100%;
  }

  body {
    position: relative;
    top: 0;
    left: 0;
    margin: 0;
    padding: 0;
    height: 100%;
  }
</style>
