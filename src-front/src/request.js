import Vue from 'vue'
import { VueAxios } from './axios'
import axios from 'axios'
// 创建 axios 实例
const service = axios.create({
  baseURL: process.env.API_HOST, // api base_url
  headers: {post: {'Content-Type': "application/json;charset=UTF-8"}}
})

// request interceptor
service.interceptors.request.use(config => {
  let storage = window.localStorage
  let token = storage.getItem("token");
  let iv = storage.getItem("iv");
  let phone = storage.getItem("phone");
  if (token) {
    config.headers[ 'token' ] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  if(iv) {
    config.headers['iv'] = iv
  }
  if(phone) {
    config.headers['phone'] = phone
  }
  return config
},(error) => {
  return Promise.reject(error)
})

// response interceptor
service.interceptors.response.use((response) => {
  let storage = window.localStorage
  let token = response.headers['token'];
  let iv = response.headers['iv'];
  let phone = response.headers['phone'];
  if(iv&&token) {
    storage.setItem("token", token ? token.replace(new RegExp(" ", "gm"), "") : token)
    storage.setItem("iv", iv)
  }
  if(phone) {
    storage.setItem("phone", phone)
  }
  return response
  })

const installer = {
  vm: {},
  install (Vue, router = {}) {
    Vue.use(VueAxios, router, service)
  }
}

export {
  installer as VueAxios,
  service as axios
}
