<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>修改密码</h3>
                <!--<el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/info/user/list' }">用户管理</el-breadcrumb-item>
                    <el-breadcrumb-item>编辑用户</el-breadcrumb-item>
                </el-breadcrumb>-->
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button onclick="router.go(-1)"><i class="fa fa-chevron-left"></i>返回</el-button>
                    <el-button type="primary" @click="save"><i class="fa fa-save"></i>保存</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form label-width="120px" label-position="right" :model="user" :rules="rules" ref="user"
                     class="edit-form">
                <!--<el-form-item label="姓名">
                    <el-input v-model="user.name" :disabled="true"></el-input>
                </el-form-item>-->
                <el-form-item label="原密码" prop="password">
                    <el-input type="password" v-model="user.password" placeholder="密码" :maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword" @change="resetForm('user')">
                    <el-input type="password" v-model="user.newPassword" placeholder="密码" :maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="确认新密码" prop="verifyNewPassword" class="is-required">
                    <el-input type="password" v-model="user.verifyNewPassword" placeholder="确认新密码" :maxlength="16"></el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            let verifyNewPasswordValidator = (rules,value,callback) =>{
                if(this.user.newPassword){
                    if(!value){
                        callback(new Error("请确认密码"));
                    }else if(value != this.user.newPassword){
                        callback(new Error("两次输入密码不一致"));
                    }else{
                        callback();
                    }
                }else{
                    callback();
                }
            };
            let newPasswordValidator =  (rules,value,callback) => {
                if(value){
                    this.$refs['user'].validateField('verifyNewPassword');
                    let reg=/^[A-Za-z0-9]+$/;
                    let flag = reg.test(value);
                    if(flag == false || value.length <6 || value.length > 16){
                        callback(new Error('长度为 6 到 16 个字母或数字'));
                    }else{
                        callback();
                    }
                }else{
                    callback()
                }
            };
            return {
                user: {
                    id: '',
                    name: '',
                    password: '',
                    newPassword: '',
                    verifyNewPassword:'',
                },
                rules: {
                    newPassword: [
                        {
                            required:true,message:'请输入新密码'
                        },
                        {
                            validator:newPasswordValidator
                        }
                    ],
                    verifyNewPassword:[
                        {
                            required:true,message:'请确认新密码',
                        },
                        {

                            validator:verifyNewPasswordValidator
                        }
                    ],
                    password:[
                        {
                            required:true,message:'请输入原密码'
                        }
                    ]
                }
            }
        },
        methods: {
            save(){
                let data = {
                    password: this.user.password,
                    newPassword: this.user.newPassword,
                    verifyNewPassword:this.user.verifyNewPassword
                };
                this.$refs.user.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/account/edit', data, (res)=> {
                            _g.toast('success', '修改成功');
                            /*Lockr.flush();*/
                            router.push('/');
                        })
                    }
                })
            }
            /*init(){
                api.request('admin/info/user/get', {id: this.$route.query.id}, (res)=> {
                    this.user.name = res.data.name;
                    this.user.mobile = res.data.mobile
                })
            }*/
        },
        created() {
            this.user.name = Lockr.get("userName");
            this.user.id = Lockr.get("id");
        }
    }
</script>
<style scoped>


</style>