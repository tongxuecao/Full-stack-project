<template>
  <div class="favorites-page">
    <div class="page-header">
      <h2>我的收藏</h2>
      <span class="count">共 {{ favorites.length }} 件宝贝</span>
    </div>

    <div v-loading="loading" style="min-height: 200px;">
      <el-row :gutter="20" v-if="favorites.length > 0">
        <el-col :xs="24" :sm="12" :md="8" v-for="item in favorites" :key="item.id" style="margin-bottom: 20px;">
          <el-card shadow="hover" class="favorite-card">
            <div class="card-image" @click="goDetail(item.id)">
              <el-image
                v-if="item.images && item.images.length > 0"
                :src="getImageUrl(item.images[0].imageUrl)"
                fit="cover"
                style="width: 100%; height: 100%;"
              />
              <span v-else style="color: #999;">暂无图片</span>
              <div v-if="item.status === 1" class="sold-mask">已售出</div>
            </div>
            <div class="card-body">
              <div class="card-title" @click="goDetail(item.id)">{{ item.title }}</div>
              <div class="card-footer">
                <span class="price">￥{{ item.price }}</span>
                <el-button size="small" type="danger" plain @click="handleRemove(item.id)">
                  <el-icon><Delete /></el-icon> 取消收藏
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else-if="!loading" description="还没有收藏宝贝，快去首页逛逛吧">
        <el-button type="primary" @click="$router.push('/')">去逛逛</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import api from '@/api/axios'
import { getImageUrl } from '@/config'

const props = defineProps(['currentUser'])
const router = useRouter()
const favorites = ref([])
const loading = ref(false)

const fetchFavorites = () => {
  loading.value = true
  api.get('/api/favorites/list', {
    params: { userId: props.currentUser.id }
  }).then(res => {
    favorites.value = res.data
  }).catch(() => {
    ElMessage.error('获取收藏列表失败')
  }).finally(() => {
    loading.value = false
  })
}

const handleRemove = (productId) => {
  ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.delete('/api/favorites/remove', {
      params: { userId: props.currentUser.id, productId }
    }).then(res => {
      if (res.data === 'success') {
        favorites.value = favorites.value.filter(f => f.id !== productId)
        ElMessage.success('已取消收藏')
      }
    })
  }).catch(() => {})
}

const goDetail = (id) => {
  console.log('MyFavorites goDetail, id:', id)
  router.push('/detail/' + id).catch(err => {
    console.error('router push failed:', err)
    window.location.href = '/detail/' + id
  })
}

onMounted(fetchFavorites)
</script>

<style scoped>
.favorites-page {
  max-width: 100%;
}
.page-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 20px;
}
.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: var(--el-text-color-primary);
}
.page-header .count {
  color: var(--el-text-color-secondary);
  font-size: 14px;
}
.favorite-card {
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.2s;
}
.favorite-card:hover {
  transform: translateY(-2px);
}
.card-image {
  height: 160px;
  background: var(--el-fill-color-light);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.sold-mask {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.5);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
}
.card-body {
  padding: 12px 0 0;
}
.card-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  margin-bottom: 12px;
}
.card-title:hover {
  color: var(--el-color-primary);
}
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}
</style>
