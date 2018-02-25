<template>
    <div>
        <el-row class="layout-page-heading">
            <el-col :span="16">
                <h3>新增配置</h3>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>信息管理</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/system/config/list' }">系统配置</el-breadcrumb-item>
                    <el-breadcrumb-item>新增配置</el-breadcrumb-item>
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
            <el-form label-width="80px" label-position="right" :model="config" :rules="rules" ref="config"
                     class="edit-form">
                <el-form-item label="参数名称" prop="name">
                    <el-input v-model="config.name" placeholder="参数名称" :maxlength="50"></el-input>
                </el-form-item>
                <el-form-item label="参数类型" prop="type">
                    <el-select v-model="config.type" placeholder="请选择参数类型">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="参数Key" prop="key">
                    <el-input v-model="config.key" placeholder="参数Key" :maxlength="50"></el-input>
                </el-form-item>
                <el-form-item label="参数值" prop="value">
                    <el-input v-model="config.value" placeholder="参数值" :maxlength="100"></el-input>
                </el-form-item>
                <el-form-item label="默认值">
                    <el-input v-model="config.def" placeholder="默认值" :maxlength="100"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input
                            type="textarea"
                            :rows="4"
                            placeholder="描述"
                            v-model="config.remark"
                            :maxlength="255">
                    </el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            let keyIsExist = (rules,value,callback) => {
                if(value){
                    var keyList = this.keyList;
                    for(var i=0;i<keyList.length;i++){
                        if(keyList[i] == value){
                            return callback(new Error('参数Key已存在'));
                        }
                    }
                }
                callback();
            };
            return {
                options: [],
                keyList:[],
                config: {
                    type: '',
                    key: '',
                    name: '',
                    def: '',
                    value: '',
                    remark:''
                },
                rules: {
                    name: [
                        {required: true, message: '请输入参数名称', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择参数类型'}
                    ],
                    key: [
                        {required: true, message: '请输入参数Key', trigger: 'blur'},
                        {validator:keyIsExist}
                    ],
                    value: [
                        {required: true, message: '请输入参数值', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            save(){
                let data = {
                    type:this.config.type,
                    key: this.config.key,
                    name: this.config.name,
                    def: this.config.def,
                    value: this.config.value,
                    remark:this.config.remark
                };
                this.$refs.config.validate((valid)=> {
                    if (valid) {
                        api.request('admin/system/config/create', data, (res)=> {
                            _g.toast('success', '保存成功');
                            router.go(-1);
                        })
                    }
                });

            },
            init(){
                api.request('admin/system/dict/getDictList', {type:'config_type'}, (res)=> {
                    this.options = res.data;
                });
                api.request('admin/system/config/keyList', {}, (res)=> {
                    this.keyList = res.data;
                });
            }
        },
        created() {
            this.init();
        }
    }
</script>

<style scoped>


</style>