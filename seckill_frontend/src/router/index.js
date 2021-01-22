import Vue from 'vue'
import VueRouter from 'vue-router'
//引入组件
import Login from '../components/Login.vue'
import Home from '../components/Home.vue'
import Welcome from '../components/Welcome.vue'
import UserList from '../components/admin/UserList.vue'
import SeckillMana from '../components/admin/SeckillMana.vue'
import Seckill from '../components/seckill/Seckill.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
    redirect: '/welcome',
    children:[
      {path: '/welcome', component: Welcome},
      {path: '/user', component: UserList},
      {path: '/seckillmana', component: SeckillMana},
      {path: '/seckill', component: Seckill},
    ]
  }

]

const router = new VueRouter({
  mode: 'history',
  routes
})

//挂载路由导航
router.beforeEach((to, from, next) => {
  //to将要访问
  //from从哪访问
  //next继续访问的路径
  if (to.path != '/login') {
    //对于非登录页面，只有token存在才能继续访问
    if (localStorage.getItem('token')) {
        next();
    } else { //token不存在
        next('/login');
    }
  } else {
      next();
  }
  
  
})

export default router
