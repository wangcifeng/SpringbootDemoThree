import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import "ant-design-vue/dist/antd.css"
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'

axios.defaults.baseURL = process.env.VUE_APP_SERVER


// 添加请求拦截器
axios.interceptors.request.use(function (config) {
   console.log('请求参数',config);
   // 在发送请求之前做些什么
   return config;
 }, function (error) {
   // 对请求错误做些什么
   return Promise.reject(error);
 });

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
   console.log('返回结果',response);
   // 对响应数据做点什么
   return response;
 }, function (error) {
   console.log('返回错误',error);
   // 对响应错误做点什么
   return Promise.reject(error);
 });




const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

//全局使用图标
const icons:any = Icons;
for (const i in icons) {
   app.component(i,icons[i])
}

console.log('环境:',process.env.NODE_ENV);
console.log('服务端',process.env.VUE_APP_SERVER);