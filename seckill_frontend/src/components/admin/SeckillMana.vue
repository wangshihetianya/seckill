<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{path:'home'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>管理界面</el-breadcrumb-item>
            <el-breadcrumb-item>秒杀管理</el-breadcrumb-item>
        </el-breadcrumb>
        
        <el-card>
            <!-- 搜索栏 -->
            <el-row :gutter="25">
                <el-col :span="10">
                <el-input placeholder="请输入搜索内容" v-model="queryInfo.query" clearable @clear="getActivity">
                    <el-button slot="append" icon="el-icon-search" @click="getActivity"></el-button>
                </el-input>
                </el-col>
                <el-col :span="4">
                <el-button type="primary" @click="addActivityDialogVisible = true">添加秒杀活动</el-button>
                </el-col>
            </el-row>
            <!-- 活动列表 -->
            <el-table :data="activityList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="活动名" prop="title"></el-table-column>
                <el-table-column label="开始时间" prop="startTime"></el-table-column>
                <el-table-column label="商品数目" prop="number"></el-table-column>
                <el-table-column label="状态" prop="status"></el-table-column>
                <el-table-column label="操作">
                    <!-- 操作插槽 -->
                    <template slot-scope="scope">
                        <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditActivityDialog(scope.row)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteActivity(scope.row.id)"></el-button>
                    </template>
                </el-table-column>
            </el-table>

             <!-- 分页 -->
            <div>
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum"
                    :page-sizes="[5, 10, 20]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
                </el-pagination>
            </div>
        </el-card>

        <!-- 新增活动表单 -->
        <el-dialog title="添加活动" :visible.sync="addActivityDialogVisible" width="50%" @close="addActivityDialogClose">
            <el-form :model="addActivityForm" :rules="activityFormRules" ref="addActivityFormRef" label-width="70px">
                <el-form-item label="活动名" prop="title">
                    <el-input v-model="addActivityForm.title" prefix-icon="el-icon-price-tag"></el-input>
                </el-form-item>
                <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker v-model="addActivityForm.startTime" type="datetime" placeholder="选择开始的日期和时间"
                    value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="抢购数目" prop="number">
                    <el-input v-model="addActivityForm.number" prefix-icon="el-icon-key"></el-input>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="addActivity">确定</el-button>
                    <el-button @click="addActivityDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
       
        <!-- 编辑活动表单 -->
        <el-dialog title="编辑活动" :visible.sync="editActivityDialogVisible" width="50%" @close="editActivityDialogClose">
            <el-form :model="editActivityForm" :rules="activityFormRules" ref="editActivityFormRef" label-width="70px">
                <el-form-item label="活动名" prop="title">
                    <el-input v-model="editActivityForm.title" prefix-icon="el-icon-price-tag"></el-input>
                </el-form-item>
                <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker v-model="editActivityForm.startTime" type="datetime" placeholder="选择开始的日期和时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="抢购数目" prop="number">
                    <el-input v-model="editActivityForm.number" prefix-icon="el-icon-key"></el-input>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="editActivity">确定</el-button>
                    <el-button @click="editActivityDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>


    </div>
</template>
<script>
export default {
    created() {
        this.getActivity();
    },
    data() {
        return {
            //查询对象
            queryInfo: {
                query: "",
                pageNum: 1,
                pageSize: 5,
            },

            activityList:[],
            total: 0,   //总记录数

            //活动对话框
            addActivityDialogVisible: false,
            addActivityForm: {
                title:'',
                startTime:'',
                number: 0
            },
            activityFormRules: {
                title: [
                    { required: true, message: '活动名不能为空', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                
            },

            //活动编辑框
            editActivityDialogVisible: false,
            editActivityForm: {
                id:0,
                title:'',
                startTime:'',
                number: ''
            },

        }
    },
    methods: {
        //获取活动
        getActivity() {
            this.$http.get('/activity',{params:this.queryInfo})
            .then(res => {
                console.log(res);
                //正常返回
                if (res.data.flag == "ok") {
                    this.activityList = res.data.activityList;
                    this.total = res.data.activityCount;
                } else {
                    this.$message.error("获取活动失败！！！")
                }
            })
        },
        //添加活动
        addActivity() {
            this.$refs.addActivityFormRef.validate(async valid => {
                if ( !valid ) {
                    return;
                }

                this.$http({
                    method:"post",
                    url: "http://localhost:9000/addActivity",
                    data: this.addActivityForm
                })
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("添加活动失败！");
                    } else {
                        this.$message.success("添加活动成功！");
                        this.addActivityDialogVisible = false;
                        this.getActivity();
                    }
                })
            })
        },
        //显示编辑活动框
        showEditActivityDialog(activity) {
            this.editActivityForm.id = activity.id;
            this.editActivityForm.title = activity.title;
            this.editActivityForm.startTime = activity.startTime;
            this.editActivityForm.number = activity.number;
            this.editActivityDialogVisible = true;
        },
        //编辑活动
        editActivity() {
            console.log(this.editActivityForm);
            this.$refs.editActivityFormRef.validate(async valid => {
                if ( !valid ) {
                    return;
                }

                this.$http({
                    method:"post",
                    url: "http://localhost:9000/editActivity",
                    data: this.editActivityForm
                })
                .then(res => {
                    console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("编辑活动失败！");
                    } else {
                        this.$message.success("编辑活动成功！");
                        this.editActivityDialogVisible = false;
                        this.getActivity();
                    }
                })
            })
        },
        //删除活动
        async deleteActivity(id) {
            const confirmResult = await this.$confirm('此操作将永久删除该活动，是否继续？','提示', {
                confirmButtonText:'确定',
                cancelButtonTest:'取消',
                type:'warning'
            }).catch(err => err)

            if (confirmResult != 'confirm') {
                return this.$message.info("已取消删除");
            }
            //确认的话发送删除请求
            this.$http.delete("deleteActivity?id="+id)
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("删除活动失败！");
                    } else {
                        this.$message.success("删除活动成功！");
                        this.getActivity();
                    }
                })
        },
        
        //页面显示数变化
        handleSizeChange(newSize) {
            this.queryInfo.pageSize = newSize;
            this.getActivity();
        },
        //页面变化
        handleCurrentChange(newPage) {
            this.queryInfo.pageNum = newPage;
            this.getActivity();
        },
        //关闭添加活动对话框
        addActivityDialogClose() {
            this.$refs.addActivityFormRef.resetFields();
        },
        //关闭编辑活动对话框
        editActivityDialogClose() {
            this.$refs.editActivityFormRef.resetFields();
        }
    },
}
</script>
<style lang="less" scoped>
.el-breadcrumb{
    margin-bottom: 15px;
    font-size: 17px;
}
.el-card{
    margin-top: 20px;
    .el-table{
        margin: 20px 0px;
    }
}
</style>