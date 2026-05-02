<template>
  <div>
    <h2 style="margin-top: 0;">我买到的宝贝</h2>
    <el-table :data="boughtProducts" stripe style="width: 100%; border-radius: 8px;">
      <el-table-column prop="title" label="商品名称" />
      <el-table-column prop="price" label="成交价">
        <template #default="scope">
          <span style="color: #67c23a; font-weight: bold;">￥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易状态">
        <template #default="scope">
          <el-tag type="success">交易完成</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="boughtProducts.length === 0" description="还没买过东西呢，快去首页逛逛吧" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const props = defineProps(['currentUser'])
const boughtProducts = ref([])

onMounted(() => {
  axios.get('http://localhost:8080/api/products/my-bought', {
    params: { userId: props.currentUser.id }
  }).then(res => {
    boughtProducts.value = res.data
  })
})
</script>