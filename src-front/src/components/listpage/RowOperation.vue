<template>
  <el-tooltip :content="tips" placement="bottom">
    <el-button :icon="icon"
               size="mini"
               :type="type"
               :disabled="disabled"
               @click="click" plain>
    </el-button>
  </el-tooltip>
</template>

<script>
  export default {
    props: {
      tips: {},
      icon: {default:'el-icon-setting'},
      disabled: {default:false},
      type: {default:'primary'}
    },
    methods: {
      click() {
        this.$emit("click");
      }
    }
  }
</script>
