<template>
  <div class="not-found-page">
    <div class="nf-content">
      <div class="nf-code">404</div>
      <h1>页面不见了</h1>
      <p>你访问的页面不存在，可能已被移除或链接地址有误</p>
      <div class="nf-actions">
        <el-button type="primary" size="large" round @click="goHome">
          <el-icon><HomeFilled /></el-icon> 返回首页
        </el-button>
        <el-button size="large" round @click="goBack">
          <el-icon><ArrowLeft /></el-icon> 返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { HomeFilled, ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.not-found-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 160px);
  padding: 40px 20px;
}

.nf-content {
  text-align: center;
  max-width: 480px;
}

.nf-code {
  font-size: 120px;
  font-weight: 800;
  line-height: 1;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 16px;
}

.nf-content h1 {
  font-size: 24px;
  color: var(--el-text-color-primary);
  margin: 0 0 12px;
}

.nf-content p {
  font-size: 15px;
  color: var(--el-text-color-secondary);
  line-height: 1.6;
  margin: 0 0 32px;
}

.nf-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
</style>
