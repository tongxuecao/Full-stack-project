import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // ================= 1. 公共页面 (无需登录) =================
  { path: '/', name: 'Home', component: () => import('../views/public/Home.vue') },
  { path: '/detail/:id', name: 'Detail', component: () => import('../views/public/Detail.vue') },
  { path: '/search', name: 'Search', component: () => import('../views/public/Search.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/public/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/public/Register.vue') },
  { path: '/about', name: 'About', component: () => import('../views/public/About.vue') },

  // ================= 2. 私人页面 (需要登录) =================
  { 
    path: '/publish', 
    name: 'Publish', 
    component: () => import('../views/private/Publish.vue'),
    meta: { requiresAuth: true } // 打上需要登录的标记
  },
  { 
    path: '/user', 
    name: 'UserCenter', 
    component: () => import('../views/private/UserCenter.vue'),
    meta: { requiresAuth: true }, // 打上需要登录的标记
    children: [
      // 当访问 /user 时，默认重定向到我的发布
      { path: '', redirect: '/user/published' },
      // 子路由不需要加 /，访问路径为 /user/published
      { path: 'published', name: 'MyPublished', component: () => import('../views/private/MyPublished.vue') },
      { path: 'orders', name: 'MyOrders', component: () => import('../views/private/MyOrders.vue') },
      { path: 'favorites', name: 'MyFavorites', component: () => import('../views/private/MyFavorites.vue') }
    ]
  },

  // ================= 3. 系统/错误页面 =================
  { 
    path: '/:pathMatch(.*)*', 
    name: 'NotFound', 
    component: () => import('../views/system/NotFound.vue') 
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// ================= 全局路由守卫 =================
// 每次页面跳转前都会执行这个拦截器
router.beforeEach((to, from, next) => {
  // 1. 判断该路由是否需要登录权限 (检查上面配置的 meta.requiresAuth)
  if (to.meta.requiresAuth) {
    // 2. 去 LocalStorage 里看有没有用户信息
    const userStr = localStorage.getItem('user')
    if (userStr) {
      next() // 有信息，放行，允许进入私人页面
    } else {
      // 没有登录信息，强制跳转到登录页，并把想去的页面作为参数带上（方便登录后跳回来）
      next({ path: '/login', query: { redirect: to.fullPath } })
    }
  } else {
    // 不需要权限的公共页面，直接放行
    next() 
  }
})

export default router