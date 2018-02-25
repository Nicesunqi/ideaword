var prefix = '?XDEBUG_SESSION_START=18683';
const api = {
    request(url, data, cb, errCb) {
        store.dispatch('requestCounter', 1);
        url += prefix;
        axios.post(url, data).then((res) => {
            store.dispatch('requestCounter', -1);
            if (res.data.status == 0) {
                _g.toast('info', res.data.message)
            }
            else {
                cb(res.data)
            }
        }).catch((res) => {
            store.dispatch('requestCounter', -1);
            if (res.response) {
                console.log("res.response.status : "+res.response.status);
                if (res.response.status == 401) {
                    _g.toast('info', '登录失效,请重新登录!');
                    Lockr.flush();
                    // console.log("router.currentRoute.fullPath: "+router.currentRoute.fullPath);
                    router.push({path: '/login', query: {redirect: router.currentRoute.fullPath}});
                    return;
                }
                else if (res.response.status == 403) {
                    _g.toast('info', '无权限操作!');
                    return;
                }
            }
            if (errCb) {
                errCb(res)
            }
            else {
                _g.toast('error', res.message)
            }

        })
    }
};

export default api
