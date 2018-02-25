<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>字典管理</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                    <el-breadcrumb-item>字典管理</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button v-permission="'permission:dict:add'" type="primary" onclick="router.push('add')"><i class="fa fa-plus"></i>添加</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <el-form :inline="true" label-width="80px" label-position="right" :model="filters" class="search-form">
                <el-form-item label="类型">
                    <el-select filterable v-model="filters.form.type" clearable placeholder="类型">
                        <el-option
                                v-for="item in options"
                                :key="item"
                                :label="item"
                                :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="filters.form.remark" placeholder="描述"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search()"><i class="fa fa-search"></i>查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="layout-page-box">
            <el-table
                    :data="dict"
                    border
                    @sort-change="sortChanged"
                    style="min-width: 1000px;width:100%">
                <!--<el-table-column
                        label="序号"
                        align="center"
                        type="index"
                        width="65px"
                >
                </el-table-column>-->
                <el-table-column
                        prop="type"
                        label="类型"
                        min-width="180px"
                >
                </el-table-column>
                <el-table-column
                        prop="label"
                        label="标签"
                        min-width="180px"
                >
                </el-table-column>
                <el-table-column
                        prop="value"
                        label="键值"
                        min-width="150px"
                >
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="描述"
                        min-width="180px"
                >
                </el-table-column>
                <el-table-column
                        prop="sort"
                        label="排序"
                        min-width="80x"
                        sortable="custom"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        min-width="230px">
                    <template scope="scope">
                        <el-button-group>
                            <el-button v-permission="'permission:dict:edit'" @click="rowEdit(scope.$index, scope.row)" type="primary" size="small">
                                <i class="fa fa-edit"></i>编辑
                            </el-button>
                            <el-button v-permission="'permission:dict:del'" @click="rowDel(scope.$index, scope.row)" size="small">
                                <i class="fa fa-remove"></i>删除
                            </el-button>
                            <el-button v-permission="'permission:dict:add'" @click="rowAdd(scope.$index, scope.row)" size="small" type="primary">
                                <i class="fa el-icon-plus"></i>添加键值
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
                options:[],
                filters: {
                    form:{
                        remark:'',
                        type:''
                    },
                    pagination: {
                        page: 1,
                        limit: 10,
                        total: 0
                    },
                    order: ''
                },
                dict:[]
            }
        },
        methods: {
            search() {
                this.filters.pagination.page = 1
                this.refresh()
            },
            rowEdit(index, row) {
                router.push({name: 'SystemDictEdit', query: {id: row.id}})
            },
            rowAdd(index, row) {
                router.push({name: 'SystemDictAddKeyValue', query: {id: row.id}})
            },
            rowDel(index, row) {
                this.$confirm('此操作将删除该字典, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/system/dict/delete', {id: row.id}, (res)=> {
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
                    remark:this.filters.form.remark,
                    type:this.filters.form.type,
                    page: this.filters.pagination.page,
                    limit: this.filters.pagination.limit,
                    order: this.filters.order
                }
                api.request('admin/system/dict/search', queries, (res)=> {
                    this.dict = res.data.list
                    this.filters.pagination.total = parseInt(res.data.count)
                })
            },
            init(){
                api.request('admin/system/dict/getDictType', {}, (res)=> {
                    this.options = res.data
                    console.log( this.students )
                })
            }
        },
        created() {
            this.query(),
            this.init()
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

