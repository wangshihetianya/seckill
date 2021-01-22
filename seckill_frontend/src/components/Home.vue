<template>
  <el-container class="home_container">
    <!-- 头部 -->
    <el-header>
      <div>
        <img src="../assets/home.jpg" alt="" />
        <span>秒杀系统</span>
      </div>
      <el-button type="info" @click="logout()">安全退出</el-button>
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" 
        :router="true" :default-active="activePath">
          <!-- 一级菜单 -->
          <el-submenu :index="item.id+''" v-for="item in menuList" :key="item.id">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>{{item.title}}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item :index="it.path" v-for="it in item.subMenuList" :key="it.id" @click="saveNavPath(it.path)">
              {{it.title}}
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <!-- 主体 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      //角色
      role: window.sessionStorage.getItem('role'),
      //菜单列表
      menuList:[],
      //默认访问组件路径
      activePath:"/welcome"
    };
  },
  //onload事件
  created() {
    this.getMenuList();
    //取出此次对话中保存的路径
    this.activePath = window.sessionStorage.getItem('activePath');
  },
  methods: {
    //安全退出
    logout() {
      window.localStorage.clear();
      window.sessionStorage.clear();
      this.$router.push("/login");
    },
    //获取菜单
    getMenuList() {
      console.log();
      this.$http.get('/menu',{
        params:{
          role: this.role,
        }
      }).then(res => {
        console.log(res);
        //正常返回
        if (res.data.flag == "ok") {
          this.menuList = res.data.menuList;
        } else {
          this.$message.error("获取菜单失败！！！")
        }
      })
    },
    //保存点击的路径
    saveNavPath(activePath) {
      window.sessionStorage.setItem('activePath', activePath);
      this.activePath = activePath;
    }
  },
};
</script>

<style lang="less" scoped>
.home_container {
  height: 100%;
}

.el-header {
  background-color: #373d41;
  display: flex;
  justify-items: center;
  justify-content: space-between;
  padding-left: 0%;

  align-items: center;

  color: #fff;
  font-size: 20px;

  div {
    display: flex;
    align-items: center;
    img {
      height: 55px;
      width: 55px;
    }
    span {
      margin-left: 15px;
    }
  }
}

.el-aside {
  background-color: #333744;
  .el-menu {
    border-right: 0px; //删除凸出来的右边框
  }
}

.el-main {
  background-color: #eaedf1;
}
</style>