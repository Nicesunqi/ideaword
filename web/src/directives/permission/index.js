/**
 * Created by xiaolei on 2017/5/17.
 */

const directive = {
  bind (el, binding, vnode) {
    var permission = Lockr.get('permission');
    var flag = false;
    permission.forEach(function (val) {
      if (binding.value == val.permission) {
        flag = true;
        return;
      }
    })

    if (!flag) {
      el.style.display = 'none'
    }

  }
}

export default directive

