<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>城市管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item>城市管理</el-breadcrumb-item>
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
                    :render-content="renderContent"
                    :props="{children:'children',label:'name'}">
            </el-tree>
        </div>
    </div>
</template>

<script>

    export default {
        data () {
            return {
                permissions: [],
            }
        },
        methods: {
            renderContent(h, {node, data, store}) {
                return (
                    <span>
                        <span>
                            <span>{node.label}</span>
                        </span>
                        <span style="float: right; margin-right: 40px">
                            <span>{data.code}</span>
                        </span>
                    </span>);
            },
            init(){
                api.request('admin/system/city/all', {id: this.$route.query.id}, (res) => {

                    this.permissions = [{
                        id: -1,
                        name: '城市名称',
                        code: '城市编码',
                        expanded:true,
                        children: res.data
                    }]
                    setTimeout(()=>{
                        this.$refs.tree.root.childNodes[0].expand()

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