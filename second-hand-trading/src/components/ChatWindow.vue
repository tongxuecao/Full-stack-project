<template>
  <div v-if="visible" class="chat-window">
    <div class="chat-header">
      <span>{{ otherUserName || '聊天' }} <small v-if="connecting">(连接中...)</small></span>
      <el-button text circle :icon="Close" @click="$emit('close')" />
    </div>
    <div class="chat-messages" ref="msgContainer">
      <div v-for="msg in mergedMessages" :key="msg.id" :class="['msg-bubble', msg.senderId === currentUser?.id ? 'msg-mine' : 'msg-other']">
        <div class="msg-content">{{ msg.content }}</div>
        <div class="msg-time">{{ formatTime(msg.createTime) }}</div>
      </div>
      <el-empty v-if="mergedMessages.length === 0 && !historyLoaded" description="加载中..." :image-size="60" />
      <el-empty v-else-if="mergedMessages.length === 0 && historyLoaded" description="暂无消息，发送第一条消息吧" :image-size="60" />
    </div>
    <div class="chat-input-area">
      <el-input v-model="newMsg" placeholder="输入消息..." @keyup.enter="handleSend" :disabled="connecting" />
      <el-button type="primary" :icon="Promotion" @click="handleSend" :disabled="!newMsg.trim() || connecting" :loading="connecting">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Close, Promotion } from '@element-plus/icons-vue'
import { useChat } from '../composables/useChat.js'
import axios from 'axios'

const props = defineProps({
  visible: Boolean,
  productId: Number,
  otherUserId: Number,
  otherUserName: String,
  currentUser: Object
})
defineEmits(['close'])

const { messages, connected, connecting, sendMessage, connect, disconnect, setMessages } = useChat(props.currentUser?.id)
const newMsg = ref('')
const msgContainer = ref(null)
const historyLoaded = ref(false)

const mergedMessages = computed(() => {
  const seen = new Set()
  return messages.value.filter(m => {
    if (seen.has(m.id)) return false
    seen.add(m.id)
    return true
  })
})

const loadHistory = async () => {
  if (!props.productId || !props.currentUser || !props.otherUserId) return
  try {
    const res = await axios.get('http://10.240.165.107:8080/api/chat/history', {
      params: {
        productId: props.productId,
        userId1: props.currentUser.id,
        userId2: props.otherUserId
      }
    })
    setMessages(Array.isArray(res.data) ? res.data : [])
    historyLoaded.value = true
    scrollToBottom()
  } catch (e) {
    console.error('History load failed:', e)
    historyLoaded.value = true
  }
}

const handleSend = async () => {
  const text = newMsg.value.trim()
  if (!text || !props.currentUser || !props.otherUserId) return

  if (!connected.value) {
    ElMessage.warning('正在连接聊天服务，请稍后再试')
    return
  }

  const ok = sendMessage({//userChat中发送信息的函数，参数是一个对象，包含了商品id、发送者id、接收者id和消息内容
    productId: props.productId,
    senderId: props.currentUser.id,
    receiverId: props.otherUserId,
    content: text
  })
  if (ok) {
    newMsg.value = ''
    nextTick(scrollToBottom)
  } else {
    ElMessage.warning('发送失败，请稍后再试')
  }
}

const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  const h = d.getHours().toString().padStart(2, '0')
  const m = d.getMinutes().toString().padStart(2, '0')
  return `${h}:${m}`
}

const scrollToBottom = () => {
  nextTick(() => {
    const el = msgContainer.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

watch(() => messages.value.length, scrollToBottom)

watch(() => [props.visible, props.otherUserId], ([v, uid]) => {
  if (v && uid) {
    historyLoaded.value = false
    setMessages([])
    loadHistory()
    connect()
  } else {
    disconnect()
    historyLoaded.value = false
  }
})

onUnmounted(() => {
  disconnect()
})
</script>

<style scoped>
.chat-window {
  position: fixed;
  bottom: 24px;
  right: 24px;
  width: 380px;
  height: 520px;
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  z-index: 1000;
  overflow: hidden;
  transition: background 0.3s;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: var(--el-color-primary);
  color: #fff;
  font-weight: 600;
  font-size: 15px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  background: var(--el-fill-color-lighter);
  transition: background 0.3s;
}

.msg-bubble {
  margin-bottom: 10px;
  max-width: 85%;
}

.msg-mine {
  margin-left: auto;
}

.msg-mine .msg-content {
  background: var(--el-color-primary);
  color: #fff;
  border-radius: 16px 16px 4px 16px;
}

.msg-other .msg-content {
  background: var(--el-bg-color);
  color: var(--el-text-color-primary);
  border-radius: 16px 16px 16px 4px;
  border: 1px solid var(--el-border-color-lighter);
}

.msg-content {
  padding: 10px 14px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.msg-time {
  font-size: 11px;
  color: var(--el-text-color-placeholder);
  margin-top: 4px;
  padding: 0 4px;
}

.msg-mine .msg-time {
  text-align: right;
}

.chat-input-area {
  display: flex;
  gap: 8px;
  padding: 10px 12px;
  border-top: 1px solid var(--el-border-color-lighter);
  background: var(--el-bg-color);
  transition: background 0.3s;
}
</style>
