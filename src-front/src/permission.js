import router from './router'
import store from './store'
import ElementUI from 'element-ui'

router.beforeEach((to, from, next) => {
  let storage = window.localStorage
  // &&storage.getItem("token")=="undefined"&&storage.getItem("iv")=="undefined"
  if(to.query['token']&&to.query['iv']) {
    let token = to.query['token'];
    let iv = to.query['iv'];
    let phone = to.query['phone'];
    storage.setItem("token", token ? token.toString().replace(new RegExp(" ", "g"), "+") : token)
    storage.setItem("iv", iv)
    storage.setItem("phone", phone)
    if (!store.getters.routers||store.getters.routers.length === 0) {
      store.dispatch('SetRouters').then(res => {
        router.addRoutes(store.getters.routers)
        const redirect = decodeURIComponent(from.query.redirect || to.path)
        if (to.path === redirect) {
          next({...to, replace: true})
        } else {
          next({path: redirect})
        }
      }).catch(e => {
        ElementUI.Message(e.data.message)
      })
    }else{
      next()
    }
  }else {
    if (!store.getters.routers||store.getters.routers.length === 0) {
      store.dispatch('SetRouters').then(res => {
        router.addRoutes(store.getters.routers)
        const redirect = decodeURIComponent(from.query.redirect || to.path)
        if (to.path === redirect) {
          // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
          next({...to, replace: true})
        } else {
          // 跳转到目的路由
          next({path: redirect})
        }
      }).catch(e => {
        ElementUI.Message(e.data.message)
      })
    }else{
      next()
    }
  }
})
