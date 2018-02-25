<template>
    <div class="page-login">
        <el-form :model="user" :rules="rules" ref="user" label-position="top" label-width="100px">
            <header>班级后台管理系统</header>
            <el-form-item label="账号" prop="account">
                <el-input type="text" v-model="user.account" auto-complete="off" @keyup.enter.native="login('user')"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="password">
                <el-input type="password" v-model="user.password" auto-complete="off" @keyup.enter.native="login('user')"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" :loading="loading" @click="login('user')" >登录</el-button>
            </el-form-item>
            <footer>琴石©2017-2018</footer>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                user: {
                    account: '',
                    password: ''
                },
                rules: {
                    account: [
                        {required: true, message: '请输入账号', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                    ]
                }
            };
        },
        computed: {
            loading () {
                return store.state.requestCount > 0
            }
        },
        methods: {
            login(user) {
                var data = {
                    'account': this.user.account,
                    'password': this.user.password
                };
                var redirect = this.$route.query.redirect;
                this.$refs.user.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/account/login', data, function (res) {

                            sessionStorage.token = res.data.token;

                            Lockr.set('userName',res.data.nick_name);
                            Lockr.set('account',res.data.account);
                            Lockr.set('id',res.data.id);
                            Lockr.set('token', res.data.token);
                            Lockr.set('permission', res.data.access);
                            window.axios.defaults.headers.token = Lockr.get('token');
                            redirect ? router.replace(redirect) : router.replace('/');
                        })
                    } else {
                        return false
                    }
                })
            },
            huiche(){
                alert("huiche");
            }
        }
    }
</script>

<style scoped>
    .page-login {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 100%;
        background-color: #f3f3f4;
    }

    .page-login form {
        width: 80%;
        max-width: 360px;
        margin: 100px auto 0px;
        background-color: #ffffff;
        padding: 15px 30px 20px 30px;
        box-shadow: 1px 1px 50px rgba(0,0,0,.3);
    }

    .page-login form header {
        font-size: 22px;
        text-align: center;
        border-bottom: 1px dashed #e7eaec;
        line-height: 60px;
        margin-bottom: 15px;
    }

    .page-login form footer {
        font-size: 14px;
        color: #666;
        text-align: right;
        margin-top: -15px;
    }

    .page-login form button {
        width: 100%;
        margin-top: 10px;
    }
</style>
