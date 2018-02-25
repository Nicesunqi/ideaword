<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>角色管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item>角色管理</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button v-permission="'permission:role:add'" type="primary" onclick="router.push('add')"><i class="fa fa-plus"></i>添加</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form :inline="true" label-width="80px" label-position="right" :model="filters" class="search-form">
                <el-form-item label="名称">
                    <el-input v-model="filters.column.name" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search()"><i class="fa fa-search"></i>查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="layout-page-box">
            <el-table
                    :data="roles"
                    border
                    @sort-change="sortChanged"
                    style="width: 100%">
                <el-table-column
                        prop="name"
                        label="名称"
                >
                </el-table-column>
                <el-table-column
                        prop="create_date"
                        label="创建时间"
                        sortable="custom"
                >
                </el-table-column>
                <el-table-column
                        prop="update_date"
                        label="更新时间"
                        sortable="custom"
                >
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        align="center"
                        width="160">
                    <template scope="scope">
                        <el-button-group>
                            <el-button v-permission="'permission:role:edit'" @click="rowEdit(scope.$index, scope.row)" type="primary" size="small">
                                <i class="fa fa-edit"></i>编辑
                            </el-button>
                            <el-button v-permission="'permission:role:del'" @click="rowDel(scope.$index, scope.row)" size="small">
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
                        name: ''
                    },
                    pagination: {
                        page: 1,
                        limit: 10,
                        total: 0
                    },
                    order: ''
                },
                roles: []
            }
        },
        methods: {
            search() {
                this.filters.pagination.page = 1
                this.refresh()
            },
            rowEdit(index, row) {
                router.push({name: 'systemRoleEdit', query: {id: row.id}})
            },
            rowDel(index, row) {
                this.$confirm('此操作将删除角色, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/system/role/delete', {id: row.id}, (res)=> {
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
                    name: this.filters.column.name,
                    page: this.filters.pagination.page,
                    limit: this.filters.pagination.limit,
                    order: this.filters.order
                }

                api.request('admin/system/role/search', queries, (res)=> {
                    this.roles = res.data.roles
                    this.filters.pagination.total = parseInt(res.data.total)
                })
            },
            init(){

            }
        },
        created() {
            this.init()
            this.query()
        },
        watch: {
            '$route' (to, from) {
                this.query()
            }
        }
    }
</script>

<style scoped>


</style>

