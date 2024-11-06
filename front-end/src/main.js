import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en';
import '@/assets/css/global.css'
import '@/assets/css/theme/index.css'
import request from "@/utils/request";

Vue.config.productionTip = false

// 设置全局请求
Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL

// 统一使用 Element UI
Vue.use(ElementUI, {
    size: "small", // 设置全局组件的尺寸
    locale: locale // 设置语言
});

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
