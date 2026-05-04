import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import './style.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)
app.mount('#app')

console.log('Router ready:', router.currentRoute.value.path)

// 检查路由是否正常
router.isReady().then(() => {
  console.log('Route ready, current path:', router.currentRoute.value.path)
}).catch(err => {
  console.error('Route init error:', err)
})

const savedDark = localStorage.getItem('darkMode')
if (savedDark === 'true') {
  document.documentElement.classList.add('dark')
}
