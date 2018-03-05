<template>
    <div class="layout">
        <div class="layout-menu-left" id="layout-menu-left">
            <div class="layout-profile">
                <img class="img-radius" src="../assets/logo.png">
                <el-dropdown trigger="click" @command="handleCommand" menu-align="start" @visible-change="visibleChange"
                             style="margin-top: 10px;">
                    <span class="el-dropdown-link">
                      {{userName}}<i class="el-icon--right" :class="dropdownIcon"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown" class="menu-conter">
                        <el-dropdown-item command="info">个人信息</el-dropdown-item>
                        <el-dropdown-item command="editPwd">修改密码</el-dropdown-item>
                        <el-dropdown-item divided command="exit">退出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <el-menu default-active="1" :router=true :unique-opened=true class="el-menu-root">
                <el-menu-item index="/"><i class="el-icon-menu"></i>首页</el-menu-item>
                <el-submenu index="/user" v-permission="'permission'">
                    <template slot="title"> <i class="el-icon-menu"></i>信息管理</template>
                    <div class="scroll-bar-div">
                        <el-menu-item index="/user/stuinfo/list" v-permission="'permission:studentUser'">学生管理</el-menu-item>
                        <el-menu-item index="/user/notype/list" v-permission="'permission:noticeType'">公告类型</el-menu-item>
                        <el-menu-item index="/user/notice/list" v-permission="'permission:notice'">通知公告</el-menu-item>
                        <el-menu-item index="/user/msg/list" v-permission="'permission:message'">留言管理</el-menu-item>
                    </div>
                </el-submenu>
                <el-submenu index="/system" v-permission="'permission'">
                    <template slot="title"><i class="el-icon-menu"></i>系统管理</template>
                    <div class="scroll-bar-div">
                        <el-menu-item index="/system/user/list" v-permission="'permission:user'">后台用户管理</el-menu-item>
                        <el-menu-item index="/system/role/list" v-permission="'permission:role'">角色管理</el-menu-item>
                        <el-menu-item index="/system/office/list" v-permission="'permission:office'">部门管理</el-menu-item>
                        <el-menu-item index="/system/menu/list" v-permission="'permission:menu'">菜单管理</el-menu-item>
                        <el-menu-item index="/system/dict/list" v-permission="'permission:dict'">字典管理</el-menu-item>
                        <!--<el-menu-item index="/system/config/list" v-permission="'permission:config'">系统配置</el-menu-item>-->
                        <!--<el-menu-item index="/system/city/list" v-permission="'permission:city'">城市管理</el-menu-item>-->
                    </div>
                </el-submenu>
            </el-menu>
        </div>
        <div class="layout-page" id="layout-page">
            <div class="layout-top">
                <a href="#" @click="exit"><i class="fa fa-sign-out"></i>退出</a>
            </div>
            <div class="layout-page-content" v-loading="loading">
                <transition name="vux-pop-in" mode="out-in" appear>
                    <router-view></router-view>
                </transition>
            </div>
        </div>
    </div>
</template>

<script>
    import permission from '../directives/permission/index'

    export default {
        directives: {
            permission
        },
        data() {
            return {
                dropdownIcon: 'el-icon-caret-bottom',
                userName: '',
                scrollMaxHeight: ''
            }
        },
        computed: {
            loading() {
                return store.state.requestCount > 0
            }
        },

        methods: {
            exit() {
                Lockr.flush();
                router.push('/login')
            },
            handleCommand(command) {
                if (command == 'exit') {
                    Lockr.flush();
                    router.push('/login')
                }
                if (command == 'editPwd') {
                    router.push('/editPassword');
                }
                if (command == 'info') {
                    router.push('/editInfo');
                }
            },
            visibleChange(flag) {
                if (flag) {
                    this.dropdownIcon = 'el-icon-caret-top'
                } else {
                    this.dropdownIcon = 'el-icon-caret-bottom'
                }

            },
            init() {
//                api.request('admin/system/user/get', {id: Lockr.get("id")}, (res)=> {
//                    this.userName = res.data.nickname;
//                });
            },
            onload() {
                var menu = document.getElementById("layout-menu-left");
                var content = document.getElementById("layout-page");
                var menuH = menu.offsetHeight;  //高度
                var contentH = content.offsetHeight;
                var scrolls = document.getElementsByClassName("scroll-bar-div");
                var x = contentH - menuH;
                for (var i = 0; i < scrolls.length; i++) {
                    scrolls[i].setAttribute('style', 'max-height: ' + x + 'px');
                }
            }
        },
        mounted() {
            this.onload();
        },
        created() {
            this.userName = Lockr.get("userName");
            this.init();
        }
    }
</script>

<style>

    .layout-menu-left {
        width: 200px;
        min-height: 400px;
        background-color: #2f4050;
    }

    .layout-profile {
        background-color: #263949;
        text-align: center;
        padding: 25px 0px;
    }

    .layout-profile > img {
        width: 80px;
        height: 80px;
        display: block;
        margin: 0 auto;
    }

    .layout-profile .el-dropdown {
        color: #ccc;
    }

    .layout-page {
        position: absolute;
        top: 0px;
        right: 0px;
        left: 200px;
        bottom: 0px;
        /*min-height: 950px;*/
        overflow-y: auto;
        background-color: #f3f3f4;
    }

    .layout-top {
        line-height: 35px;
        height: 35px;
        padding: 10px 15px;
    }

    .layout-top a {
        float: right;
        color: #999c9e;
        text-decoration: none;
        font-weight: bold;
        font-size: 15px;
    }

    .layout-top a i {
        margin-right: 5px;
    }

    .layout-page-content {
        padding: 0px 15px;
    }

    .layout-page-heading {
        background-color: #fff;
        margin: 0px -15px;
        padding: 10px 25px 15px;
    }

    .layout-page-heading h3 {
        margin: 0;
        line-height: 45px;
        font-weight: normal;
    }

    .layout-page-heading-action {
        margin-top: 15px;
        text-align: right;
    }

    .layout-page-box {
        background-color: #ffffff;
        padding: 15px 20px 20px 20px;
        margin: 15px 0px;
        border: 1px solid #eaeefb;
        border-radius: 4px;
        transition: .2s;
    }

    .layout-page-box:hover {
        box-shadow: 0 0 8px 0 rgba(232, 237, 250, .6), 0 2px 4px 0 rgba(232, 237, 250, .5);
    }

    .img-radius {
        border-radius: 50%;
    }

    .menu-conter {
        left: 48px !important;
    }

    .scroll-bar-div {
        /*max-height: 500px;*/
        overflow: auto;
    }

    /*滚动条样式*/
    .scroll-bar-div::-webkit-scrollbar {
        background-color: #52E3C6;
        height: 1px;
        width: 3px;
    }
</style>
