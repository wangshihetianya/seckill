<template>
    <div class="login_container">
        <div class="login_box">
            <!-- 头像 -->
            <div class="avatar_box">
                <img src="../assets/logo.webp" alt="">
            </div>
            <!-- 表单 -->
            <el-form ref="loginFormRef" :rules="loginRules" :model="loginForm" class="login_form" label-width="0">
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" prefix-icon="el-icon-key" type="password"></el-input>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="login()">提交</el-button>
                    <el-button type="info" @click="resetForm()">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loginForm: {
                username: "",
                password: ""
            },
            //表单验证规则
            loginRules: {
                username: [
                    { required: true, message: '账号不能为空', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        //表单重置方法
        resetForm() {
            this.$refs.loginFormRef.resetFields();
        },
        //登录处理
        login() {
            this.$refs.loginFormRef.validate(async valid => {
                if ( !valid ) {
                    return;
                }

                this.$http({
                    method:"post",
                    url: "http://localhost:9000/login",
                    data: this.loginForm
                })
                .then(res => {
                    console.log(res);
                    if (res.data.flag == "ok") {
                        this.$message.success("登录成功！");
                        //存储token和角色
                        //TODO:这里不应该存用户id,因为token已经包含了，但先存后面再改
                        window.localStorage.setItem('token', res.data.token);
                        window.sessionStorage.setItem('userid', res.data.userid);
                        window.sessionStorage.setItem('role', res.data.role);
                        //
                        //跳转页面
                        this.$router.push({
                            path:"/home",
                            query:{
                                role: res.data.role
                            }
                        });
                    } else {
                        this.$message.error("登录失败！");
                    }
                })


            })
        },
    },
}
</script>

<style lang="less" scoped>
.login_container{
    background-color: #2b4b6b;
    height: 100%;
}
.login_box{
    width: 450px;
    height: 300px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    .avatar_box{
        width: 130;
        height: 130;
        border: 1px solid #eee;
        border-radius: 50%;
        padding: 5px;
        box-shadow: 0 0 2px #ddd;
        position: absolute;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: #0ee;
        img{
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background-color: #eee;
        }
    }
}
.btns{
    display: flex;
    justify-content: flex-end;
}
.login_form{
    position: absolute;
    bottom: 0%;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
}

</style>