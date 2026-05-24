<template>
  <div class="search-container">
    <el-breadcrumb separator="/" style="margin-bottom: 20px;">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>搜索结果</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="search-header">
      <h2>
        关于 <span class="highlight">"{{ currentKeyword }}"</span> 的搜索结果
      </h2>
      <p style="color: #909399;">共找到 {{ searchResults.length }} 件相关的待售宝贝</p>
    </div>

    <el-divider />

    <div v-loading="loading" style="min-height: 300px;">
      <el-row :gutter="20" v-if="searchResults.length > 0">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in searchResults" :key="item.id" style="margin-bottom: 20px;">
          <ProductCard
            :product="item"
            :currentUser="currentUser"
            :favoritedIds="favoritedIds"
            @buy="handleBuy"
            @goDetail="handleGoDetail"
          />
        </el-col>
      </el-row>
      
      <el-empty 
        v-else-if="!loading" 
        description="哎呀，没有找到相关的宝贝，换个关键词试试吧？" 
      >
        <el-button type="primary" @click="$router.push('/')">回首页逛逛</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api/axios'
import ProductCard from '../../components/ProductCard.vue'

const route = useRoute()
const router = useRouter()

const searchResults = ref([])
const loading = ref(false)
const currentKeyword = ref('')
const favoritedIds = ref([])

// 获取当前登录用户（用于传给 ProductCard 判断是否显示"我的发布"）
const currentUser = computed(() => {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user) : null
})

const fetchFavoritedIds = () => {
  if (!currentUser.value) return
  api.get('/api/favorites/ids', {
    params: { userId: currentUser.value.id }
  }).then(res => {
    favoritedIds.value = res.data
  }).catch(() => {})
}

onMounted(() => {
  fetchFavoritedIds()
})

// 核心拉取数据的逻辑
const fetchSearchResults = (keyword) => {
  if (!keyword) {
    searchResults.value = []
    currentKeyword.value = '无关键词'
    return
  }
  
  loading.value = true
  currentKeyword.value = keyword

  // 调用后端已经写好的 search 接口
  api.get('/api/products/search', {
    params: { keyword: keyword }
  }).then(res => {
    // 这里为了严谨，过滤一下，只显示仍在“待售(0)”状态的商品
    searchResults.value = res.data.filter(item => item.status === 0)
  }).catch(() => {
    ElMessage.error('搜索请求失败，请检查网络')
  }).finally(() => {
    loading.value = false
  })
}

// 🌟 Vue Router 的高级技巧：使用 watch 监听路由参数变化
// 为什么不用 onMounted？
// 因为如果用户已经在 /search 页面，再次在顶部导航栏搜索新词，页面组件不会重新挂载！
// 所以必须监听路由的 query 参数变化，一旦变化立刻重新拉取数据。
watch(
  () => route.query.keyword,
  (newKeyword) => {
    fetchSearchResults(newKeyword)
  },
  { immediate: true } // immediate: true 表示页面初次加载时，立刻执行一次 watch 里的逻辑
)

// 复用购买逻辑 (这里可以写得简单点，直接跳转详情页购买，或者保留这里的快捷购买)
const handleBuy = (product) => {
  if (!currentUser.value) {
    ElMessage.warning('请登录后再购买')
    router.push('/login')
    return
  }
  router.push(`/detail/${product.id}`) // 引导去详情页购买体验更好
}
const handleGoDetail = (id) => {
  console.log('Search handleGoDetail, id:', id)
  router.push('/detail/' + id).catch(err => {
    console.error('router push failed:', err)
    window.location.href = '/detail/' + id
  })
}
</script>

<style scoped>
.search-container {
  max-width: 1200px;
  margin: 0 auto;
}
.search-header {
  margin: 20px 0;
}
.search-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}
.highlight {
  color: #409EFF;
  font-weight: bold;
}
</style>