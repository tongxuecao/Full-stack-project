<template>
  <div class="app-wrapper">
    <header class="main-header">
      <div class="header-inner">
        <div class="logo" @click="goTo('/')">
          <el-icon :size="24" color="#409eff"><Shop /></el-icon>
          <span class="logo-text">校园二手平台</span>
        </div>

        <div class="nav-links">
          <el-button
            text
            :class="{ active: route.path === '/' }"
            @click="goTo('/')"
          >首页</el-button>
          <el-button
            text
            :class="{ active: route.path === '/about' }"
            @click="goTo('/about')"
          >防骗指南</el-button>
        </div>

        <div class="search-area">
          <el-input
            v-model="searchKeyword"
            placeholder="搜搜看..."
            size="large"
            @keyup.enter="handleSearch"
            class="search-input"
          >
            <template #suffix>
              <el-icon class="search-icon" @click="handleSearch"><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <div class="actions">
          <template v-if="isLoggedIn">
            <div class="user-info" @click="goTo('/user')">
              <el-avatar :size="34" :src="currentUser?.avatar">
                {{ currentUser?.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ currentUser?.username }}</span>
            </div>
            <el-button type="primary" round @click="goTo('/publish')">
              <el-icon><Plus /></el-icon> 我要发布
            </el-button>
          </template>
          <template v-else>
            <el-button type="primary" round @click="goTo('/login')">登录 / 注册</el-button>
          </template>

          <div class="theme-toggle" @click="toggleDark" :title="isDark ? '切换亮色模式' : '切换暗色模式'">
            <transition name="theme-icon" mode="out-in">
              <el-icon :size="20" :key="isDark ? 'moon' : 'sunny'">
                <Moon v-if="isDark" />
                <Sunny v-else />
              </el-icon>
            </transition>
          </div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Shop, Search, Plus, Moon, Sunny } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const searchKeyword = ref('')

const isLoggedIn = ref(false)
const currentUser = ref(null)

const refreshUser = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
    isLoggedIn.value = true
  } else {
    currentUser.value = null
    isLoggedIn.value = false
  }
}
refreshUser()

watch(() => route.fullPath, () => refreshUser())

const isDark = ref(document.documentElement.classList.contains('dark'))

const toggleDark = () => {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('darkMode', isDark.value)
}

const goTo = (path) => {
  router.push(path)
}

const handleSearch = () => {
  if (searchKeyword.value) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
    searchKeyword.value = ''
  } else {
    router.push('/')
  }
}
</script>

<style scoped>
.main-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-lighter);
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
  height: 60px;
  transition: background 0.3s, border-color 0.3s;
}

.header-inner {
  max-width: 1400px;
  height: 100%;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-shrink: 0;
  margin-right: 32px;
  user-select: none;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  letter-spacing: 1px;
  transition: color 0.3s;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
}

.nav-links :deep(.el-button) {
  font-size: 15px;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.2s;
}

.nav-links :deep(.el-button:hover) {
  color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.nav-links .el-button.active {
  color: var(--el-color-primary) !important;
  font-weight: 600;
  background: var(--el-color-primary-light-9) !important;
}

.search-area {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 0 40px;
}

.search-input {
  max-width: 420px;
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  background: var(--el-fill-color-light);
  border: 1px solid transparent;
  transition: all 0.3s;
}

.search-input :deep(.el-input__wrapper:hover) {
  background: var(--el-fill-color);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  background: var(--el-bg-color);
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 1px var(--el-color-primary) inset;
}

.search-icon {
  cursor: pointer;
  color: var(--el-text-color-secondary);
  font-size: 18px;
  transition: color 0.2s;
}

.search-icon:hover {
  color: var(--el-color-primary);
}

.actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 10px 4px 4px;
  border-radius: 20px;
  transition: background 0.2s;
}

.user-info:hover {
  background: var(--el-fill-color-light);
}

.user-info .username {
  font-size: 14px;
  color: var(--el-text-color-primary);
  font-weight: 500;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.theme-toggle {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--el-text-color-regular);
  background: transparent;
  transition: all 0.3s;
  flex-shrink: 0;
}

.theme-toggle:hover {
  background: var(--el-fill-color-light);
  color: var(--el-color-warning);
}

.theme-icon-enter-active,
.theme-icon-leave-active {
  transition: all 0.2s ease;
}

.theme-icon-enter-from {
  opacity: 0;
  transform: rotate(-90deg) scale(0.6);
}

.theme-icon-leave-to {
  opacity: 0;
  transform: rotate(90deg) scale(0.6);
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 32px;
}
</style>
