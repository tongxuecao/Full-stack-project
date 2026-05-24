<template>
  <div>
    <h2 style="margin-top: 0;">我的消息</h2>
    <div v-loading="loading" style="min-height: 200px;">
      <div v-if="conversations.length > 0" class="conv-list">
        <div
          v-for="conv in conversations"
          :key="conv.productId + '_' + conv.otherUserId"
          class="conv-item"
          @click="openChat(conv)"
        >
          <el-avatar :size="44" :src="getImageUrl(conv.otherUserAvatar)">
            {{ conv.otherUserName?.charAt(0) }}
          </el-avatar>
          <div class="conv-info">
            <div class="conv-top">
              <span class="conv-name">{{ conv.otherUserName }}</span>
              <span class="conv-time">{{ formatTime(conv.lastMessage?.createTime) }}</span>
            </div>
            <div class="conv-preview">{{ conv.productTitle }}</div>
            <div class="conv-msg">{{ conv.lastMessage?.content }}</div>
          </div>
        </div>
      </div>
      <el-empty v-else-if="!loading" description="暂无消息" />
    </div>

    <ChatWindow
      :visible="showChat"
      :productId="activeProductId"
      :otherUserId="activeOtherUserId"
      :otherUserName="activeOtherUserName"
      :currentUser="currentUser"
      @close="showChat = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api/axios'
import { getImageUrl } from '@/config'
import ChatWindow from '../../components/ChatWindow.vue'

const props = defineProps(['currentUser'])
const currentUser = props.currentUser
const conversations = ref([])
const loading = ref(false)
const showChat = ref(false)
const activeProductId = ref(0)
const activeOtherUserId = ref(0)
const activeOtherUserName = ref('')

const fetchConversations = async () => {
  loading.value = true
  try {
    const res = await api.get('/api/chat/conversations', {
      params: { userId: props.currentUser.id }
    })
    conversations.value = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    console.error('获取会话列表失败:', e)
    ElMessage.error('获取消息列表失败')
  } finally {
    loading.value = false
  }
}

const openChat = (conv) => {
  activeProductId.value = conv.productId
  activeOtherUserId.value = conv.otherUserId
  activeOtherUserName.value = conv.otherUserName
  showChat.value = true
}

const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  const now = new Date()
  if (d.toDateString() === now.toDateString()) {
    return d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  return d.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' })
}

onMounted(fetchConversations)
</script>

<style scoped>
.conv-list {
  display: flex;
  flex-direction: column;
}

.conv-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.2s;
}

.conv-item:hover {
  background: var(--el-fill-color-light);
}

.conv-info {
  flex: 1;
  min-width: 0;
}

.conv-top {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.conv-name {
  font-weight: 600;
  font-size: 15px;
  color: var(--el-text-color-primary);
}

.conv-time {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}

.conv-preview {
  margin-top: 2px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.conv-msg {
  margin-top: 4px;
  font-size: 13px;
  color: var(--el-text-color-placeholder);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
