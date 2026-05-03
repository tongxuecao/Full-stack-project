<template>
  <div class="detail-container" v-loading="loading">
    <el-breadcrumb separator="/" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品详情</el-breadcrumb-item>
    </el-breadcrumb>

    <el-row :gutter="40" v-if="product">
      <el-col :span="12">
        <div class="image-gallery">
          <div class="main-image">
            <span v-if="product.status === 1" class="sold-out-badge">已 售 出</span>
            
            <el-carousel v-if="product.images && product.images.length > 0" height="400px" trigger="click">
              <el-carousel-item v-for="img in product.images" :key="img.id">
                <el-image :src="img.imageUrl" fit="contain" style="width: 100%; height: 100%; background-color: #f5f7fa;" />
              </el-carousel-item>
            </el-carousel>

            <div v-else style="background-color: #f5f7fa; width: 100%; height: 400px; display: flex; align-items: center; justify-content: center; border-radius: 8px;">
              <span style="color: #909399; font-size: 20px;">暂无实物图片</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="product-info">
          <el-tag type="primary" effect="dark" style="margin-bottom: 10px;">{{ product.category || '其他闲置' }}</el-tag>
          <h1 class="title">{{ product.title }}</h1>
          
          <div class="price-box">
            <span class="currency">￥</span>
            <span class="amount">{{ product.price }}</span>
          </div>

          <el-divider />

          <div class="description">
            <h3>宝贝描述</h3>
            <p>{{ product.description }}</p>
          </div>

          <div class="seller-card">
            <div class="seller-header">卖家信息</div>
            <div class="seller-body">
              <p><strong>昵称：</strong>{{ seller.username || '加载中...' }}</p>
              <p><strong>认证学号：</strong>{{ seller.schoolId || '已核验' }}</p>
              <p><strong>联系方式：</strong>
                <span v-if="isLoggedIn" style="color: #409EFF; font-weight: bold;">{{ seller.phone || '尚未填写' }}</span>
                <span v-else style="color: #f56c6c;">登录后可见手机号</span>
              </p>
            </div>
          </div>

          <div class="actions" style="margin-top: 30px;">
            <el-button 
              type="success" 
              size="large" 
              icon="ShoppingCart" 
              :disabled="product.status === 1"
              @click="handleBuy"
              style="width: 200px; height: 50px; font-size: 18px;"
            >
              {{ product.status === 1 ? '已被抢走啦' : '立即购买' }}
            </el-button>
            <el-button
              v-if="isLoggedIn && currentUser.id !== product.sellerId"
              size="large"
              :icon="isFav ? StarFilled : Star"
              :type="isFav ? 'warning' : 'default'"
              plain
              @click="toggleFavorite"
            >
              {{ isFav ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const product = ref(null)
const seller = ref({}) // 存放卖家详细信息

// 获取当前登录状态
const currentUser = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user) : null
})
const isLoggedIn = computed(() => !!currentUser.value)

const isFav = ref(false)

const fetchDetail = async () => {
  loading.value = true
  const id = route.params.id
  try {
    const res = await axios.get(`http://localhost:8080/api/products/detail/${id}`)
    product.value = res.data

    const sellerRes = await axios.get(`http://localhost:8080/api/users/info/${product.value.sellerId}`)
    seller.value = sellerRes.data

    if (currentUser.value) {
      checkFavorite()
    }
  } catch (error) {
    ElMessage.error('获取详情失败，请检查后端接口')
  } finally {
    loading.value = false
  }
}

const checkFavorite = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/favorites/check', {
      params: { userId: currentUser.value.id, productId: product.value.id }
    })
    isFav.value = res.data
  } catch (e) {
    // ignore
  }
}

const toggleFavorite = async () => {
  try {
    if (isFav.value) {
      await axios.delete('http://localhost:8080/api/favorites/remove', {
        params: { userId: currentUser.value.id, productId: product.value.id }
      })
      isFav.value = false
      ElMessage.success('已取消收藏')
    } else {
      const res = await axios.post('http://localhost:8080/api/favorites/add', null, {
        params: { userId: currentUser.value.id, productId: product.value.id }
      })
      if (res.data === 'already') {
        isFav.value = true
        ElMessage.info('已经收藏过了')
      } else {
        isFav.value = true
        ElMessage.success('收藏成功')
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleBuy = () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请登录后再进行购买')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  
  if (currentUser.value.id === product.value.sellerId) {
    ElMessage.error('不能购买自己发布的宝贝哦！')
    return
  }

  ElMessageBox.confirm(`确认以 ￥${product.value.price} 的价格购买吗？`, '交易确认').then(() => {
    axios.post(`http://localhost:8080/api/products/buy/${product.value.id}`, null, {
      params: { buyerId: currentUser.value.id }
    }).then(res => {
      if (res.data === 'success') {
        ElMessage.success('购买成功！请根据展示的手机号联系卖家。')
        product.value.status = 1
      }
    })
  })
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}
.main-image {
  position: relative;
  overflow: hidden;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.sold-out-badge {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  color: white;
  display: flex;
  align-items: center; justify-content: center;
  font-size: 30px; font-weight: bold; z-index: 10;
}
.title {
  font-size: 28px;
  margin: 10px 0;
  color: #303133;
}
.price-box {
  margin: 20px 0;
  color: #f56c6c;
}
.currency { font-size: 24px; font-weight: bold; }
.amount { font-size: 40px; font-weight: bold; }

.description {
  color: #606266;
  line-height: 1.8;
  margin-bottom: 30px;
}

.seller-card {
  background: var(--el-color-warning-light-9);
  border: 1px solid var(--el-color-warning-light-7);
  border-radius: 8px;
  padding: 20px;
  transition: background 0.3s, border-color 0.3s;
}
.seller-header {
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--el-color-warning);
  border-bottom: 1px solid var(--el-color-warning-light-7);
  padding-bottom: 10px;
}
.seller-body p {
  margin: 8px 0;
  font-size: 15px;
}
</style>