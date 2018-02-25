import Vue from 'vue'
export default (function () {
  Vue.filter('roles', function (value) {
    value.map(function (item) {
      return {
        value: item.id,
        label: item.name
      };
    });
  })
})()
