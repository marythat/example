## 开发说明
本应用采用开发时前后端分类，部署时统一打包部署的方式部署应用

首次执行
1. ### 安装yarn  
    npm install -g yarn --registry=https://registry.npm.taobao.orgcd [project_root]/src-front
2. ### 项目初始化  
    cd [project_root]/src-front
3. ### 开发调试
    1. 前端  
        yarn install  
        yarn run dev
    2. 后端
        执行springboot启动类
4. ### 部署
    1. 打包到后端项目中  
        yarn build
    2. maven打包springboot项目产生jar 
    