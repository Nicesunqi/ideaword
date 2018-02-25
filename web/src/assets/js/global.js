const commonFn = {
    toast(type, msg) {
        bus.$message({
            message: msg,
            type: type
        })
    },
    notify(type, title, msg){
        bus.$notify({
            title: title,
            message: msg,
            type: type
        });
    },
    trimTree(tree) {
        for (var i = 0; i < tree.length; i++) {
            if (tree[i].children.length == 0) {
                delete tree[i].children
            }
            else {
                this.trimTree(tree[i].children)
            }
        }
    },
    sortFilter(sort){
        if (sort.prop) {
            return sort.prop + ' ' + sort.order.replace('descending', 'desc').replace('ascending', 'asc')
        } else {
            return ''
        }
    },
    /**
     * param 将要转为URL参数字符串的对象
     * key URL参数字符串的前缀
     * encode true/false 是否进行URL编码,默认为true
     *
     * return URL参数字符串
     */
    urlEncode(param, key, encode) {
        if(param==null) return '';
        var paramStr = '';
        var t = typeof (param);
        if (t == 'string' || t == 'number' || t == 'boolean') {
            paramStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);
        } else {
            for (var i in param) {
                var k = key == null ? i : key + (param instanceof Array ? '[]' : '.' + i);
                paramStr += this.urlEncode(param[i], k, encode);
            }
        }
        return paramStr;
    }
}

export default commonFn
