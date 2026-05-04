import { ref, onUnmounted } from 'vue'

export function useChat(userId) {
  const messages = ref([])
  const connected = ref(false)
  const connecting = ref(false)
  let stompClient = null

  const connect = () => {
    if (!userId || connecting.value || connected.value) return
    connecting.value = true

    import('@stomp/stompjs').then(({ Client }) => {
      stompClient = new Client({
        brokerURL: 'ws://localhost:8080/ws-native',
        reconnectDelay: 5000,
        heartbeatIncoming: 10000,
        heartbeatOutgoing: 10000,
        onConnect: () => {
          connected.value = true
          connecting.value = false
          stompClient.subscribe(`/topic/chat.${userId}`, (message) => {
            try {
              const msg = JSON.parse(message.body)
              const exists = messages.value.find(m => m.id === msg.id)
              if (!exists) messages.value.push(msg)
            } catch (e) { /* ignore */ }
          })
        },
        onDisconnect: () => {
          connected.value = false
        },
        onStompError: (frame) => {
          console.error('STOMP error:', frame.headers['message'])
          connected.value = false
          connecting.value = false
        },
        onWebSocketError: (e) => {
          console.error('WebSocket error:', e.message || e)
          connected.value = false
          connecting.value = false
        }
      })
      stompClient.activate()
    }).catch(e => {
      console.error('Failed to load STOMP client:', e.message)
      connecting.value = false
    })
  }

  const sendMessage = (msg) => {
    if (!stompClient || !stompClient.connected) {
      console.warn('STOMP not connected')
      return false
    }
    stompClient.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(msg)
    })
    return true
  }

  const disconnect = () => {
    if (stompClient) {
      try { stompClient.deactivate() } catch (e) { /* ignore */ }
      stompClient = null
    }
    connected.value = false
    connecting.value = false
    messages.value = []
  }

  const setMessages = (list) => {
    messages.value = list || []
  }

  onUnmounted(() => {
    disconnect()
  })

  return { messages, connected, connecting, sendMessage, connect, disconnect, setMessages }
}
