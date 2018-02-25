<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>系统配置</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item>系统配置</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
            <el-col :span="8">
                <div class="layout-page-heading-action">
                    <el-button v-permission="'permission:config:add'" type="primary" onclick="router.push('add')"><i class="fa fa-plus"></i>添加</el-button>
                </div>
            </el-col>
        </el-row>
        <div class="layout-page-box">
            <!--<el-form :inline="true" label-width="120px" label-position="right" :model="filters" class="search-form">
                <el-form-item label="参数名称">
                    <el-input v-model="filters.column.name" placeholder="参数名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search()"><i class="fa fa-search"></i>查询</el-button>
                </el-form-item>
            </el-form>-->
        </div>
        <div class="layout-page-box">
            <el-table
                    :data="configs"
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
                        prop="name"
                        label="参数名称"
                        min-width="120px"
                >
                </el-table-column>
                <el-table-column
                        prop="type_label"
                        label="参数类型"
                        min-width="80px"
                >
                </el-table-column>
                <el-table-column
                        prop="key"
                        label="参数Key"
                        min-width="80px"
                >
                </el-table-column>
                <el-table-column
                        prop="value"
                        label="参数值"
                        min-width="80px"
                >
                </el-table-column>
                <el-table-column
                        prop="def"
                        label="默认值"
                        min-width="80px"
                >
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="描述"
                        min-width="220px"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        min-width="160px">
                    <template scope="scope">
                        <el-button-group>
                            <el-button v-permission="'permission:config:edit'" @click="rowEdit(scope.$index, scope.row)" type="primary" size="small">
                                <i class="fa fa-edit"></i>编辑
                            </el-button>
                            <!--<el-button v-permission="'permission:config:del'" @click="rowDel(scope.$index, scope.row)" size="small">
                                <i class="fa fa-remove"></i>删除
                            </el-button>-->
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
                    id:'',
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
                configs:[]
            }
        },
        methods: {
            search() {
                this.filters.pagination.page = 1;
                this.refresh()
            },
            rowEdit(index, row) {
                router.push({name: 'SystemConfigEdit', query: {id: row.id}})
            },
            rowDel(index, row) {
                this.$confirm('此操作将删除该配置, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    api.request('admin/system/config/delete', {id: row.id}, (res)=> {
                        _g.toast('success', '删除成功!');
                        this.refresh()
                    })
                })
            },
            pageChanged(page) {
                this.filters.pagination.page = page;
                this.refresh()
            },
            sortChanged(sort) {
                this.filters.order = _g.sortFilter(sort);
                this.refresh()
            },
            refresh(){
                router.push({
                    path: this.$route.path,
                    query: _.merge({}, this.$route.query, {__query: JSON.stringify(this.filters)}, {random: Math.random()})
                })
            },
            query(){
                let query = this.$route.query.__query;
                if (query) {
                    this.filters = JSON.parse(query);
                }

                let queries = {
//                    name: this.filters.column.name,
                    page: this.filters.pagination.page,
                    limit: this.filters.pagination.limit,
                    order: this.filters.order
                };
                api.request('admin/system/config/search', queries, (res)=> {
                    this.configs = res.data.list;
                    this.filters.pagination.total = parseInt(res.data.count);
                })
            }
        },
        created() {
            this.query();
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

