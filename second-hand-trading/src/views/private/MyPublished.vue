<template>
  <div>
    <h2 style="margin-top: 0;">我发布的商品</h2>
    <el-table :data="myProducts" stripe style="width: 100%; border-radius: 8px;">
      <el-table-column prop="title" label="商品名称" min-width="180" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: bold;">￥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
            {{ scope.row.status === 0 ? '展示中' : '已售出' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button 
            v-if="scope.row.status === 0" 
            type="danger" 
            size="small" 
            plain 
            @click="handleDelete(scope.row.id)"
          >
            下架删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const props = defineProps(['currentUser'])
const myProducts = ref([])

const loadMyProducts = () => {
  axios.get('http://localhost:8080/api/products/my-published', {
    params: { userId: props.currentUser.id }
  }).then(res => {
    myProducts.value = res.data
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要下架并永久删除该闲置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    axios.delete(`http://localhost:8080/api/products/delete/${id}`, {
      params: { userId: props.currentUser.id }
    }).then(res => {
      if (res.data === 'success') {
        ElMessage.success('商品已成功下架')
        loadMyProducts() // 重新刷新列表
      }
    })
  })
}

onMounted(loadMyProducts)
</script>