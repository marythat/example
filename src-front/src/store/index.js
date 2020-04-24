import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import getters from "./getters";
import AdminLayout from '@/components/AdminLayout'
import ProjectInformationList from '@/views/ProjectInformationList'
import ApplicationRecordList from '@/views/ApplicationRecordList'
import WorkFlowOperation from '@/views/WorkFlowOperation'
import ProjectInformationListCompany from '@/views/ProjectInformationList_Company'
import ApplicationRecordListCompany from '@/views/applicationRecordList_Company'
import WorkFlowOperationCompany from '@/views/WorkFlowOperation_Company'
import ProjectFlowRecordList from '@/views/ProjectFlowRecordList'
import Report from '@/views/Report'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    routers: [],
    userInfo: {}
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.routers = routers
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
    }
  },
  actions: {
    SetRouters({ commit }) {
      return new Promise((resolve, reject) => {
        let storage = window.localStorage
        let header = {
          'token': storage.getItem('token'),
          'iv': storage.getItem('iv')
        }
        if(storage.getItem('phone')) {
          header['phone'] = storage.getItem('phone')
        }
        axios({
          url: process.env.API_HOST + '/api/auth/curUser',
          method: 'get',
          headers: header
        }).then(response => {
          if (response.data.code == 200) {
            let routers = [];
            let per = response.data.data.epspUser.per
            if (response.data.data.companyUser) {
              routers = [{
                path: '/',
                redirect: 'projectInformationListCompany',
                component: AdminLayout,
                name: '首页',
                hidden: false,
                meta: {title: '首页', icon: '', noCache: true},
                children: [
                  //---------企业用户功能----------------
                  {
                    path: '/projectInformationListCompany',
                    name: 'projectInformationListCompany',
                    component: ProjectInformationListCompany,
                    meta: {title: '项目列表', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  },
                  {
                    path: '/applicationRecordListCompany',
                    name: 'applicationRecordListCompany',
                    component: ApplicationRecordListCompany,
                    meta: {title: '申请记录', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  },
                  {
                    path: '/workFlowOperationCompany',
                    name: 'workFlowOperationCompany',
                    component: WorkFlowOperationCompany,
                    meta: {title: '申请填写', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  },
                  {
                    path: '/projectFlowRecordList',
                    name: 'projectFlowRecordList',
                    component: ProjectFlowRecordList,
                    meta: {title: '流程审批信息记录', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  },
                ]
              }]
            } else {
              routers = [{
                path: '/',
                redirect: 'report',
                component: AdminLayout,
                name: '首页',
                hidden: false,
                meta: {title: '首页', icon: '', noCache: true},
                children: [
                  //---------部门用户功能----------------
                  {
                    path: '/workFlowOperation',
                    name: 'workFlowOperation',
                    component: WorkFlowOperation,
                    meta: {title: '审批界面', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  },
                ]
              }]
              if (per) {
                //managerApply,managerApplyAdmin,managerProject
                if(per.indexOf('managerCount')!=-1){
                  let reportPage={
                    path: '/report',
                    name: 'report',
                    component: Report,
                    meta: {title: '统计', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  }
                  routers[0].redirect = '/report'
                  routers[0].children.splice(0,0,reportPage)
                }
                if(per.indexOf('managerProject')!=-1){
                  let l = {
                    path: '/projectInformationList',
                    name: 'projectInformationList',
                    component: ProjectInformationList,
                    meta: {title: '项目配置', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  }
                  if(per.indexOf('managerCount')==-1) {
                    routers[0].redirect = '/projectInformationList'
                  }
                  routers[0].children.splice(0,0,l)
                }
                if(per.indexOf('managerApply')!=-1||per.indexOf('managerApplyAdmin')!=-1){
                  let l1 = {
                    path: '/applicationRecordList',
                    name: 'applicationRecordList',
                    component: ApplicationRecordList,
                    meta: {title: '申请管理', icon: 'fa fa-dashboard fa-lg', noCache: true}
                  }
                  if(per.indexOf('managerProject')!=-1){
                    routers[0].children.splice(1,0,l1)
                  }else{
                    if(per.indexOf('managerCount')==-1) {
                      routers[0].redirect = '/applicationRecordList'
                    }
                    routers[0].children.splice(0,0,l1)
                  }
                }
              }
            }
            //console.log(routers[0].children)
            commit("SET_USERINFO",response.data.data)
            commit('SET_ROUTERS', routers)
            resolve(response)
          }else{
            reject(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
  },
  getters
})
