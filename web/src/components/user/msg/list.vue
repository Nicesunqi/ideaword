<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>信息管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item>通知公告</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button v-permission="'permission:message:add'" type="primary" onclick="router.push('add')"><i
                            class="fa fa-plus"></i>添加
                    </el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form :inline="true" :model="filters" class="demo-form-inline">
                <el-form-item label="公告：">
                    <el-input v-model="filters.column.title"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="layout-page-box">
            <el-table
                    :data="messages"
                    border
                    style="width: 100%">
                <el-table-column
                        prop="content"
                        label="留言"
                        min-width="500">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="留言人"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="comment_size"
                        label="评价人数"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="create_date"
                        label="留言时间"
                        width="180">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        align="center"
                        min-width="130px">
                    <template scope="scope">
                        <el-button-group>
                            <el-button v-permission="'permission:message:edit'" @click="edit(scope.row)" type="primary" size="small"><i class="fa fa-edit"></i>查看</el-button>
                            <el-button v-permission="'permission:message:del'" @click="del(scope.row)" size="small"><i class="fa fa-remove"></i>删除</el-button>
                        </el-button-group>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                    @current-change="pageChanged"
                    :current-page="filters.pagination.page"
                    :page-size="filters.pagination.limit"
                    layout="total, prev, pager, next, jumper"
                    :total="filters.pagination.total">
            </el-pagination>
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
                filters: {
                    column: {
                        title: '',
                    },pagination: {
                        page: 1,
                        limit: 10,
                        total: 0
                    },
                },
                messages: []
            }
        },
        methods: {
            edit(row){
                router.push({name: 'MessageEdit', query: {id: row.id}})
            },
            del(row){
                this.$confirm('此操作将删除用户, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/impl/message/delete', {id: row.id}, (res)=> {
                        _g.toast('success', '删除成功!')
                        this.init()
                    })
                })
            },
            search() {
                this.filters.pagination.page = 1;
                router.push({
                    path:this.$route.path,
                    query:{title:this.filters.column.title}
                });
                api.request("admin/impl/message/list", this.$route.query, (res) => {
                    this.messages = res.data.messages;
                    this.filters.pagination.total = parseInt(res.data.total)
                })
            },
            init() {
                api.request("admin/impl/message/list", {}, (res) => {
                    this.messages = res.data.messages;
                    this.filters.pagination.total = parseInt(res.data.total)
                })
            }
        },
        created() {
            this.init();
        }
    }
</script>

<style scoped>

</style>