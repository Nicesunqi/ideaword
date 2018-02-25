import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'
import Index from '@/components/home/index'
import Login from '@/components/account/login'
import SystemUser from '@/components/system/user/list'
import SystemUserEdit from '@/components/system/user/edit'
import SystemUserAdd from '@/components/system/user/add'
import SystemRole from '@/components/system/role/list'
import SystemRoleEdit from '@/components/system/role/edit'
import SystemRoleAdd from '@/components/system/role/add'
import SystemMenu from '@/components/system/menu/list'
import SystemOffice from '@/components/system/office/list'
import StudentUser from '@/components/user/stuinfo/list'
import StudentUserEdit from '@/components/user/stuinfo/edit'
import StudentUserAdd from '@/components/user/stuinfo/add'
import StudentNotice from '@/components/user/notice/list'

// import SystemCity from '@/components/system/city/list'
import SystemDict from '@/components/system/dict/list'
import SystemDictAdd from '@/components/system/dict/add'
import SystemDictEdit from '@/components/system/dict/edit'
import SystemDictAddKeyValue from '@/components/system/dict/addKeyValue'

import AccountEditPassword from '@/components/account/editPassword'
import AccountEditInfo from '@/components/account/editInfo'

// import SystemConfig from '@/components/system/config/list'
// import SystemConfigAdd from '@/components/system/config/add'
// import SystemConfigEdit from '@/components/system/config/edit'

Vue.use(Router)

const routes = [
    {path: '/login', component: Login, name: 'login'},
    {
        path: '/',
        component: Main,
        children: [
            {path: '/', component: Index, name: 'index'},
            {path: '/editPassword', component: AccountEditPassword, name: 'accountEditPassword'},
            {path: '/editInfo', component: AccountEditInfo, name: 'accountEditInfo'}
        ]
    },
    {
        path: '/',
        component: Main,
        children: [
            {path: '/user/notice/list', component: StudentNotice, name: 'StudentNotice'},
            {path: '/user/stuinfo/add', component: StudentUserAdd, name: 'StudentUserAdd'},
            {path: '/user/stuinfo/edit', component: StudentUserEdit, name: 'studentUserEdit'},
            {path: '/user/stuinfo/list', component: StudentUser, name: 'studentUser'},
            {path: '/system/user/list', component: SystemUser, name: 'systemUser'},
            {path: '/system/user/edit', component: SystemUserEdit, name: 'systemUserEdit'},
            {path: '/system/user/add', component: SystemUserAdd, name: 'systemUserAdd'},
            {path: '/system/role/list', component: SystemRole, name: 'systemRole'},
            {path: '/system/role/edit', component: SystemRoleEdit, name: 'systemRoleEdit'},
            {path: '/system/role/add', component: SystemRoleAdd, name: 'systemRoleAdd'},
            {path: '/system/menu/list', component: SystemMenu, name: 'systemMenu'},
            {path: '/system/office/list', component: SystemOffice, name: 'SystemOffice'},
            // {path: '/system/city/list', component: SystemCity, name: 'SystemCity'},
            {path: '/system/dict/add', component: SystemDictAdd, name: 'SystemDictAdd'},
            {path: '/system/dict/edit', component: SystemDictEdit, name: 'SystemDictEdit'},
            {path: '/system/dict/list', component: SystemDict, name: 'SystemDict'},
            {path: '/system/dict/addKeyValue', component: SystemDictAddKeyValue, name: 'SystemDictAddKeyValue'},
            // {path: '/system/config/add', component: SystemConfigAdd, name: 'SystemConfigAdd'},
            // {path: '/system/config/edit', component: SystemConfigEdit, name: 'SystemConfigEdit'},
            // {path: '/system/config/list', component: SystemConfig, name: 'SystemConfig'}
        ]
    }
]
export default routes
/*export default {

    routes
}*/
