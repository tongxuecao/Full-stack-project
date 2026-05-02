<template>
  <div class="publish-container">
    <el-card class="publish-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>发布闲置宝贝</h2>
          <span style="color: #909399; font-size: 14px;">给你的旧物找个新主人吧！</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="publishForm" label-width="100px" size="large">
        
        <el-form-item label="商品标题" prop="title">
          <el-input 
            v-model="form.title" 
            placeholder="请输入标题，例如：自用九成新罗技机械键盘" 
            clearable 
            maxlength="30"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="商品图片" prop="images">
          <el-upload
            action="http://localhost:8080/api/files/upload"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            multiple
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div style="font-size: 12px; color: #999; margin-top: 5px; width: 100%;">
            最多上传 5 张图片，单张不超过 2MB
          </div>
        </el-form-item>

        <el-form-item label="商品分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择最符合的分类" style="width: 100%;">
                <el-option label="数码3C" value="数码3C" />
                <el-option label="书籍资料" value="书籍资料" />
                <el-option label="生活用品" value="生活用品" />
                <el-option label="美妆服饰" value="美妆服饰" />
                <el-option label="其他闲置" value="其他闲置" />
            </el-select>
        </el-form-item>

        <el-form-item label="转让价格" prop="price">
          <el-input-number 
            v-model="form.price" 
            :min="0" 
            :precision="2" 
            :step="1" 
            style="width: 200px;" 
          />
          <span style="margin-left: 10px; color: #999;">元</span>
        </el-form-item>

        <el-form-item label="详细描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请详细描述物品的新旧程度、购买时间、转手原因等，越详细越容易卖出哦~"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-alert
          title="平台担保：发布商品将与您的认证学号终身绑定，请如实描述商品信息，共建诚信校园。"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 25px;"
        />

        <el-form-item>
          <el-button type="primary" @click="submitPublish" :loading="loading" style="width: 150px; font-weight: bold;">
            确 认 发 布
          </el-button>
          <el-button @click="$router.push('/')">取消返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue' // 引入上传图标
import axios from 'axios'

const router = useRouter()
const publishForm = ref(null)
const loading = ref(false)
const imageUrls = ref([])// 🌟 新增：专门用来存放上传成功后返回的图片 URL 数组

// 表单数据绑定
const form = ref({
  title: '',
  category: '',
  price: 0.00,
  description: ''
})

// 表单必填校验规则
const rules = {
  title: [{ required: true, message: '商品标题不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '请输入转让价格', trigger: 'blur' }],
  description: [{ required: true, message: '请简单描述一下商品', trigger: 'blur' }],
  category: [{ required: true, message: '请选择商品分类', trigger: 'change' }]
}
// 新增：处理图片上传成功的逻辑
const handleUploadSuccess = (response, uploadFile) => {
  if (response !== 'error') {
    imageUrls.value.push(response) // 把后端返回的 URL 塞进数组
    // 把 URL 存到 uploadFile 对象里，方便后面删除时用到
    uploadFile.url = response 
  } else {
    ElMessage.error('图片上传失败')
  }
}

//  新增：处理用户点击删除某张图片的逻辑
const handleRemove = (uploadFile) => {
  const urlToRemove = uploadFile.url || uploadFile.response
  // 从数组中过滤掉被删除的那张图的 URL
  imageUrls.value = imageUrls.value.filter(url => url !== urlToRemove)
}

const submitPublish = () => {
  publishForm.value.validate((valid) => {
    if (!valid) return
    
    // 1. 获取当前登录的卖家信息 (防自买自卖的核心依据)
    const userStr = localStorage.getItem('user')
    if (!userStr) {
      ElMessage.error('登录状态已失效，请重新登录')
      router.push('/login')
      return
    }
    const currentUser = JSON.parse(userStr)

    loading.value = true

    // 2. 组装发给后端的数据体，强行塞入 sellerId
    const payload = {
      title: form.value.title,
      category: form.value.category,
      price: form.value.price,
      description: form.value.description,
      sellerId: currentUser.id,
      imageUrls: imageUrls.value // 把刚刚收集到的图片 URL 数组发给后端
    }

    // 3. 发送 POST 请求到咱们已经写好的后端接口
    axios.post('http://localhost:8080/api/products/add', payload)
      .then(res => {
        if (res.data === 'success') {
          ElMessage.success('发布成功！你的闲置已经上架啦~')
          // 发布成功后，直接跳回首页看自己的商品
          router.push('/')
        } else {
          ElMessage.error('发布失败，服务器繁忙')
        }
      })
      .catch(err => {
        console.error(err)
        ElMessage.error('网络请求出错')
      })
      .finally(() => {
        loading.value = false
      })
  })
}
</script>

<style scoped>
.publish-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.publish-card {
  width: 100%;
  max-width: 700px; /* 限制最大宽度，让表单看起来不那么空旷 */
  border-radius: 10px;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}
</style>