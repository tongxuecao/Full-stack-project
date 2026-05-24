<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="always">
      <div class="auth-header">
        <h2>欢迎回来</h2>
        <p>登录校园二手交易平台</p>
      </div>

      <el-form :model="form" :rules="rules" ref="loginForm" label-width="0" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名/手机号" prefix-icon="User" clearable />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
        </el-form-item>

        <el-form-item prop="captcha">
          <div style="display: flex; gap: 10px; width: 100%;">
            <el-input v-model="form.captcha" placeholder="验证码" style="flex: 1;" @keyup.enter="handleLogin" />
            <canvas ref="captchaCanvas" width="120" height="40" @click="drawCaptcha" style="cursor: pointer; border-radius: 4px; border: 1px solid #dcdfe6;"></canvas>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%; font-weight: bold; font-size: 16px;" @click="handleLogin" :loading="loading">
            登 录
          </el-button>
        </el-form-item>

        <div class="auth-footer">
          <span>还没有账号？</span>
          <el-button type="primary" link @click="$router.push('/register')">立即注册</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import api from '@/api/axios'

const router = useRouter()
const route = useRoute()
const loginForm = ref(null)
const loading = ref(false)
const form = ref({ username: '', password: '', captcha: '' })
const validCaptcha = ref('')
const captchaCanvas = ref(null)

const resetForm = () => {
  form.value = { username: '', password: '', captcha: '' }
  loginForm.value?.resetFields()
  drawCaptcha()
}

watch(() => route.path, (path) => {
  if (path === '/login') resetForm()
})

// 表单校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 登录逻辑
const handleLogin = () => {
  loginForm.value.validate((valid) => {
    if (!valid) return
    if (form.value.captcha.toLowerCase() !== validCaptcha.value.toLowerCase()) {
      ElMessage.error('验证码错误')
      drawCaptcha(); form.value.captcha = ''; return
    }
    
    loading.value = true
    
    // 真实的登录请求
    api.post('/api/users/login', {
      username: form.value.username,
      password: form.value.password
    }).then(res => {
      loading.value = false
      // 如果后端查到了用户并返回了数据
      if (res.data && res.data.id) {
        // 把后端返回的真实用户数据存到浏览器里
        localStorage.setItem('user', JSON.stringify(res.data))
        ElMessage.success('登录成功！')
        const redirect = route.query.redirect || '/'
        router.push(redirect)
      } else {
        ElMessage.error('用户名或密码错误')
        drawCaptcha()
      }
    })
  })
}

// ===== Canvas 绘制图形验证码逻辑 =====
const drawCaptcha = () => {
  const canvas = captchaCanvas.value
  const ctx = canvas.getContext('2d')
  const width = canvas.width
  const height = canvas.height
  
  // 随机生成 4 位字母+数字
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789'
  let code = ''
  for (let i = 0; i < 4; i++) {
    code += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  validCaptcha.value = code // 存起来用于校验

  // 绘制背景
  ctx.fillStyle = '#f0f2f5'
  ctx.fillRect(0, 0, width, height)

  // 绘制文字
  ctx.font = 'bold 24px Arial'
  ctx.textBaseline = 'middle'
  for (let i = 0; i < code.length; i++) {
    ctx.fillStyle = `rgb(${Math.random()*100},${Math.random()*100},${Math.random()*100})`
    ctx.save()
    ctx.translate(20 * i + 20, height / 2)
    ctx.rotate((Math.random() - 0.5) * 0.5) // 随机倾斜
    ctx.fillText(code[i], 0, 0)
    ctx.restore()
  }

  // 绘制干扰线
  for (let i = 0; i < 5; i++) {
    ctx.strokeStyle = `rgb(${Math.random()*200},${Math.random()*200},${Math.random()*200})`
    ctx.beginPath()
    ctx.moveTo(Math.random() * width, Math.random() * height)
    ctx.lineTo(Math.random() * width, Math.random() * height)
    ctx.stroke()
  }
}

onMounted(() => {
  drawCaptcha() // 页面加载时生成第一个验证码
})
</script>

<style scoped>
/* 电商级沉浸式背景 */
.auth-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%); /* 非常有活力的渐变色 */
  margin: -20px -40px; /* 抵消 App.vue 里的 padding */
}

.auth-card {
  width: 90%;
  max-width: 400px;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}
@media (max-width: 480px) {
  .auth-page { margin: -12px; padding: 0 10px; }
  .auth-header h2 { font-size: 22px; }
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h2 {
  margin: 0 0 10px;
  color: #303133;
  font-size: 26px;
}

.auth-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>