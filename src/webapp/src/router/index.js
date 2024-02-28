import { createRouter, createWebHashHistory } from 'vue-router'
import { useMainStore } from '@/stores/main'

let polling = null;

const router = createRouter({
  // 使用hash形式，防止刷新页面请求服务器
  history: createWebHashHistory(),
  routes: [
    // 主页(聊天室)
    {
      path: '/',
      component: () => import("@/views/ChatView.vue"),
      meta: {
        requireLogin: true
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
    },
    // 修改个人信息界面
    {
      path: '/change',
      component: () => import('@/views/ChangeView.vue'),
      meta: {
        // requireLogin: true
      }
    }
  ]
})

// 页面切换前的验证与预处理
router.beforeEach((to) => {
  const store = useMainStore();

  // 关闭未关闭的websocket连接
  if (store.ws != null) {
    store.ws.close(3001);
  }

  // 用户未登录
  if (to.meta.requireLogin && !store.user.id && !store.user.password) {
    return '/login'
  }

  // 目标页面需要验证
  if (to.meta.requireLogin) {
    if (polling != null) {
      clearInterval(polling);
    }

    // 先尝试更新
    store.refresh().then((isFresh) => {
      if (!isFresh) {
        alert("用户认证信息过期或异常");
        router.push("/login");
      }
      // 每50秒更新一次
      polling = setInterval(() => {
        store.refresh().then((isFresh) => {
          console.log(isFresh);
          if (!isFresh) {
            alert("用户认证信息过期或异常");
            clearInterval(polling);
            polling = null;
            router.push("/login");
          }
        })
      }, 50 * 1000);
    })

  } else if (polling != null) {
    clearInterval(polling);
    polling = null;
  }
})

export default router
