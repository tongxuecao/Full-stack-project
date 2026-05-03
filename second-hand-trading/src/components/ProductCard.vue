<template>
  <el-card 
    shadow="hover" 
    :style="{ opacity: product.status === 1 ? 0.6 : 1, marginBottom: '20px', cursor: 'pointer' }"
    @click="$emit('go-detail', product.id)"
  >
    
    <div style="height: 150px; background-color: #f0f2f5; position: relative; display: flex; align-items: center; justify-content: center; overflow: hidden;">
      
      <el-image 
        v-if="product.images && product.images.length > 0" 
        :src="product.images[0].imageUrl" 
        fit="cover" 
        style="width: 100%; height: 100%; position: absolute; inset: 0;"
      />
      
      <div v-if="product.status === 1" 
          style="position: absolute; inset: 0; background: rgba(0,0,0,0.5); color: white; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 18px; z-index: 10;">
        已 售 出
      </div>
      
      <span v-else-if="!product.images || product.images.length === 0" style="color: #999; z-index: 1;">暂无图片</span>
    </div>

    <div style="padding: 14px;">
      <div style="font-weight: bold; font-size: 16px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
        {{ product.title }}
      </div>
      
      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
        <span style="color: red; font-weight: bold; font-size: 18px;">￥{{ product.price }}</span>

        <el-button v-if="product.status === 1" type="info" size="small" disabled>
          卖掉啦
        </el-button>
        
        <el-tag v-else-if="currentUser && currentUser.id == product.sellerId" type="warning">
          我的发布
        </el-tag>
        
        <el-button v-else type="success" size="small" @click.stop="$emit('buy', product)">
          立即购买
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
defineProps(['product', 'currentUser'])
// 3. 声明新增的 go-detail 事件
defineEmits(['buy', 'go-detail'])
</script>