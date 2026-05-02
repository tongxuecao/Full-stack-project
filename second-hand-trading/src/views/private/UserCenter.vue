<template>
  <div class="user-center-layout">
    <el-container style="height: calc(100vh - 100px); border: 1px solid #eee; border-radius: 8px;">
      <el-aside width="220px" style="background-color: #fff; border-right: 1px solid #eee;">
        <div class="user-profile-mini">
          <el-avatar :size="60" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <h3 v-if="currentUser">{{ currentUser.username }}</h3>
          <el-tag size="small" type="success" v-if="currentUser">学号：{{ currentUser.schoolId }}</el-tag>
        </div>

        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          @select="handleSelect"
          router
        >
          <el-menu-item index="/user/published">
            <el-icon><Sell /></el-icon>
            <span>我发布的商品</span>
          </el-menu-item>
          <el-menu-item index="/user/orders">
            <el-icon><ShoppingCart /></el-icon>
            <span>我买到的宝贝</span>
          </el-menu-item>
          <el-divider />
          <el-menu-item index="/about">
            <el-icon><QuestionFilled /></el-icon>
            <span>帮助与反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main style="background-color: #f9f9f9; padding: 25px;">
        <router-view v-if="currentUser" :currentUser="currentUser" />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Sell, ShoppingCart, QuestionFilled } from '@element-plus/icons-vue'

const route = useRoute()
const currentUser = ref(null)

const activeMenu = computed(() => route.path)

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  }
})
</script>

<style scoped>
.user-center-layout {
  max-width: 1200px;
  margin: 0 auto;
}
.user-profile-mini {
  text-align: center;
  padding: 30px 10px;
  border-bottom: 1px solid #f0f0f0;
}
.user-profile-mini h3 {
  margin: 10px 0 5px;
  color: #333;
}
.el-menu {
  border-right: none;
}
</style>