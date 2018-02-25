<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>信息管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item>学生管理</el-breadcrumb-item>
                    <el-breadcrumb-item>用户添加</el-breadcrumb-item>
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
            <el-form label-width="80px" label-position="right" :model="student" :rules="rules" ref="student"
                     class="edit-form">
                <el-form-item label="姓名" prop="name" class="is-required">
                    <el-input v-model="student.name" placeholder="姓名"></el-input>
                </el-form-item>
                <el-form-item label="学号" prop="stuNo" class="is-required">
                    <el-input v-model="student.stuNo" placeholder="学号"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex" class="is-required">
                    <el-radio v-model="student.sex" label="男">男</el-radio>
                    <el-radio v-model="student.sex" label="女">女</el-radio>
                </el-form-item>
                <el-form-item label="电话号码" prop="mobile" class="is-required">
                    <el-input v-model="student.mobile" placeholder="电话号码"></el-input>
                </el-form-item>
                <el-form-item label="生日" prop="birthday" class="is-required">
                    <el-date-picker
                            v-model="student.birthday"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="家庭住址" prop="address" class="is-required">
                    <el-input class="address" v-model="student.address" placeholder="家庭住址"></el-input>
                </el-form-item>
                <el-form-item label="QQ" prop="qq" class="is-required">
                    <el-input v-model="student.qq" placeholder="QQ"></el-input>
                </el-form-item>
                <el-form-item label="微信" prop="wechat" class="is-required">
                    <el-input v-model="student.wechat" placeholder="微信"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email" class="is-required">
                    <el-input v-model="student.email" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item label="头像" prop="picture" class="is-required">
                <el-upload
                        class="avatar-uploader"
                        action="http://upload.qiniup.com"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :on-error="handleError"
                        :before-upload="beforeAvatarUpload"
                        :data="postData">
                    <img v-if="student.picture" :src="domain+student.picture" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import permission from '../../../directives/permission/index'
    import api from "../../../assets/js/http";

    export default {
        directives: {
            permission
        },
        data() {
            return {
                student: {
                    id:"",
                    name: "",
                    stuNo: "",
                    sex: "男",
                    mobile: "",
                    birthday: '',
                    address: "",
                    qq: "",
                    wechat: "",
                    email: "",
                    picture:""
                },
                postData:{
                    token:"",
                    key:""
                },
                domain:"",
                rules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                    ],
                    stuNo: [
                        {required: true, message: '请输入学号', trigger: 'blur'},
                    ],
                    mobile: [
                        {
                            required: true,
                            message: '请输入电话号码'
                        },
                        {
                            pattern: /^1[34578]\d{9}$/,
                            message: '请输入正确的电话号码'
                        }
                    ],
                    birthday: [
                        {
                            required: true,
                            message: '请选择出生日期'
                        }
                    ],
                    address: [
                        {
                            required: true,
                            message: '请输入家庭地址'
                        }
                    ],
                    qq: [
                        {
                            required: true,
                            message: '请输入qq'
                        },
                        {
                            pattern: /^\d{5,10}$/,
                            message: '请输入正确的的qq号'
                        }
                    ],
                    wechat: [
                        {
                            required: true,
                            message: '请输入微信'
                        },
                        {
                            pattern: /^[a-z_\d]+$/,
                            message: '请输入正确的微信号'
                        }
                    ],
                    email: [
                        {
                            required: true,
                            message: '请输入邮箱'
                        }, {
                            pattern: /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/,
                            message: '请输入正确的的邮箱'
                        }
                    ]
                }
            }
        },
        methods: {
            save() {
                var data = {
                    id:this.student.id,
                    name: this.student.name,
                    stuNo: this.student.stuNo,
                    sex: this.student.sex,
                    mobile: this.student.mobile,
                    birthday: this.student.birthday,
                    address: this.student.address,
                    qq: this.student.qq,
                    wechat: this.student.wechat,
                    email: this.student.email,
                    picture:this.student.picture
                }
                this.$refs.student.validate((valid) => {
                    if (valid) {
                        api.request('admin/impl/user/add', data, (res) => {
                            _g.toast('success', '保存成功');
                            router.go(-1);
                        })
                    }
                })
            },
            handleAvatarSuccess(res, file) {
                debugger;
                this.student.picture =res.key;
                console.log("image:"+this.imageUrl);
            },
            handleError(res, file) {
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPng = file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;
                console.log(isPng)
                if (!isJPG && !isPng) {
                    this.$message.error('上传头像图片只能是 JPG或者PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                this.postData.key = "picture/img"+new Date().getTime();
                return (isJPG && isLt2M) || (isPng && isLt2M);
            },
            init(){
                if(!this.$route.query.id){
                    return;
                }
                api.request("admin/impl/user/list", {id: this.$route.query.id}, (res) => {
                    if(parseInt(res.data.total) > 0){
                       this.student.id = res.data.students[0].id;
                        this.student.name = res.data.students[0].name;
                        this.student.stuNo = res.data.students[0].stu_no;
                        this.student.sex = res.data.students[0].sex;
                        this.student.mobile = res.data.students[0].mobile;
                        this.student.birthday = res.data.students[0].birthday;
                        this.student.address = res.data.students[0].address;
                        this.student.qq = res.data.students[0].qq;
                        this.student.wechat = res.data.students[0].wechat;
                        this.student.email = res.data.students[0].email;
                        this.student.picture = res.data.students[0].picture;
                    }
                    console.log(res.data.students[0]);
                })
            },
            initToken(){
                api.request("admin/impl/upload/token",{}, (res) => {
                    this.postData.token = res.data.token;
                    this.domain = res.data.domain;
                    console.log(this.postData.token)
                })
            }
        },
        created() {
            this.init()
            this.initToken();
        }
    }
</script>

<style scoped>
    .layout-page-box .el-input {
        width: 30% !important;
    }
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>