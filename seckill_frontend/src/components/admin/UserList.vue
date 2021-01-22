<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{path:'home'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>管理界面</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
        
        <el-card>
            <!-- 搜索栏 -->
            <el-row :gutter="25">
                <el-col :span="10">
                <el-input placeholder="请输入搜索内容" v-model="queryInfo.query" clearable @clear="getUser">
                    <el-button slot="append" icon="el-icon-search" @click="getUser"></el-button>
                </el-input>
                </el-col>
                <el-col :span="4">
                <el-button type="primary" @click="addUserDialogVisible = true">添加用户</el-button>
                </el-col>
            </el-row>
            <!-- 用户列表 -->
            <el-table :data="userList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="用户名" prop="username"></el-table-column>
                <el-table-column label="密码" prop="password"></el-table-column>
                <el-table-column label="角色" prop="role"></el-table-column>
                <el-table-column label="操作">
                    <!-- 操作插槽 -->
                    <template slot-scope="scope">
                        <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditUserDialog(scope.row)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUser(scope.row.id)"></el-button>
                        <!-- <el-tooltip effect="dark" content="分配权限" placement="top-start" :enterable="false">
                            <el-button type="warning" icon="el-icon-setting" size="mini"></el-button>
                        </el-tooltip> -->
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

        <!-- 新增用户表单 -->
        <el-dialog title="添加用户" :visible.sync="addUserDialogVisible" width="50%" @close="addUserDialogClose">
            <el-form :model="addUserForm" :rules="userFormRules" ref="addUserFormRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="addUserForm.username" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="addUserForm.password" prefix-icon="el-icon-key" type="password"></el-input>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="addUser">确定</el-button>
                    <el-button @click="addUserDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
       
        <!-- 编辑用户表单 -->
        <el-dialog title="编辑用户" :visible.sync="editUserDialogVisible" width="50%" @close="editUserDialogClose">
            <el-form :model="editUserForm" :rules="userFormRules" ref="editUserFormRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="editUserForm.username" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="editUserForm.password" prefix-icon="el-icon-key"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="role">
                    <el-select v-model="editUserForm.role" placeholder="请选择角色">
                        <el-option
                        v-for="item in roleOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="editUser">确定</el-button>
                    <el-button @click="editUserDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>


    </div>
</template>
<script>
export default {
    created() {
        this.getUser();
    },
    data() {
        return {
            //查询对象
            queryInfo: {
                query: "",
                pageNum: 1,
                pageSize: 5,
            },

            userList:[],
            total: 0,   //总记录数

            //用户对话框
            addUserDialogVisible: false,
            addUserForm: {
                username:'',
                password:''
            },
            userFormRules: {
                username: [
                    { required: true, message: '账号不能为空', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ]
            },

            //用户编辑框
            editUserDialogVisible: false,
            editUserForm: {
                id:0,
                username:'',
                password:'',
                role: ''
            },
            //角色，暂时先定死2个，本来应该是要由后端发送的消息来确定的
            roleOptions:[{
                value:'user',
                label:'普通用户'
            },{
                value:'admin',
                label:'管理员'
            }]

        }
    },
    methods: {
        //获取用户
        getUser() {
            this.$http.get('/user',{params:this.queryInfo})
            .then(res => {
                // console.log(res);
                //正常返回
                if (res.data.flag == "ok") {
                    this.userList = res.data.userList;
                    this.total = res.data.userCounts;
                } else {
                    this.$message.error("获取用户失败！！！")
                }
            })
        },
        //添加用户
        addUser() {
            this.$refs.addUserFormRef.validate(async valid => {
                if ( !valid ) {
                    return;
                }

                this.$http({
                    method:"post",
                    url: "http://localhost:9000/addUser",
                    data: this.addUserForm
                })
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("添加用户失败！");
                    } else {
                        this.$message.success("添加用户成功！");
                        this.addUserDialogVisible = false;
                        this.getUser();
                    }
                })
            })
        },
        //显示编辑用户框
        showEditUserDialog(user) {
            this.editUserForm.id = user.id;
            this.editUserForm.username = user.username;
            this.editUserForm.password = user.password;
            this.editUserForm.role = user.role;
            this.editUserDialogVisible = true;
        },
        //编辑用户
        editUser() {
            console.log(this.editUserForm);
            this.$refs.editUserFormRef.validate(async valid => {
                if ( !valid ) {
                    return;
                }

                this.$http({
                    method:"post",
                    url: "http://localhost:9000/editUser",
                    data: this.editUserForm
                })
                .then(res => {
                    console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("编辑用户失败！");
                    } else {
                        this.$message.success("编辑用户成功！");
                        this.editUserDialogVisible = false;
                        this.getUser();
                    }
                })
            })
        },
        //删除用户
        async deleteUser(id) {
            const confirmResult = await this.$confirm('此操作将永久删除用户，是否继续？','提示', {
                confirmButtonText:'确定',
                cancelButtonTest:'取消',
                type:'warning'
            }).catch(err => err)

            if (confirmResult != 'confirm') {
                return this.$message.info("已取消删除");
            }
            //确认的话发送删除请求
            this.$http.delete("deleteUser?id="+id)
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("删除用户失败！");
                    } else {
                        this.$message.success("删除用户成功！");
                        this.getUser();
                    }
                })
        },
        
        //页面显示数变化
        handleSizeChange(newSize) {
            this.queryInfo.pageSize = newSize;
            this.getUser();
        },
        //页面变化
        handleCurrentChange(newPage) {
            this.queryInfo.pageNum = newPage;
            this.getUser();
        },
        //关闭添加用户对话框
        addUserDialogClose() {
            this.$refs.addUserFormRef.resetFields();
        },
        //关闭编辑用户对话框
        editUserDialogClose() {
            this.$refs.editUserFormRef.resetFields();
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