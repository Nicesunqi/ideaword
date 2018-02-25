<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>添加字典</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/system/dict/list' }">字典管理</el-breadcrumb-item>
                    <el-breadcrumb-item>添加字典</el-breadcrumb-item>
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
            <el-form label-width="120px" label-position="right" :model="dict" :rules="rules" ref="dict"
                     class="edit-form">
                <el-form-item label="键值" prop="value">
                    <el-input v-model="dict.value" placeholder="键值" :maxlength="100">
                    </el-input>
                </el-form-item>
                <el-form-item label="标签" prop="label">
                    <el-input v-model="dict.label" placeholder="标签" :maxlength="100">
                    </el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-input v-model="dict.type" placeholder="类型" :maxlength="100">
                    </el-input>
                </el-form-item>
                <el-form-item label="描述" prop="remark">
                    <el-input v-model="dict.remark" placeholder="描述" :maxlength="255">
                    </el-input>
                </el-form-item>
                <el-form-item label="排序" class="is-required">
                    <el-input-number v-model="dict.sort" :min="1" :max="1000000000"></el-input-number>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                dict: {
                    value: '',
                    label: '',
                    type: '',
                    sort: '',
                    remark:''
                },
                rules: {
                    value: [
                        {required: true, message: '请输入键值',trigger: 'blur'}
                    ],
                    label: [
                        {required: true, message: '请输入标签', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请输入类型', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            save(){
                var data = {
                    value:this.dict.value,
                    label:this.dict.label,
                    type:this.dict.type,
                    sort:this.dict.sort,
                    remark:this.dict.remark
                }
                this.$refs.dict.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/dict/create', data, (res)=> {
                            _g.toast('success', '保存成功')
                            router.go(-1)
                        })
                    }
                })

            }/*,
            init(){
                api.request('admin/system/dictCategory/findAll', {}, (res)=> {
                    this.options = res.data
                })
            }*/
        },
        created() {
           /* this.init()*/
        }
    }
</script>

<style scoped>


</style>