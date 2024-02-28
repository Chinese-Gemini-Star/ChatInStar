import { createRouter, createWebHashHistory } from 'vue-router'
import { useMainStore } from '@/stores/main'

const router = createRouter({
  // 使用hash形式，防止刷新页面请求服务器
  history: createWebHashHistory(),
  routes: [
    // 主页(聊天室)
    {
      path: '/',
      component: () => import("@/views/ChatView.vue"),
      meta: {
        requiresLogin: true
      }
    },
    // 登录页面
    {
      path: '/login',
      component: () => import("@/views/LoginView.vue")
    },
    // 注册界面
    {
      path: '/register',
      component: () => import("@/views/RegisterView.vue")
    }
  ]
})

// 部分页面验证是否登录
router.beforeEach((to) => {
  const store = useMainStore();

  // 用户未登录
  if (to.meta.requiresLogin && !store.user.id && !store.user.password) {
    return '/login'
  }
})

export default router
