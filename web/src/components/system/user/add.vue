<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>新增用户</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/system/user/list' }">后台用户管理</el-breadcrumb-item>
                    <el-breadcrumb-item>新增用户</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button onclick="router.go(-1)"><i class="fa fa-chevron-left"></i>返回</el-button>
                    <el-button type="primary" @click="save"><i class="fa fa-save"></i>保存</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <!--<el-form :model="user" :rules="rules" ref="user" class="search-form">-->
            <el-form label-width="80px" label-position="right" :model="user" :rules="rules" ref="user"
                     class="edit-form">
                <el-form-item label="账号" prop="account">
                    <el-input v-model="user.account" placeholder="账号" :maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="nickname">
                    <el-input v-model="user.nickname" placeholder="姓名" :maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="user.email" placeholder="邮箱" :maxlength="100"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="role_keys">
                    <el-select
                            v-model="user.role_keys"
                            multiple
                            filterable
                            placeholder="请选择角色">
                        <el-option
                                v-for="item in roles"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="部门" prop="offices" class="is-required">
                    <el-cascader
                            change-on-select
                            :clearable="true"
                            :options="offices"
                            :props="{value: 'id',label: 'name'}"
                            v-model="user.office_keys">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="联系电话" prop="mobile">
                    <el-input v-model="user.mobile" placeholder="联系电话" :maxlength="11"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="user.password" placeholder="密码" :maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="verifyPassword" class="is-required">
                    <el-input type="password" v-model="user.verifyPassword" placeholder="确认密码" :maxlength="16"></el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            var accountValidator = (rules,value,callback) => {
//                this.$refs['user'].validateField('verifyPassword');
                var reg=/^[A-Za-z0-9]+$/;
                var flag = reg.test(value);
                if(flag == false || value.length <6 || value.length > 16){
                    callback(new Error('长度为 6 到 16 个字母或数字'))
                }else{
                    callback()
                }
            };
            var passwordValidator = (rules,value,callback) => {
                this.$refs['user'].validateField('verifyPassword');
                var reg=/^[A-Za-z0-9]+$/;
                var flag = reg.test(value);
                if(flag == false || value.length <6 || value.length > 16){
                    callback(new Error('长度为 6 到 16 个字母或数字'))
                }else{
                    callback()
                }
            };
            var officeValidator = (rules,value,callback) => {
                var arr = this.user.office_keys;
                if(arr.length == 0){
                    callback(new Error('请选择部门'))
                }else{
                    callback()
                }
            };
            var verifyPasswordValidator = (rules,value,callback) =>{

                if(!value){
                    callback(new Error("请确认密码"));
                }else if(value != this.user.password){
                    callback(new Error("两次输入密码不一致"));
                }else{
                    callback();
                }
            };
            return {
                user: {
                    id: '',
                    account: '',
                    nickname: '',
                    email: '',
                    mobile: '',
                    password: '',
                    verifyPassword: '',
                    office_keys: [],
                    role_keys: []
                },
                rules: {
                    account: [
                        {required: true, message: '请输入账号', trigger: 'blur'},
                        {validator:accountValidator}
                    ],
                    nickname: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ],
                    email: [
                        {
                            pattern: /^([a-zA-Z0-9_\-\.])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$/,
                            message:'请输入正确的邮箱'
                        }
                    ],
                    role_keys: [
                        {
                            required:true,
                            message:'请选择角色'
                        }
                    ],
                    offices: [
                        {
                            validator:officeValidator
                        }
                    ],
                    mobile: [
                        {
                            required:true,
                            message:'请输入电话号码'
                        },
                        {
                            pattern:/^1[34578]\d{9}$/,
                            message:'请输入正确的电话号码'
                        }
                    ],
                    password: [
                        {
                            required:true,
                            message:'请输入密码'
                        },
                        {
                            validator:passwordValidator
                        }
                    ],
                    verifyPassword:[
                        {
                            validator:verifyPasswordValidator
                        }
                    ]

                },
                roles: [],
                offices: []
            }
        },
        methods: {
            save(){
                var data = {
                    id: this.$route.query.id,
                    account: this.user.account,
                    nickname: this.user.nickname,
                    email: this.user.email,
                    mobile: this.user.mobile,
                    password: this.user.password,
                    roles: this.user.role_keys,
                    verifyPassword:this.user.verifyPassword
                };
                if (this.user.office_keys.length) {
                    data.office_id = this.user.office_keys[this.user.office_keys.length - 1]
                }

                this.$refs.user.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/user/create', data, (res)=> {
                            _g.toast('success', '保存成功');
                            router.go(-1);
                        })
                    }
                })

            },
            init(){
                api.request('admin/system/role/all', {}, (res)=> {
                    this.roles = res.data;
                });
                api.request('admin/system/office/all', {}, (res)=> {
                    _g.trimTree(res.data);
                this.offices = res.data;
            })
            }
        },
        created() {
            this.init()
        }
    }
</script>

<style scoped>


</style>