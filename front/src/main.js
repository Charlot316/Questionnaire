import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
import installAxios from './plugins/axios'
import * as echarts from 'echarts'
import QRCode from 'qrcodejs2'
import axios from 'axios'
import { setCookie, getCookie, delCookie } from './plugins/cookie'
// import htmlToPdf from './utils/htmlToPdf'
const app = createApp(App)
installElementPlus(app)
installAxios(app)
import{
    getPdf
} from './utils/htmlToPdf'
app.config.globalProperties.$cookieStore = {
    setCookie,
    getCookie,
    delCookie,
}
app.config.globalProperties.$echarts = echarts
app.config.globalProperties.$QRCode = QRCode
app.config.globalProperties.$getPdf = getPdf
app.config.globalProperties.$axios = axios
app
    .use(store)
    .use(router)
    .mount('#app')
    // .use(htmlToPdf)

router.beforeEach((to, from, next) => {
    if(to.matched.some(res => res.meta.requireAuth)){
        if(store.state.islogin){
            next();
        }
        else{
            next({path: '/login',
            query: {redirect: to.fullPath} 
        });
           
        }
    }
    else{
        next();
    }
});
