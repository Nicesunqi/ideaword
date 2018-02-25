<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>后台用户管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item>后台用户管理</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button v-permission="'permission:user:add'" type="primary" onclick="router.push('add')"><i class="fa fa-plus"></i>添加</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form :inline="true" label-width="80px" label-position="right" :model="filters" class="search-form">
                <el-form-item label="账号">
                    <el-input v-model="filters.column.account" placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="filters.column.name" placeholder="姓名"></el-input>
                </el-form-item>
                <el-form-item label="部门">
                    <el-cascader
                            change-on-select
                            :clearable="true"
                            :options="offices"
                            :props="{value: 'id',label: 'name'}"
                            v-model="filters.column.office_keys">
                    </el-cascader>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search()"><i class="fa fa-search"></i>查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="layout-page-box">
            <el-table
                    :data="users"
                    border
                    @sort-change="sortChanged"
                    style="min-width: 1000px;width:100%"><!--width: 100%-->
                <el-table-column
                        prop="account"
                        label="账号"
                        min-width="120px"
                       ><!--" fixed="left" width="150"-->
                </el-table-column>
                <el-table-column
                        prop="nickname"
                        label="姓名"
                        min-width="100px"
                        ><!--width="120"-->
                </el-table-column>
                <el-table-column
                        prop="office_name"
                        label="部门"
                        min-width="90px"
                        ><!--width="120"-->
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="邮箱"
                        min-width="170px"
                        ><!--width="300"-->
                </el-table-column>
                <el-table-column
                        prop="login_count"
                        label="登录次数"
                        sortable="custom"
                        min-width="120px"
                        ><!---->
                </el-table-column>
                <el-table-column
                        prop="last_login_time"
                        label="最后登录时间"
                        sortable="custom"
                        min-width="170px"
                        ><!--width="300"-->
                </el-table-column>
                <el-table-column
                        prop="last_login_ip"
                        label="最后登录IP"
                        min-width="140px"
                        ><!--width="120"-->
                </el-table-column>
                <el-table-column
                        prop="create_date"
                        label="创建时间"
                        sortable="custom"
                        min-width="170px"
                        ><!---->
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        align="center"
                        min-width="160px"><!--fixed="right" width="160"-->
                    <template scope="scope">
                        <el-button-group>
                            <el-button v-permission="'permission:user:edit'" @click="rowEdit(scope.$index, scope.row)" type="primary" size="small">
                                <i class="fa fa-edit"></i>编辑
                            </el-button>
                            <el-button v-permission="'permission:user:del'"@click="rowDel(scope.$index, scope.row)" size="small">
                                <i class="fa fa-remove"></i>删除
                            </el-button>
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
    export default {
        directives: {
            permission
        },
        data () {
            return {
                filters: {
                    column: {
                        account: '',
                        name: '',
                        office_keys: []
                    },
                    pagination: {
                        page: 1,
                        limit: 10,
                        total: 0
                    },
                    order: ''
                },
                users: [],
                offices: []
            }
        },
        methods: {
            search() {
                this.filters.pagination.page = 1
                this.refresh()
            },
            rowEdit(index, row) {
                router.push({name: 'systemUserEdit', query: {id: row.id}})
            },
            rowDel(index, row) {
                this.$confirm('此操作将删除用户, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/system/user/delete', {id: row.id}, (res)=> {
                        _g.toast('success', '删除成功!')
                        this.refresh()
                    })
                })
            },
            pageChanged(page) {
                this.filters.pagination.page = page
                this.refresh()
            },
            sortChanged(sort) {
                this.filters.order = _g.sortFilter(sort)
                this.refresh()
            },
            refresh(){
                router.push({
                    path: this.$route.path,
                    query: _.merge({}, this.$route.query, {__query: JSON.stringify(this.filters)}, {random: Math.random()})
                })
            },
            query(){
                var query = this.$route.query.__query
                if (query) {
                    this.filters = JSON.parse(query)
                }

                var queries = {
                    account: this.filters.column.account,
                    name: this.filters.column.name,
                    page: this.filters.pagination.page,
                    limit: this.filters.pagination.limit,
                    order: this.filters.order
                }
                if (this.filters.column.office_keys.length) {
                    queries.office_id = this.filters.column.office_keys[this.filters.column.office_keys.length - 1]
                }
                api.request('admin/system/user/search', queries, (res)=> {

                    this.users = res.data.users
                    this.filters.pagination.total = parseInt(res.data.total)
                })
            },
            init(){
                api.request('admin/system/office/all', {}, (res)=> {
                    _g.trimTree(res.data)
                    this.offices = res.data;
                })
            }
        },
        created() {

            this.init()
            this.query()
        },
        watch: {
            '$route' (to, from) {
                debugger;
                this.query()
            }
        }
    }
</script>

<style scoped>


</style>

