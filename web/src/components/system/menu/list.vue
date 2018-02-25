<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>菜单管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button type="primary" @click="init"><i class="fa fa-refresh"></i>刷新</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-tree
                    :data="permissions"
                    node-key="id"
                    ref="tree"
                    :default-expanded-keys="expandedKeys"
                    @node-expand="treeExpand"
                    @node-collapse="treeCollapse"
                    :render-content="renderContent"
                    :props="{children:'children',label:'name'}">
            </el-tree>
        </div>
        <el-dialog
                title="菜单操作"
                :visible.sync="dialog.visible"
                size="tiny">
            <el-form label-width="80px" label-position="top" :model="dialog" :rules="rules" ref="dialog"
                     class="edit-form">
                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="dialog.name" placeholder="菜单名称"></el-input>
                </el-form-item>
                <el-form-item label="权限编码" prop="permission">
                    <el-input v-model="dialog.permission" placeholder="权限编码"></el-input>
                </el-form-item>
                <el-form-item label="排序">
                    <el-input-number size="small" v-model="dialog.sort"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialog.visible = false">取 消</el-button>
                <el-button type="primary" @click="dialogCommit()">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    export default {
        data () {
            return {
                permissions: [],
                dialog: {
                    id: '',
                    name: '',
                    permission: '',
                    sort: 1,
                    type: '',
                    visible: false,
                    $currentNode: null
                },
                expandedKeys:[],
                rules: {}
            }
        },
        methods: {
            renderContent(h, {node, data, store}) {
                if(data.id==0){
                    return (
                        <span>
                            <span>
                                <span>{node.label}</span>
                            </span>
                            <span style="float: right; margin-right: 20px">
                                <el-button size="mini" on-click={ () => this.treeAdd(node, store, data) }>增加</el-button>
                                <el-button size="mini" on-click={ () => this.treeEdit(node, store, data) } disabled>编辑</el-button>
                                <el-button size="mini" on-click={ () => this.treeDelete(node, store, data) } disabled>删除</el-button>
                            </span>
                        </span>);
                }
                else {
                    return (
                        <span>
                            <span>
                                <span>{node.label}</span>
                            </span>
                            <span style="float: right; margin-right: 20px">
                                <el-button size="mini" on-click={ () => this.treeAdd(node, store, data) }>增加</el-button>
                                <el-button size="mini" on-click={ () => this.treeEdit(node, store, data) }>编辑</el-button>
                                <el-button size="mini" on-click={ () => this.treeDelete(node, store, data) }>删除</el-button>
                            </span>
                        </span>);
                }
            },
            treeExpand(data){
                this.expandedKeys.push(data.id)
            },
            treeCollapse(data){
                _.remove(this.expandedKeys, function(n) {
                    return n === data.id;
                });
            },
            treeAdd(node, store, data){
                event.stopPropagation()

                this.dialog.id = ''
                this.dialog.name = ''
                this.dialog.permission = ''
                this.dialog.sort = 1
                this.dialog.type = 'add'
                this.dialog.visible = true
                this.dialog.$currentNode = node


            },
            treeEdit(node, store, data){
                event.stopPropagation()

                this.dialog.id = data.id
                this.dialog.name = data.name
                this.dialog.sort = data.sort
                this.dialog.permission = data.permission
                this.dialog.type = 'edit'
                this.dialog.visible = true
                this.dialog.$currentNode = node

            },
            treeDelete(node, store, data){
                event.stopPropagation()

                this.$confirm('此操作将删除菜单, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/system/menu/delete', {
                        id: data.id
                    }, (res) => {
                        store.remove(data);
                        _g.toast('success', '删除成功!')
                    })
                })

            },
            dialogCommit(){
                if (this.dialog.type == 'add') {
                    api.request('admin/system/menu/create', {
                        pid: this.dialog.$currentNode.data.id,
                        name: this.dialog.name,
                        sort: this.dialog.sort,
                        permission: this.dialog.permission
                    }, (res) => {
                        this.init()
                    })
                }
                else {
                    api.request('admin/system/menu/update', {
                        id: this.dialog.id,
                        name: this.dialog.name,
                        sort: this.dialog.sort,
                        permission: this.dialog.permission
                    }, (res) => {
                        this.init()
                    })
                }
                this.dialog.visible = false
            },
            init(){
                api.request('admin/system/menu/all', {id: this.$route.query.id}, (res) => {

                    this.permissions = [{
                        id: 0,
                        name: '菜单',
                        permission: '',
                        expanded:true,
                        children: res.data
                    }]
                    setTimeout(()=>{
                        if(this.dialog.$currentNode){
                            this.dialog.$currentNode.expand()
                        }
                        else{
                            this.$refs.tree.root.childNodes[0].expand()
                        }

                    },0)
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