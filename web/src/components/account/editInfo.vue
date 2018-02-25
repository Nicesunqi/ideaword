<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>个人信息</h3>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button onclick="router.go(-1)"><i class="fa fa-chevron-left"></i>返回</el-button>
                    <el-button type="primary" @click="save"><i class="fa fa-save"></i>保存</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form label-width="80px" label-position="right" :model="user" :rules="rules" ref="user"
                     class="edit-form">
                <el-form-item label="账号" class="is-required">
                    <el-input v-model="user.account" placeholder="账号" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="nickname">
                    <el-input v-model="user.nickname" placeholder="姓名" :maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="user.email" placeholder="邮箱" :maxlength="100"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="role_keys">
                    <el-select
                            :disabled="true"
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
                            :disabled="true"
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
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            var officeValidator = (rules,value,callback) => {
                var arr = this.user.office_keys
                if(arr.length == 0){
                    callback(new Error('请选择部门'))
                }else{
                    callback()
                }
            }
            var verifyPasswordValidator = (rules,value,callback) =>{
                if(this.user.password){
                    if(!value){
                        callback(new Error("请确认密码"))
                    }else if(value != this.user.password){
                        callback(new Error("两次输入密码不一致"))
                    }else{
                        callback()
                    }
                }else{
                    callback()
                }
            }
            var passwordValidator =  (rules,value,callback) => {
                if(value){
                    this.$refs['user'].validateField('verifyPassword');
                    this.verifyPasswordFlag=false
                    var reg=/^[A-Za-z0-9]+$/
                    var flag = reg.test(value)
                    if(flag == false || value.length <6 || value.length > 16){
                        callback(new Error('长度为 6 到 16 个字母或数字'))
                    }else{
                        callback()
                    }
                }else{
                    this.verifyPasswordFlag=true
                    this.user.verifyPassword=''
                    callback()
                }
            }
            return {
                user: {
                    id: '',
                    account: '',
                    nickname: '',
                    email: '',
                    mobile: '',
                    password: '',
                    office_keys: [],
                    role_keys: [],
                    verifyPassword:'',
                },
                roles: [],
                offices: [],
                verifyPasswordFlag:true,
                rules: {
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
                    ]
                }
            }
        },
        methods: {
            save(){
                var data = {
                    id: this.$route.query.id,
                    nickname: this.user.nickname,
                    email: this.user.email,
                    mobile: this.user.mobile,
                };
                this.$refs.user.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/account/editInfo', data, (res)=> {
                            Lockr.set("userName",this.user.nickname);
                            _g.toast('success', '保存成功');
                            router.replace('/');
                            top.location.reload();
                        })
                    }
                })
            },
            init(){
                api.request('admin/system/user/get', {id: Lockr.get("id")}, (res)=> {
                    this.user.account = res.data.account;
                    this.user.nickname = res.data.nickname;
                    this.user.email = res.data.email;
                    this.user.mobile = res.data.mobile;
                    this.user.office_keys = res.data.office_id_list;
                    this.user.role_keys = res.data.roles.map(function (item) {
                        return item.id
                    })
                });
                api.request('admin/system/role/all', {}, (res)=> {
                    this.roles = res.data
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
<style>
    .verifyPasswordFlag{
        display:none
    }
</style>
<style scoped>


</style>