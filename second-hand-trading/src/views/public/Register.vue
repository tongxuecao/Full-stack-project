<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="always">
      <div class="auth-header">
        <h2>注册新账号</h2>
        <p>加入校园二手交易平台</p>
      </div>

      <el-form :model="form" :rules="rules" ref="registerForm" label-width="0" size="large">
        
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="设置用户名 (3-15位字符)" prefix-icon="User" clearable />
        </el-form-item>

        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="输入11位手机号" prefix-icon="Iphone" clearable />
        </el-form-item>

        <el-form-item prop="schoolId">
        <el-input 
            v-model="form.schoolId" 
            placeholder="请输入学号 (用于校园身份核验)" 
            prefix-icon="Postcard" 
            clearable 
        />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="设置密码 (至少6位)" prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%; font-weight: bold; font-size: 16px;" @click="handleRegister" :loading="loading">
            立即注册
          </el-button>
        </el-form-item>

        <div class="auth-footer">
          <span>已有账号？</span>
          <el-button type="primary" link @click="$router.push('/login')">返回登录</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Iphone } from '@element-plus/icons-vue'
import axios from 'axios' // 记得顶部引入

const router = useRouter()
const route = useRoute()
const registerForm = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  phone: '',
  schoolId: '',
  password: '',
  confirmPassword: ''
})

const resetForm = () => {
  form.value = { username: '', phone: '', schoolId: '', password: '', confirmPassword: '' }
  nextTick(() => registerForm.value?.resetFields())
}

watch(() => route.path, (path) => {
  if (path === '/register') resetForm()
})

// 自定义校验逻辑：两次密码是否一致
const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.value.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

// 严格的表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    // 真实的中国大陆手机号正则
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  schoolId: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { pattern: /^\d{8}$/, message: '学号格式不正确 (通常为8位数字)', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码不能少于 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
}



const handleRegister = () => {
  registerForm.value.validate((valid) => {
    if (!valid) return
    loading.value = true
    
    // 真实的注册请求
    axios.post('http://localhost:8080/api/users/register', form.value)
      .then(res => {
        loading.value = false
        if (res.data === 'success') {
          ElMessage.success('注册成功，请登录！')
          router.push('/login')
        } else if (res.data === 'username_exists') {
          ElMessage.warning('抱歉，该用户名已被使用')
        } else {
          ElMessage.error('注册失败，请稍后重试')
        }
      })
  })
}
</script>

<style scoped>
/* 复用登录页的样式 */
.auth-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%);
  margin: -20px -40px; 
}
.auth-card { width: 400px; border-radius: 12px; box-shadow: 0 8px 30px rgba(0,0,0,0.1); }
.auth-header { text-align: center; margin-bottom: 30px; }
.auth-header h2 { margin: 0 0 10px; color: #303133; font-size: 26px; }
.auth-header p { margin: 0; color: #909399; font-size: 14px; }
.auth-footer { text-align: center; margin-top: 10px; font-size: 14px; color: #606266; }
</style>