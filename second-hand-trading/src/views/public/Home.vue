<template>
  <div class="home-container">
    <div class="banner" :class="{ 'banner-dark': isDark }">
      <h2>发现校园好物</h2>
      <p>让闲置物品重新发光，安全便捷的校内交易</p>
    </div>

    <el-tabs v-model="activeTab" class="category-tabs">
      <el-tab-pane label="全部闲置" name="all"></el-tab-pane>
      <el-tab-pane label="数码3C" name="数码3C"></el-tab-pane>
      <el-tab-pane label="书籍资料" name="书籍资料"></el-tab-pane>
      <el-tab-pane label="生活用品" name="生活用品"></el-tab-pane>
      <el-tab-pane label="美妆服饰" name="美妆服饰"></el-tab-pane>
      <el-tab-pane label="其他闲置" name="其他闲置"></el-tab-pane>
    </el-tabs>

    <div v-loading="loading" style="min-height: 300px;">
      <el-row :gutter="20" v-if="filteredProducts.length > 0">
       <el-col :xs="12" :sm="8" :md="6" v-for="item in filteredProducts" :key="item.id" style="margin-bottom: 20px;">
          <ProductCard
            :product="item"
            :currentUser="currentUser"
            :favoritedIds="favoritedIds"
            @buy="handleBuy"
            @goDetail="handleGoDetail"
          />
        </el-col>
      </el-row>
      
      <el-empty v-else-if="!loading" description="这个分类下暂时没有宝贝哦，快去发布一个吧！" />
    </div>
  </div>
  <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="handlePageChange"
      />
 </div>

</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api/axios'
import ProductCard from '../../components/ProductCard.vue'

const router = useRouter()
const activeTab = ref('all')
const productList = ref([])
const currentUser = ref(null)
const favoritedIds = ref([])
const loading = ref(false)
const isDark = ref(document.documentElement.classList.contains('dark'))

let observer = null
onMounted(() => {
  observer = new MutationObserver(() => {
    isDark.value = document.documentElement.classList.contains('dark')
  })
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
})
onUnmounted(() => {
  observer?.disconnect()
})

const currentPage = ref(1)
const pageSize = ref(8) // 每页展示 8 条数据
const total = ref(0)    // 数据库里符合条件的总条数
// 前端计算属性：根据当前选中的 tab 过滤商品
const filteredProducts = computed(() => {
  if (activeTab.value === 'all') {
    return productList.value
  }
  return productList.value.filter(item => item.category === activeTab.value)
})

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
    fetchFavoritedIds()
  }
  loadPageData()
})

const fetchFavoritedIds = () => {
  if (!currentUser.value) return
  api.get('/api/favorites/ids', {
    params: { userId: currentUser.value.id }
  }).then(res => {
    favoritedIds.value = res.data
  }).catch(() => {})
}

// 🌟 核心：向后端请求分页数据
const loadPageData = () => {
  loading.value = true
  api.get('/api/products/page', {
    params: {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      category: activeTab.value // 把当前选中的分类传给后端
    }
  }).then(res => {
    // PageHelper 返回的数据结构中，真实的列表在 list 属性里，总条数在 total 属性里
    productList.value = res.data.list
    total.value = res.data.total
  }).catch(err => {
    console.error(err)
    ElMessage.error('获取商品列表失败')
  }).finally(() => {
    loading.value = false
  })
}

// 当用户点击底部的页码时触发
const handlePageChange = (newPage) => {
  currentPage.value = newPage
  loadPageData() // 重新拉取新一页的数据
}

// 当用户点击顶部的分类标签时触发
const handleCategoryChange = () => {
  currentPage.value = 1 // 切换分类时，必须强行把页码重置回第 1 页
  loadPageData()
}
// 购买逻辑
const handleBuy = (product) => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录后再购买宝贝哦')
    router.push('/login')
    return
  }

  ElMessageBox.confirm(
    `确定要花 ￥${product.price} 购买【${product.title}】吗？`, 
    '购买确认', 
    {
      confirmButtonText: '确认购买',
      cancelButtonText: '再想想',
      type: 'success',
    }
  ).then(() => {
    api.post(`/api/products/buy/${product.id}`, null, {
      params: { buyerId: currentUser.value.id }
    }).then(res => {
      if (res.data === 'success') {
        ElMessage.success('购买成功！请根据学号联系卖家进行线下交易。')
        product.status = 1 
      } else {
        ElMessage.error('购买失败，商品可能已经被抢走啦')
        loadProducts() 
      }
    })
  }).catch(() => {})
}

// 跳转详情页逻辑
const handleGoDetail = (id) => {
  console.log('Home handleGoDetail, id:', id)
  router.push('/detail/' + id).catch(err => {
    console.error('router push failed:', err)
    window.location.href = '/detail/' + id
  })
}
</script>

<style scoped>
.banner {
  background: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
  border-radius: 12px;
  padding: 40px 20px;
  text-align: center;
  color: #fff;
  margin-bottom: 25px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  transition: background 0.4s, color 0.3s, box-shadow 0.3s;
}

.banner-dark {
  background: linear-gradient(120deg, #1e3a2f 0%, #1e3848 100%) !important;
  color: #d0d5db;
  box-shadow: 0 4px 15px rgba(0,0,0,0.35);
}
.banner h2 { 
  margin: 0 0 10px 0; 
  font-size: 32px; 
  letter-spacing: 2px;
}
/* 🌟 当屏幕宽度小于 768px（也就是手机设备）时，应用以下样式覆盖上面的 */
@media (max-width: 768px) {
  .banner {
    padding: 20px 10px; /* 缩小内边距 */
    margin-bottom: 15px; /* 缩小底部间距 */
  }
  .banner h2 {
    font-size: 22px; /* 缩小标题字体 */
  }
  .banner p {
    font-size: 14px; /* 缩小副标题字体 */
  }
}
.banner p { 
  margin: 0; 
  font-size: 16px; 
  opacity: 0.9; 
}
.category-tabs {
  margin-bottom: 20px;
  background: var(--el-bg-color);
  padding: 5px 20px 0;
  border-radius: 8px;
  transition: background 0.3s;
}
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-bottom: 20px;
}
@media (max-width: 768px) {
  /* 隐藏 Element Plus 默认的左右滚动箭头 */
  :deep(.el-tabs__nav-next), 
  :deep(.el-tabs__nav-prev) {
    display: none !important;
  }
  
  /* 让导航区域支持原生左右滑动，且隐藏滚动条 */
  :deep(.el-tabs__nav-wrap) {
    overflow-x: auto !important;
    -webkit-overflow-scrolling: touch; /* 让苹果手机滑动更丝滑 */
  }
  
  :deep(.el-tabs__nav-wrap::-webkit-scrollbar) {
    display: none; /* 隐藏底部丑陋的滚动条 */
  }

  /* 缩小标签的内边距和字体 */
  :deep(.el-tabs__item) {
    padding: 0 12px !important;
    font-size: 14px;
  }
}
</style>