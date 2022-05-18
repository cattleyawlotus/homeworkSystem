import Vue from 'vue'
import VueRouter from 'vue-router'
import Blog from '../views/user/Blog'

const Login = () => import(/* webpackChunkName: "Login_NotFound" */ '../views/Login.vue')
const NotFound = () => import(/* webpackChunkName: "Login_NotFound" */ '../views/NotFound.vue')

const Home = () => import(/* webpackChunkName: "Home_Types_Tag_Crawler" */ '../views/Home.vue')
const Types = () => import(/* webpackChunkName: "Home_Types_Tag_Crawler" */ '../views/type/Types.vue')
const Tags = () => import(/* webpackChunkName: "Home_Types_Tag_Crawler" */ '../views/type/Tags.vue')

const PostBlogs = () => import(/* webpackChunkName: "User" */ '../views/user/PostBlogs.vue')
const Archives = () => import(/* webpackChunkName: "User" */ '../views/user/Archives.vue')
const Collects = () => import(/* webpackChunkName: "User" */ '../views/user/Collects.vue')
const Info = () => import(/* webpackChunkName: "User" */ '../views/user/Info.vue')
const MyInfo = () => import(/* webpackChunkName: "User" */ '../components/info/MyInfo.vue')
const Notify = () => import(/* webpackChunkName: "User" */ '../components/info/Notify.vue')

const Message = () => import(/* webpackChunkName: "Backyard" */ '../views/backyard/Message.vue')

const Welcome = () => import(/* webpackChunkName: "BW" */ '../views/admin/Welcome')
const AdminHome = () => import(/* webpackChunkName: "BW" */ '../components/layout/AdminHome')

const Blogs = () => import(/* webpackChunkName: "admin" */ '../views/admin/blogs/Blogs.vue')
const Types2 = () => import(/* webpackChunkName: "admin" */ '../views/admin/blogs/Types2.vue')
const Tags2 = () => import(/* webpackChunkName: "admin" */ '../views/admin/blogs/Tags2.vue')
const Comments = () => import(/* webpackChunkName: "admin" */ '../views/admin/blogs/Comments')

const Users = () => import(/* webpackChunkName: "Admin_Users" */ '../views/admin/users/Users')

const Message2 = () => import(/* webpackChunkName: "Admin_Users" */ '../views/admin/message/message2')

const Data = () => import(/* webpackChunkName: "dataWatch" */ '../views/admin/dataWatch/data')

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: 'home' },
  { path: '/login', component: Login },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/Blog',
    name: 'Blog',
    component: Blog
  },
  {
    path: '/Types',
    name: 'Types',
    component: Types
  },
  {
    path: '/Tags',
    name: 'Tags',
    component: Tags
  },
  {
    path: '/Archives',
    name: 'Archives',
    component: Archives
  },
  {
    path: '/collects',
    name: 'Collects',
    component: Collects
  },
  {
    path: '/Message',
    name: 'message',
    component: Message
  },
  {
    path: '/AdminHome',
    component: AdminHome,
    redirect: '/Welcome',
    children: [{ path: '/Welcome', component: Welcome },
      { path: '/users', component: Users },
      { path: '/comments', component: Comments },
      { path: '/message2', component: Message2 },
      { path: '/Blogs', component: Blogs },
      { path: '/tags2', component: Tags2 },
      { path: '/types2', component: Types2 },
      { path: '/data', component: Data }
    ]
  },
  {
    path: '/PostBlogs',
    name: 'PostBlogs',
    component: PostBlogs
  },
  {
    path: '/info',
    name: 'Info',
    component: Info,
    redirect: '/myinfo',
    children: [
      { path: '/myinfo', component: MyInfo },
      { path: '/notify', component: Notify }
    ]
  },
  {
    path: '*',
    component: NotFound
  }
]
const router = new VueRouter({
  mode: 'history',
  routes
})

const userpath = ['/archives', '/collects', '/info', '/myinfo', '/notify', '/Postblogs']
const adminpath = ['/AdminHome', '/Welcome', '/users', '/comments', '/message2', '/Blogs', '/tags2', '/types2', '/data']
// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 将要访问的路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数，表示放行
  //     next()  放行    next('/login')  强制跳转

  // if (to.path === '/login') return next()
  // // 获取token
  // const tokenStr = window.sessionStorage.getItem('token')
  // // 后端指定接口验证了token的正确性
  // if (!tokenStr && (to.path === '/blogs')) return next('/login')
  // next()
  if (userpath.indexOf(to.path) !== -1 || adminpath.indexOf(to.path) !== -1) {
    if (adminpath.indexOf(to.path) !== -1 && window.sessionStorage.getItem('role') === 'admin') {
      next()
    } else if (userpath.indexOf(to.path) !== -1 && (window.sessionStorage.getItem('role') === 'student' || window.sessionStorage.getItem('role') === 'teacher')) {
      next()
    } else {
      next('/login')
    }
  } else {
    next()
  }
})
export default router
