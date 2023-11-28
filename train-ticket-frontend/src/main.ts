import {createApp} from 'vue'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router'
import ElementPlus from 'element-plus'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
// import 'element-plus/dist/index.css'
import "~/styles/index.scss";
import "uno.css";
import routes from '~pages'
import App from './App.vue'
import order from "~/pages/order.vue";


routes.push( { path: '/order/:orderId', component: order },)

const router = createRouter({
    history: createWebHistory(),
    routes,
})





// @ts-ignore
createApp(App).use(createPinia()).use(ElementPlus, {
    locale: zhCn,
}).use(router).mount('#app')

// app.use(pinia);
// app.use(ElementPlus, {
//     locale: zhCn,
// })
// app.use(router)
//
//
// app.mount('#app')