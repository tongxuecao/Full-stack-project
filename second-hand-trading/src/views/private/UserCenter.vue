<template>
  <div class="user-center-layout">
    <el-container class="center-container">
      <el-aside class="center-aside">
        <div class="user-profile-mini">
          <div class="avatar-wrapper" @click="triggerAvatarUpload">
            <el-avatar :size="60" :src="currentUser?.avatar">
              <span style="font-size: 24px;">{{ avatarDefaultText }}</span>
            </el-avatar>
            <div class="avatar-overlay">更换</div>
          </div>
          <input
            ref="avatarInputRef"
            type="file"
            accept="image/*"
            style="display: none"
            @change="handleAvatarChange"
          />
          <h3 class="username-link" @click="showNameDialog = true">{{ currentUser?.username }}</h3>
          <el-tag size="small" type="success" v-if="currentUser">学号：{{ currentUser.schoolId }}</el-tag>
          <div class="profile-actions">
            <el-button size="small" text type="primary" @click="showNameDialog = true">修改名称</el-button>
            <el-button size="small" text type="warning" @click="showPasswordDialog = true">修改密码</el-button>
          </div>
        </div>

        <el-menu
          :default-active="activeMenu"
          class="center-menu"
          @select="handleSelect"
          router
        >
          <el-menu-item index="/user/published" class="menu-item-custom">
            <el-icon><Sell /></el-icon>
            <span>我发布的商品</span>
          </el-menu-item>
          <el-menu-item index="/user/orders" class="menu-item-custom">
            <el-icon><ShoppingCart /></el-icon>
            <span>我买到的宝贝</span>
          </el-menu-item>
          <el-menu-item index="/user/favorites" class="menu-item-custom">
            <el-icon><Star /></el-icon>
            <span>我的收藏</span>
          </el-menu-item>
          <el-divider style="margin: 8px 0;" />
          <el-menu-item index="/about" class="menu-item-custom">
            <el-icon><QuestionFilled /></el-icon>
            <span>帮助与反馈</span>
          </el-menu-item>
        </el-menu>

        <div style="flex: 1;"></div>
        <div class="logout-section">
          <el-button type="danger" plain style="width: 100%;" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 退出登录
          </el-button>
        </div>
      </el-aside>

      <el-main class="center-main">
        <router-view v-if="currentUser" v-slot="{ Component, route: r }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" :key="r.fullPath" :currentUser="currentUser" />
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <!-- 修改名称对话框 -->
    <el-dialog v-model="showNameDialog" title="修改名称" width="400px" :close-on-click-modal="false">
      <el-input v-model="newName" placeholder="请输入新名称" maxlength="20" show-word-limit />
      <template #footer>
        <el-button @click="showNameDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateName" :loading="savingName">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px" :close-on-click-modal="false">
      <el-form :model="passwordForm" label-width="80px">
        <el-form-item label="旧密码">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdatePassword" :loading="savingPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Sell, ShoppingCart, QuestionFilled, Star, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const currentUser = ref(null)

const activeMenu = computed(() => route.path)
const avatarDefaultText = computed(() => {
  return currentUser.value?.username?.charAt(0)?.toUpperCase() || 'U'
})

const avatarInputRef = ref(null)
const showNameDialog = ref(false)
const showPasswordDialog = ref(false)
const newName = ref('')
const savingName = ref(false)
const savingPassword = ref(false)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

onMounted(() => {
  loadUser()
})

const loadUser = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  }
}

const refreshUser = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/users/refresh/${currentUser.value.id}`)
    if (res.data) {
      localStorage.setItem('user', JSON.stringify(res.data))
      currentUser.value = res.data
    }
  } catch (e) {
    console.error('Failed to refresh user info')
  }
}

const triggerAvatarUpload = () => {
  avatarInputRef.value?.click()
}

const handleAvatarChange = async (e) => {
  const file = e.target.files?.[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  try {
    const uploadRes = await axios.post('http://localhost:8080/api/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    const avatarUrl = uploadRes.data
    if (avatarUrl === 'error') {
      ElMessage.error('头像上传失败')
      return
    }

    const updateRes = await axios.put('http://localhost:8080/api/users/avatar', {
      id: currentUser.value.id,
      avatarUrl
    })
    if (updateRes.data === 'success') {
      await refreshUser()
      ElMessage.success('头像更新成功')
    } else {
      ElMessage.error('头像保存失败')
    }
  } catch (e) {
    ElMessage.error('头像上传失败')
  } finally {
    avatarInputRef.value.value = ''
  }
}

const handleUpdateName = async () => {
  const name = newName.value.trim()
  if (!name) {
    ElMessage.warning('名称不能为空')
    return
  }
  savingName.value = true
  try {
    const res = await axios.put('http://localhost:8080/api/users/username', {
      id: currentUser.value.id,
      username: name
    })
    if (res.data === 'success') {
      ElMessage.success('名称修改成功')
      showNameDialog.value = false
      newName.value = ''
      await refreshUser()
    } else if (res.data === 'exists') {
      ElMessage.warning('该名称已被占用')
    } else {
      ElMessage.error('修改失败')
    }
  } catch (e) {
    ElMessage.error('修改失败')
  } finally {
    savingName.value = false
  }
}

const handleUpdatePassword = async () => {
  const { oldPassword, newPassword, confirmPassword } = passwordForm.value
  if (!oldPassword || !newPassword || !confirmPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (newPassword !== confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }
  if (newPassword === oldPassword) {
    ElMessage.warning('新密码不能与旧密码相同')
    return
  }
  savingPassword.value = true
  try {
    const res = await axios.put('http://localhost:8080/api/users/password', {
      id: currentUser.value.id,
      oldPassword,
      newPassword
    })
    if (res.data === 'success') {
      ElMessage.success('密码修改成功，请重新登录')
      showPasswordDialog.value = false
      passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
      localStorage.removeItem('user')
      router.push('/login')
    } else if (res.data === 'wrong_password') {
      ElMessage.error('旧密码不正确')
    } else {
      ElMessage.error('修改失败')
    }
  } catch (e) {
    ElMessage.error('修改失败')
  } finally {
    savingPassword.value = false
  }
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('user')
    router.push('/login')
    ElMessage.success('已退出登录')
  }).catch(() => {})
}
</script>

<style scoped>
.user-center-layout {
  max-width: 1200px;
  margin: 0 auto;
}

.center-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.06);
  border: 1px solid var(--el-border-color-lighter);
  background: var(--el-bg-color);
  transition: background 0.3s, border-color 0.3s;
}

.center-aside {
  width: 230px !important;
  background: var(--el-bg-color);
  border-right: 1px solid var(--el-border-color-lighter);
  display: flex;
  flex-direction: column;
  transition: background 0.3s, border-color 0.3s;
}

.user-profile-mini {
  text-align: center;
  padding: 28px 10px 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  background: var(--el-bg-color);
  transition: background 0.3s, border-color 0.3s;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.user-profile-mini h3 {
  margin: 10px 0 5px;
  color: var(--el-text-color-primary);
  font-size: 16px;
}

.username-link {
  cursor: pointer;
  transition: color 0.2s;
}

.username-link:hover {
  color: #409eff;
}

.profile-actions {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  gap: 0;
}

.center-menu {
  border-right: none;
  background: transparent;
  padding: 8px 0;
}

.center-menu .menu-item-custom {
  margin: 2px 10px;
  border-radius: 8px;
  transition: all 0.2s;
}

.center-menu .menu-item-custom:hover {
  background: #ecf5ff;
}

.center-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, #ecf5ff 0%, #d9ecff 100%);
  color: #409eff;
  font-weight: 600;
  border-radius: 8px;
}

.logout-section {
  padding: 14px 16px;
  border-top: 1px solid var(--el-border-color-lighter);
  background: var(--el-bg-color);
  transition: background 0.3s, border-color 0.3s;
}

.center-main {
  background: var(--el-fill-color-lighter);
  padding: 28px;
  min-height: calc(100vh - 160px);
  transition: background 0.3s;
}

/* 路由切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.25s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(12px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-12px);
}
</style>
