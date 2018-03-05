import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import routes from './router'
import filter from './filter'
import VueRouter from 'vue-router'
// import Cropper from 'cropperjs'

import store from './vuex/store'

import axios from 'axios'
import qs from 'qs'
import Lockr from 'lockr'
import _ from 'lodash'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//
import moment from 'moment'

import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import _g from './assets/js/global'
import api from './assets/js/http'

import './assets/css/common.css';

Vue.config.productionTip = false;

// axios.defaults.baseURL = 'http://localhost:8880/';
// axios.defaults.baseURL = 'http://qinshi-admin.lianxiba.cn/';
axios.defaults.baseURL = 'http://220.168.63.96:7078/';
axios.defaults.timeout = 1000 * 60;
//axios.defaults.headers.token = Lockr.get('token');
axios.defaults.headers.token = sessionStorage.token;
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';
axios.interceptors.request.use(function (config) {
    if (config.data instanceof FormData) {

    } else {
        config.data = _g.urlEncode(config.data);
    }
    return config;
}, function (err) {
    return Promise.reject(err);
});

// var Cropper =

const router = new VueRouter({
    routes
});

router.beforeEach((to, from, next) => {
    NProgress.start();

    var t = sessionStorage.token;
    if(!t){
        localStorage.clear();
    }
    var token = Lockr.get('token');

    if (!token && to.name && to.name.toLocaleLowerCase() != 'login') {
        next({
            path: '/login',
            query: {redirect: to.fullPath}
        })
    }
    next()
});

router.afterEach(transition => {
    NProgress.done()
});

Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.use(moment);

window.Cropper = require('cropperjs');
window.router = router;
window.store = store;
window.axios = axios;
window.Lockr = Lockr;
window.bus = new Vue();
window._ = _;
window._g = _g;
window.api = api;
window.moment = moment;

new Vue({
    el: '#app',
    Cropper,
    router,
    store,
    filters: filter,
    template: '<App/>',
    components: {App}
}).$mount('#app');
