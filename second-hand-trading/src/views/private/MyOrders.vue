<template>
  <div>
    <h2 style="margin-top: 0;">我买到的宝贝</h2>
    <el-table :data="boughtProducts" stripe highlight-current-row style="width: 100%; border-radius: 8px;" @row-click="goDetail">
      <el-table-column label="商品名称">
        <template #default="scope">
          <span style="color: var(--el-color-primary); cursor: pointer;">{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="成交价">
        <template #default="scope">
          <span style="color: #67c23a; font-weight: bold;">￥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '交易完成' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="boughtProducts.length === 0" description="还没买过东西呢，快去首页逛逛吧" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const props = defineProps(['currentUser'])
const router = useRouter()
const boughtProducts = ref([])

const goDetail = (row) => {
  console.log('MyOrders goDetail clicked, id:', row.id)
  router.push('/detail/' + row.id).catch(err => {
    console.error('router push failed:', err)
    window.location.href = '/detail/' + row.id
  })
}

onMounted(() => {
  axios.get('http://10.240.165.107:8080/api/products/my-bought', {
    params: { userId: props.currentUser.id }
  }).then(res => {
    boughtProducts.value = res.data
  })
})
</script>