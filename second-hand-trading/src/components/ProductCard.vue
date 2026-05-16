<template>
  <div class="card-wrapper" @click="handleCardClick">
    <el-card shadow="hover" :body-style="{ padding: 0 }">
      <div class="card-image-box">
        <el-image
          v-if="product.images && product.images.length > 0"
          :src="product.images[0].imageUrl"
          fit="cover"
          style="width: 100%; height: 100%; position: absolute; inset: 0;"
        />

        <div v-if="product.status === 1" class="sold-mask">已 售 出</div>
        <span v-else-if="!product.images || product.images.length === 0" style="color: #999; z-index: 1;">暂无图片</span>

        <el-button
          v-if="currentUser && currentUser.id != product.sellerId && product.status !== 1"
          class="fav-btn"
          :type="isFav ? 'danger' : 'default'"
          size="small"
          circle
          :icon="isFav ? StarFilled : Star"
          @click.stop="toggleFavorite"
        />
      </div>

      <div style="padding: 14px;">
        <div class="card-title">{{ product.title }}</div>
        <div class="card-bottom">
          <span class="price">￥{{ product.price }}</span>
          <el-button v-if="product.status === 1" type="info" size="small" disabled>卖掉啦</el-button>
          <el-tag v-else-if="currentUser && currentUser.id == product.sellerId" type="warning">我的发布</el-tag>
          <el-button v-else type="success" size="small" @click.stop="handleBuyClick">立即购买</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const props = defineProps(['product', 'currentUser', 'favoritedIds'])
const emit = defineEmits(['buy', 'goDetail'])

const isFav = ref(false)

watch(() => [props.favoritedIds, props.product], () => {
  if (props.favoritedIds && props.product) {
    isFav.value = props.favoritedIds.includes(props.product.id)
  }
}, { immediate: true })

const handleCardClick = () => {
  emit('goDetail', props.product.id)
}

const handleBuyClick = () => {
  emit('buy', props.product)
}

const toggleFavorite = async () => {
  if (!props.currentUser) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    if (isFav.value) {
      await axios.delete('http://10.240.165.107:8080/api/favorites/remove', {
        params: { userId: props.currentUser.id, productId: props.product.id }
      })
      isFav.value = false
      ElMessage.success('已取消收藏')
    } else {
      const res = await axios.post('http://10.240.165.107:8080/api/favorites/add', null, {
        params: { userId: props.currentUser.id, productId: props.product.id }
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
</script>

<style scoped>
.card-wrapper {
  cursor: pointer;
  margin-bottom: 20px;
}

.card-image-box {
  height: 150px;
  background: var(--el-fill-color-light);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  transition: background 0.3s;
}

.sold-mask {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.5);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  z-index: 10;
}

.fav-btn {
  position: absolute;
  top: 6px;
  right: 6px;
  z-index: 20;
  opacity: 0.85;
  transition: opacity 0.2s, transform 0.2s;
}

.fav-btn:hover {
  opacity: 1;
  transform: scale(1.1);
}

.card-title {
  font-weight: bold;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.price {
  color: red;
  font-weight: bold;
  font-size: 18px;
}
</style>
