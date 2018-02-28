<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>信息管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item>公告类型</el-breadcrumb-item>
                    <el-breadcrumb-item>类型添加</el-breadcrumb-item>
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
            <el-form label-width="80px" label-position="right" :model="notype" :rules="rules" ref="notype"
                     class="edit-form">
                <el-form-item label="名称" prop="name" class="is-required">
                    <el-input v-model="notype.name" placeholder="名称"></el-input>
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
                notype: {
                    name: "",
                },
                rules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {
            save() {
                var data = {
                    name: this.notype.name,
                }
                this.$refs.notype.validate((valid) => {
                    if (valid) {
                        api.request('admin/impl/notype/add', data, (res) => {
                            _g.toast('success', '保存成功');
                            router.go(-1);
                        })
                    }
                })
            }
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