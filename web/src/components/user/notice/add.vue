<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>信息管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item>通知公告</el-breadcrumb-item>
                    <el-breadcrumb-item>公告添加</el-breadcrumb-item>
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
            <el-form label-width="80px" label-position="right" :model="notice" :rules="rules" ref="notice"
                     class="edit-form">
                <el-form-item label="标题" prop="title" class="is-required">
                    <el-input v-model="notice.title" placeholder="标题"></el-input>
                </el-form-item>
                <el-form-item label="公告类型" prop="notype" class="is-required">
                    <el-select v-model="name" placeholder="请选择">
                        <el-option
                                v-for="item in notypes"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="内容" prop="content" class="is-required">
                    <el-input v-model="notice.content" placeholder="标题"></el-input>
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
                notice: {
                    title: "",
                    notype:""
                },
                notypes:[],
                rules: {
                    name: [
                        {required: true, message: '请输入标题', trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {
            save() {
                var data = {
                    title: this.notice.title,
                }
                this.$refs.notice.validate((valid) => {
                    if (valid) {
                        api.request('admin/impl/notice/add', data, (res) => {
                            _g.toast('success', '保存成功');
                            router.go(-1);
                        })
                    }
                })
            },
            loadNoticeType(){
                api.request('admin/impl/notype/list', {}, (res) => {
                    this.notypes = res.data.notice_type;
                })
            }
        },
        created(){
            this.loadNoticeType();
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