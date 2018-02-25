<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>编辑角色</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/system/role/list' }">角色管理</el-breadcrumb-item>
                    <el-breadcrumb-item>编辑角色</el-breadcrumb-item>
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
            <el-form label-width="80px" label-position="right" :model="role" :rules="rules" ref="role"
                     class="edit-form">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="role.name" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item label="权限分配">
                    <el-tree
                            :data="permissions"
                            show-checkbox
                            node-key="id"
                            ref="tree"
                            :default-checked-keys="permissions_selected"
                            :props="{children:'children',label:'name'}">
                    </el-tree>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                role: {
                    name: ''
                },
                permissions: [],
                permissions_selected: [],
                rules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            save(){
                var data = {
                    id: this.$route.query.id,
                    name: this.role.name,
                    permissions: this.$refs.tree.getCheckedKeys()
                }
                this.$refs.role.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/role/update', data, (res)=> {
                            _g.toast('success', '保存成功')
                            router.go(-1)
                        })
                    }
                })

            },
            init(){
                api.request('admin/system/role/get', {id: this.$route.query.id}, (res)=> {
                    for(let i =0;i<res.data.permissions_selected.length;i++){
                        this.permissions_selected.push(res.data.permissions_selected[i].menu_id);
                    }
                    this.role.name = res.data.role.name
                    this.permissions = res.data.permissions

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