<template>
  <div class="detail-container" v-loading="loading">
    <el-breadcrumb separator="/" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品详情</el-breadcrumb-item>
    </el-breadcrumb>

    <el-row :gutter="40" v-if="product">
      <el-col :xs="24" :md="12">
        <div class="product-gallery">
          <div class="main-image-wrapper">
            <span v-if="product.status === 1" class="sold-out-badge">已 售 出</span>

            <el-image
              v-if="product.images && product.images.length > 0"
              :src="product.images[0].imageUrl"
              :preview-src-list="previewList"
              :initial-index="0"
              fit="cover"
              class="main-image"
              hide-on-click-modal
              preview-teleported
            />

            <div v-else class="no-image-placeholder">
              <el-icon :size="40"><Picture /></el-icon>
              <span>暂无图片</span>
            </div>
          </div>

          <div v-if="product.images && product.images.length > 1" class="thumbnail-list">
            <el-image
              v-for="(img, index) in product.images.slice(1)"
              :key="img.id"
              :src="img.imageUrl"
              :preview-src-list="previewList"
              :initial-index="index + 1"
              fit="cover"
              class="thumb-item"
              hide-on-click-modal
              preview-teleported
            />
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :md="12" style="margin-top: 15px;">
        <div class="product-info">
          <div style="display: flex; gap: 8px; align-items: center; margin-bottom: 10px;">
            <el-tag type="primary" effect="dark">{{ product.category || '其他闲置' }}</el-tag>
            <el-tag v-if="isLoggedIn && currentUser.id === product.sellerId" type="warning" effect="dark">我的发布</el-tag>
            <el-tag v-if="product.status === 0" type="success" effect="plain">待售中</el-tag>
            <el-tag v-else-if="product.status === 1" type="danger" effect="plain">已售出</el-tag>
            <el-tag v-else-if="product.status === -1" type="info" effect="plain">已下架</el-tag>
          </div>
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

          <div v-if="isLoggedIn && currentUser.id === product.sellerId && product.status === 0" class="seller-actions" style="margin-top: 20px;">
            <el-alert title="这是您发布的商品" type="warning" show-icon :closable="false" style="margin-bottom: 12px;" />
            <el-button type="danger" plain size="large" @click="handleUnlist" style="width: 200px;">
              撤销上架
            </el-button>
          </div>

          <div class="actions" style="margin-top: 30px;">
            <el-button
              v-if="!isLoggedIn || currentUser.id !== product.sellerId"
              type="success"
              size="large"
              icon="ShoppingCart"
              :disabled="product.status !== 0"
              @click="handleBuy"
              style="width: 200px; height: 50px; font-size: 18px;"
            >
              {{ product.status === 1 ? '已被抢走啦' : product.status === -1 ? '已下架' : '立即购买' }}
            </el-button>
            <el-button
              v-if="isLoggedIn && currentUser.id !== product.sellerId"
              type="primary"
              size="large"
              plain
              icon="ChatDotRound"
              @click="showChat = true"
            >
              联系卖家
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

    <ChatWindow
      :visible="showChat"
      :productId="product?.id"
      :otherUserId="product?.sellerId"
      :otherUserName="seller?.username"
      :currentUser="currentUser"
      @close="showChat = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, StarFilled, Picture, ChatDotRound } from '@element-plus/icons-vue'
import axios from 'axios'
import ChatWindow from '../../components/ChatWindow.vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const product = ref(null)
const seller = ref({})
const showChat = ref(false)

// 获取当前登录状态
const currentUser = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user) : null
})
const isLoggedIn = computed(() => !!currentUser.value)

const isFav = ref(false)

const previewList = computed(() => {
  if (!product.value?.images) return []
  return product.value.images.map(img => img.imageUrl)
})

const fetchDetail = async () => {
  loading.value = true
  const id = route.params.id
  try {
    const res = await axios.get(`http://10.240.165.107:8080/api/products/detail/${id}`)
    product.value = res.data

    const sellerRes = await axios.get(`http://10.240.165.107:8080/api/users/info/${product.value.sellerId}`)
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
    const res = await axios.get('http://10.240.165.107:8080/api/favorites/check', {
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
      await axios.delete('http://10.240.165.107:8080/api/favorites/remove', {
        params: { userId: currentUser.value.id, productId: product.value.id }
      })
      isFav.value = false
      ElMessage.success('已取消收藏')
    } else {
      const res = await axios.post('http://10.240.165.107:8080/api/favorites/add', null, {
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
    axios.post(`http://10.240.165.107:8080/api/products/buy/${product.value.id}`, null, {
      params: { buyerId: currentUser.value.id }
    }).then(res => {
      if (res.data === 'success') {
        ElMessage.success('购买成功！请根据展示的手机号联系卖家。')
        product.value.status = 1
      }
    })
  })
}

const handleUnlist = () => {
  ElMessageBox.confirm('确定要下架该商品吗？下架后其他用户将无法看到。', '确认下架', {
    confirmButtonText: '确定下架',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      const res = await axios.delete(`http://10.240.165.107:8080/api/products/delete/${product.value.id}`, {
        params: { userId: currentUser.value.id }
      })
      if (res.data === 'success') {
        ElMessage.success('商品已成功下架')
        product.value.status = -1
      } else {
        ElMessage.error('下架失败，请重试')
      }
    } catch (e) {
      ElMessage.error('网络请求失败')
    }
  }).catch(() => {})
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}
.product-gallery {
  width: 100%;
}

.main-image-wrapper {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.main-image {
  width: 100%;
  height: 400px;
  display: block;
}

.main-image :deep(img) {
  cursor: zoom-in;
}

.sold-out-badge {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  color: #fff;
  display: flex;
  align-items: center; justify-content: center;
  font-size: 30px; font-weight: bold; z-index: 10;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.thumb-item {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  border: 2px solid transparent;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s, transform 0.2s;
}

.thumb-item:hover {
  border-color: var(--el-color-primary);
  transform: scale(1.05);
}

.thumb-item :deep(img) {
  cursor: zoom-in;
}

.no-image-placeholder {
  width: 100%;
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: var(--el-fill-color-light);
  border-radius: 12px;
  color: var(--el-text-color-secondary);
  font-size: 16px;
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