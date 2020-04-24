// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import listPageComponents from "@/components/listpage";
import 'font-awesome/css/font-awesome.css'
import 'element-ui/lib/theme-chalk/index.css'
import { VueAxios } from "./request"
import '@/permission'
import store from './store/'
import Viser from 'viser-vue'
import axios from 'axios'

Vue.config.productionTip = false
Vue.use(Viser)
Vue.use(ElementUI)
Vue.use(VueAxios)
Vue.use(router)
Vue.use(listPageComponents)
Vue.prototype.$ele = ElementUI
Vue.prototype.$api_host= process.env.API_HOST;
Vue.prototype.$axios=axios;
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

