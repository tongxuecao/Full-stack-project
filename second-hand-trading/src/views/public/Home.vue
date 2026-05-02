<template>
  <div class="home-container">
    <div class="banner">
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
        <el-col :span="6" v-for="item in filteredProducts" :key="item.id" style="margin-bottom: 20px;">
          <ProductCard 
            :product="item" 
            :currentUser="currentUser" 
            @buy="handleBuy" 
            @go-detail="handleGoDetail"
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
// 确保路径正确指向你的组件文件夹
import ProductCard from '../../components/ProductCard.vue'

const router = useRouter()
const activeTab = ref('all')
const productList = ref([])
const currentUser = ref(null)
const loading = ref(false)

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
  }
  loadPageData() // 页面初始化时拉取第一页数据
})

// 🌟 核心：向后端请求分页数据
const loadPageData = () => {
  loading.value = true
  axios.get('http://localhost:8080/api/products/page', {
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
    axios.post(`http://localhost:8080/api/products/buy/${product.id}`, null, {
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
  router.push(`/detail/${id}`)
}
</script>

<style scoped>
.banner {
  background: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
  border-radius: 12px;
  padding: 40px 20px;
  text-align: center;
  color: white;
  margin-bottom: 25px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}
.banner h2 { 
  margin: 0 0 10px 0; 
  font-size: 32px; 
  letter-spacing: 2px;
}
.banner p { 
  margin: 0; 
  font-size: 16px; 
  opacity: 0.9; 
}
.category-tabs { 
  margin-bottom: 20px; 
  background: #fff;
  padding: 5px 20px 0;
  border-radius: 8px;
}
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-bottom: 20px;
}
</style>