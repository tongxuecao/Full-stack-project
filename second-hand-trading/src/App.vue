<template>
  <div class="app-wrapper">
    <header class="main-header">
      <div class="logo" @click="goTo('/')">校园二手平台</div>
      
      <div class="nav-links">
        <el-button text @click="goTo('/')">首页</el-button>
        <el-button text @click="goTo('/about')">防骗指南</el-button>
      </div>

      <div class="actions">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜搜看..." 
          @keyup.enter="handleSearch"
          style="width: 200px; margin-right: 20px;"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>

        <template v-if="isLoggedIn">
          <el-button type="success" plain @click="goTo('/user')">个人中心</el-button>
          <el-button type="primary" @click="goTo('/publish')">我要发布</el-button>
          <el-button type="danger" link @click="logout">退出</el-button>
        </template>
        <template v-else>
          <el-button @click="goTo('/login')">登录 / 注册</el-button>
        </template>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchKeyword = ref('')

// 使用计算属性动态判断是否登录，这样不用手动刷新状态
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('user')
})

const goTo = (path) => {
  router.push(path)
}

const handleSearch = () => {
  if (searchKeyword.value) {
    // 跳转到搜索结果页，并带上参数
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
    searchKeyword.value = ''
  }
  else {
    router.push('/') // 没有关键词也跳转到搜索页，显示空结果
  }
}

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login') // 退出后跳转到登录页
}
</script>

<style scoped>
.main-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.logo {
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}
.main-content {
  padding: 20px 40px;
}
</style>