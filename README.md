# 校园二手交易平台

一个采用前后端分离架构的全栈 Web 应用，为在校学生提供安全、便捷、可靠的闲置物品交易服务。

## 技术栈

### 前端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue 3** | 3.5 | 前端核心框架，采用 Composition API + `<script setup>` 语法糖编写 |
| **Vite** | 8 | 下一代前端构建工具，基于原生 ES 模块实现极速开发服务器和高效打包 |
| **Vue Router** | 4.6 | Vue 官方路由管理器，实现 SPA 页面导航与路由守卫 |
| **Element Plus** | 2.13 | 国产企业级 UI 组件库，提供按钮、表格、对话框、头像、消息等 80+ 组件 |
| **Axios** | 1.15 | 基于 Promise 的 HTTP 客户端，用于与后端 REST API 通信 |
| **@stomp/stompjs** | — | STOMP 客户端，通过原生 WebSocket 与后端实时通信 |

**核心技术特性：**

- **Composition API + `<script setup>`**：所有组件采用 Vue 3 推荐的组合式 API 写法，代码更简洁，逻辑复用更方便
- **路由懒加载**：所有页面组件通过 `() => import(...)` 动态导入，首屏只加载必要代码
- **导航守卫**：`router.beforeEach` 拦截路由，未登录用户访问私有页面自动跳转登录页，支持登录后回跳原页面
- **深色模式**：通过 Element Plus 官方暗黑主题 CSS 变量实现，一键切换亮色/暗色，偏好设置持久化到 localStorage
- **响应式布局**：使用 Element Plus 的 24 栏栅格系统，适配不同屏幕尺寸
- **用户状态响应式同步**：通过 `ref` + `watch(route)` 监听路由变化，登录/登出/切换账号后 Header 即时更新

### 后端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 4.0 | Java 企业级微服务框架，提供自动配置、嵌入式服务器和依赖管理 |
| **MyBatis** | 3.x | 轻量级持久层框架，通过注解直接编写 SQL，灵活控制查询逻辑 |
| **PageHelper** | 2.1 | MyBatis 分页插件，自动拦截 SQL 并在底层追加 LIMIT 和 COUNT 语句 |
| **Spring WebSocket** | — | 基于 STOMP 协议的实时消息推送，用于买卖双方在线聊天 |
| **SQL Server** | — | 微软关系型数据库，通过 JDBC 驱动连接 |

**核心技术特性：**

- **三层架构**：Controller（接口层）→ Service（业务逻辑层）→ Mapper（数据访问层），职责清晰
- **CORS 跨域**：通过 `@CrossOrigin` 注解允许前端（5173 端口）跨域访问后端（8080 端口）
- **文件上传**：`MultipartFile` 接收图片，UUID 重命名防止文件名冲突，静态资源映射提供访问 URL
- **文件大小限制**：商品图和头像统一限制 5MB（前端 `before-upload` + 后端 `file.getSize()` 双重校验）
- **安全处理**：返回用户数据前擦除密码字段，防止敏感信息泄露到前端
- **WebSocket 双端点**：同时提供 SockJS（`/ws`）和原生 WebSocket（`/ws-native`）两种连接方式

### 项目整体架构

```
┌──────────────────────────────────────────────────────────┐
│                     浏览器 (Browser)                       │
│                http://localhost:5173                      │
└──────┬────────────────────────────┬──────────────────────┘
       │                            │
       │  HTTP REST                  │  WebSocket (STOMP)
       │                            │  ws://localhost:8080/ws-native
┌──────▼──────────┐        ┌────────▼──────────┐
│   Vue 3 前端     │        │   Spring Boot     │
│   (Vite 构建)    │◄──────►│   后端 (Maven)     │
│   port: 5173     │        │   port: 8080      │
└─────────────────┘        └────────┬──────────┘
                                    │
                            ┌───────▼──────────┐
                            │   SQL Server     │
                            │   port: 1433     │
                            └──────────────────┘
```

## 项目结构

```
Full-stack-project/
│
├── second-hand-trading/               # Vue 3 前端项目
│   ├── src/
│   │   ├── views/
│   │   │   ├── public/                # 公开页面（无需登录）
│   │   │   │   ├── Home.vue           # 首页：分类商品展示、分页
│   │   │   │   ├── Detail.vue         # 商品详情：图片放大预览、状态标签、
│   │   │   │   │                      #   卖家信息、购买/收藏/联系卖家/撤销上架
│   │   │   │   ├── Search.vue         # 搜索结果：关键词查询、模糊匹配
│   │   │   │   ├── Login.vue          # 登录：Canvas 验证码、登出自动清空表单
│   │   │   │   ├── Register.vue       # 注册：表单校验、登出自动清空表单
│   │   │   │   └── About.vue          # 帮助中心：防骗指南 + 常见问题
│   │   │   ├── private/               # 私有页面（需登录）
│   │   │   │   ├── UserCenter.vue     # 个人中心：头像/昵称/密码修改、侧边栏、子路由
│   │   │   │   ├── MyPublished.vue    # 我发布的商品：表格展示、点击行跳转详情
│   │   │   │   ├── MyOrders.vue       # 我买到的宝贝：交易记录、点击行跳转详情
│   │   │   │   ├── MyFavorites.vue    # 我的收藏：卡片网格、取消收藏
│   │   │   │   ├── MyChats.vue        # 我的消息：会话列表、点击打开聊天窗口
│   │   │   │   └── Publish.vue        # 发布商品：图片上传(≤5MB)、分类选择
│   │   │   └── system/
│   │   │       └── NotFound.vue       # 404 页面
│   │   ├── components/
│   │   │   ├── ProductCard.vue        # 商品卡片：图片、价格、收藏星标、购买
│   │   │   └── ChatWindow.vue         # 浮动聊天窗口：实时消息、历史记录、气泡样式
│   │   ├── composables/
│   │   │   └── useChat.js             # WebSocket/STOMP 连接管理组合式函数
│   │   ├── router/
│   │   │   └── index.js               # 路由配置 + 全局导航守卫
│   │   ├── App.vue                    # 根组件：导航栏、头像+用户名、搜索、主题切换
│   │   ├── main.js                    # 应用入口：挂载路由/ElementPlus、暗黑主题、路由验证
│   │   └── style.css                  # 全局样式：滚动条美化、深色模式过渡
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
├── secondHandTrading/                 # Spring Boot 后端项目
│   └── src/main/
│       ├── java/com/example/secondHandTrading/
│       │   ├── entity/
│       │   │   ├── User.java          # 用户：id, username, password, schoolId, phone, avatar
│       │   │   ├── Product.java       # 商品：id, title, price, description, status, category, sellerId, buyerId
│       │   │   ├── ProductImage.java  # 商品图片：id, productId, imageUrl, sortOrder
│       │   │   ├── Favorite.java      # 收藏：id, userId, productId
│       │   │   └── ChatMessage.java   # 聊天消息：id, productId, senderId, receiverId, content, createTime
│       │   ├── mapper/
│       │   │   ├── UserMapper.java    # 用户：注册/登录/查询/改头像/改名/改密
│       │   │   ├── ProductMapper.java # 商品：发布/购买/搜索/分页/下架
│       │   │   ├── ProductImageMapper.java  # 商品图片：批量插入/按商品查询
│       │   │   ├── FavoriteMapper.java      # 收藏：添加/删除/列表/去重
│       │   │   └── ChatMessageMapper.java   # 聊天：插入/查对话/查会话/标记已读
│       │   ├── service/
│       │   │   ├── UserService.java   # 用户：密码校验、用户名去重、敏感信息擦除
│       │   │   ├── ProductService.java # 商品：事务发布、分页搜索
│       │   │   ├── FavoriteService.java # 收藏：防重复添加
│       │   │   └── ChatService.java   # 聊天：保存消息、会话分组（含用户和商品信息）
│       │   ├── controller/
│       │   │   ├── UserController.java     # /api/users/*
│       │   │   ├── ProductController.java  # /api/products/*
│       │   │   ├── FileController.java     # /api/files/upload (≤5MB限制)
│       │   │   ├── FavoriteController.java # /api/favorites/*
│       │   │   ├── ChatController.java     # /api/chat/* (REST接口)
│       │   │   └── ChatWebSocketController.java  # STOMP /app/chat.send
│       │   └── config/
│       │       ├── WebConfig.java     # /uploads/** 静态资源映射
│       │       └── WebSocketConfig.java  # STOMP端点 + 消息代理配置
│       └── resources/
│           └── application.properties # 数据源 + MyBatis + 文件上传大小
│
└── SQLquery/
    ├── updata.sql                     # 建表语句 + 字段迁移
    ├── select.sql                     # 调试查询语句
    └── test-data.sql                  # 演示测试数据（5用户+13商品+收藏+聊天记录）
```

## 数据库设计

```
                          ┌──────────────────────┐
                          │       [User]         │
                          ├──────────────────────┤
                          │ id (PK, IDENTITY)    │
                          │ username              │
                          │ password              │
                          │ school_id             │
                          │ phone                 │
                          │ avatar                │
                          └──────┬───────────────┘
                                 │
                    ┌────────────┼────────────────────┐
                    │ seller_id  │                    │ buyer_id
                    ▼            │                    ▼
          ┌──────────────┐      │    ┌──────────────┐     ┌──────────────────┐
          │   Product    │      │    │   Favorite   │     │   ChatMessage    │
          ├──────────────┤      │    ├──────────────┤     ├──────────────────┤
          │ id (PK)      │      │    │ id (PK)      │     │ id (PK)          │
          │ title        │      │    │ user_id (FK) │     │ product_id (FK)  │
          │ price        │      │    │ product_id   │     │ sender_id (FK)   │
          │ description  │      │    │    (FK)      │     │ receiver_id (FK) │
          │ status       │      │    └──────────────┘     │ content          │
          │ category     │      │                         │ create_time      │
          │ seller_id(FK)├──────┘                         │ is_read          │
          │ buyer_id (FK)│                                └──────────────────┘
          └──────┬───────┘
                 │ 1
                 ▼
     ┌───────────────────┐
     │   ProductImage    │
     ├───────────────────┤
     │ id (PK)           │
     │ product_id (FK)   │
     │ image_url         │
     │ sort_order        │
     └───────────────────┘
```

### 关键字段说明

- **Product.status**：`0` = 待售，`1` = 已售出，`-1` = 已下架
- **Product.category**：中文分类（数码3C、书籍资料、生活用品、美妆服饰、其他闲置）
- **User.avatar**：头像图片的完整 URL，为空时显示用户名首字母
- **Favorite**：防重复添加，通过 `(user_id, product_id)` 组合判断
- **ChatMessage.is_read**：`0` = 未读，`1` = 已读
- **ChatMessage.create_time**：SQL Server 自动填充 `GETDATE()`

## 功能详细介绍

### 1. 用户认证系统

- **注册流程**：填写用户名、手机号、学号、密码 → 前端表单校验（两次密码一致、手机号格式、学号格式）→ 后端查重（用户名唯一）→ 写入数据库
- **登录流程**：输入用户名密码 → 前端 Canvas 绘制验证码（随机 4 位字符 + 干扰线）→ 验证码校验 → 后端明文密码比对 → 成功后将用户信息（不含密码）存入 localStorage
- **登录态维持**：App.vue 通过 `ref` + `watch(route.fullPath)` 监听路由变化，实时读取 localStorage 刷新用户状态，确保切换账号后 Header 即时更新
- **退出登录**：二次确认弹窗 → 清除 localStorage → 跳转登录页
- **表单重置**：登出后再次进入登录/注册页，表单自动清空（`watch(route.path)` 触发 `resetForm()`）

### 2. 个人中心

- **头像管理**：点击头像 → 弹出文件选择器 → 上传到 `/api/files/upload`（限制 5MB）→ 返回 URL → 调用 `/api/users/avatar` 保存 → 刷新本地用户信息
- **昵称修改**：弹窗输入新名称 → 后端查重（排除自身）→ 更新数据库 → 同步 localStorage
- **密码修改**：旧密码 + 新密码 + 确认 → 前端校验一致 → 后端校验旧密码 → 清空登录态重新登录
- **导航栏用户标识**：Header 中显示用户头像 + 用户名（替代原始"个人中心"按钮），点击进入个人中心
- **退出登录**：按钮位于侧边栏底部，带二次确认

### 3. 商品发布与展示

- **发布流程**：标题 → 上传图片（最多 5 张，UUID 重命名，单张 ≤5MB）→ 选择分类 → 价格 → 描述 → 后端事务写入（商品 + 图片批量插入）
- **首页展示**：分类标签页切换 → PageHelper 分页（每页 8 条）→ `el-row/el-col` 栅格渲染 ProductCard
- **商品搜索**：导航栏胶囊搜索框 → `/search?keyword=xxx` → SQL `LIKE` 模糊匹配
- **图片放大预览**：详情页使用 `el-image` 的 `preview-src-list`，点击任意图片进入全屏预览模式，支持缩放、旋转、左右切换
- **深色模式 Banner**：浅色模式为绿蓝渐变，暗色模式自动切换为深绿深蓝渐变，通过 `MutationObserver` 监听实时生效

### 4. 商品交易

- **购买流程**："立即购买"→ 确认对话框 → 后端更新 status=1 + 记录 buyerId → 前端即时更新 UI
- **防自购**：前后端均校验买家不能是卖家本人
- **状态标签**：详情页显示分类标签 + "我的发布"（卖家标识）+ "待售中"/"已售出"/"已下架"状态标签
- **撤销上架**：卖家在详情页可点击"撤销上架"按钮 → 确认后调用 `DELETE /api/products/delete/{id}` → status 变为 -1 → 页面即时更新

### 5. 收藏系统

- **快捷收藏**：商品卡片右上角星形按钮 → 点击收藏/取消 → 图标即时切换（空心↔实心）→ 后端异步操作
- **收藏状态同步**：Home/Search 页面初始化时批量查询已收藏 ID 列表，ProductCard 通过 `watch` 监听 `favoritedIds` 实时高亮
- **收藏管理**：个人中心"我的收藏"→ 卡片网格 → "取消收藏"移除 → 支持点击跳转商品详情
- **详情页收藏**：自动检测收藏状态，按钮文字和图标动态切换

### 6. 实时聊天系统（WebSocket）

- **连接方式**：前端使用 `@stomp/stompjs` + 原生 WebSocket 连接到 `/ws-native` 端点，不依赖 SockJS
- **消息流程**：买家在详情页点击"联系卖家"→ 右下角弹出 ChatWindow → 自动加载历史消息 → 输入消息 → STOMP 发送到 `/app/chat.send` → 后端保存到数据库 → 广播给双方
- **会话列表**：个人中心"我的消息"→ 展示所有会话（对方头像、商品标题、最后消息预览、时间）→ 点击打开聊天
- **实时推送**：消息通过 `/topic/chat.{userId}` 通道实时推送给接收方，无需手动刷新
- **消息持久化**：所有消息保存到 `ChatMessage` 表，包含商品ID、发送者、接收者、内容、时间、已读状态
- **会话分组**：后端 ChatService 按 `(product_id, other_user)` 去重，返回每个会话的最新一条消息及关联的用户和商品信息

### 7. 深色模式

- **实现原理**：Element Plus 暗黑主题 CSS 变量文件，`<html>` 添加 `dark` class 激活
- **切换方式**：导航栏右侧太阳/月亮图标按钮，带旋转缩放过渡动画，偏好保存至 localStorage
- **主题适配**：所有硬编码颜色替换为 `var(--el-*)` CSS 变量，深色模式下视觉和谐
- **实时响应**：Home.vue 使用 `MutationObserver` 监听 class 变化，Banner 渐变色即时切换

### 8. 帮助与防骗

- **防骗指南**：6 条安全提示（低价陷阱、校内面交、身份核实、实物验货、隐私保护、举报渠道）
- **常见问题**：5 个高频问题（如何发布、联系卖家、修改信息、收藏管理、聊天功能）
- **平台信息**：右侧边栏展示平台优势和联系方式

### 9. 交互体验优化

- **表格行点击**：个人中心"我发布的"和"我买到的"表格行可直接点击跳转到商品详情页
- **导航栏美化**：粘性定位 + Logo 图标 + 胶囊搜索框 + 用户头像/名称 + 主题切换按钮
- **路由切换动画**：个人中子页面切换带 fade-slide 过渡效果
- **404 页面**：渐变色大号"404"+ 返回首页/上一页按钮

## API 接口文档

### 用户模块 — `/api/users`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/register` | `RequestBody: User` | `"success"` / `"exists"` | 注册，检查用户名唯一 |
| POST | `/login` | `RequestBody: {username, password}` | `User`(无密码) / `null` | 登录校验 |
| GET | `/info/{id}` | `PathVariable: id` | `User`(无密码) | 获取用户公开信息 |
| PUT | `/avatar` | `RequestBody: {id, avatarUrl}` | `"success"` / `"fail"` | 更新头像 URL |
| PUT | `/username` | `RequestBody: {id, username}` | `"success"` / `"exists"` | 修改昵称 |
| PUT | `/password` | `RequestBody: {id, oldPassword, newPassword}` | `"success"` / `"wrong_password"` | 修改密码 |
| POST | `/avatar/upload` | `Multipart: file` (≤5MB) | URL / `"error"` / `"size_exceeded"` | 上传头像 |
| GET | `/refresh/{id}` | `PathVariable: id` | `User`(无密码) | 获取最新用户信息 |

### 商品模块 — `/api/products`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| GET | `/page` | `Params: pageNum, pageSize, category` | `PageInfo: {list, total}` | 分页+分类查询 |
| GET | `/detail/{id}` | `PathVariable: id` | `Product`(含图片) | 商品详情 |
| GET | `/search` | `Params: keyword` | `List<Product>` | 模糊搜索 |
| POST | `/add` | `RequestBody: Product` | `"success"` / `"fail"` | 发布商品(事务) |
| POST | `/buy/{id}` | `Params: buyerId` | `"success"` / `"fail"` | 购买商品 |
| DELETE | `/delete/{id}` | `Params: userId` | `"success"` | 下架(status=-1) |
| GET | `/my-published` | `Params: userId` | `List<Product>` | 我发布的 |
| GET | `/my-bought` | `Params: userId` | `List<Product>` | 我买到的 |

### 收藏模块 — `/api/favorites`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/add` | `Params: userId, productId` | `"success"` / `"already"` | 添加收藏 |
| DELETE | `/remove` | `Params: userId, productId` | `"success"` / `"fail"` | 取消收藏 |
| GET | `/check` | `Params: userId, productId` | `boolean` | 检查收藏状态 |
| GET | `/list` | `Params: userId` | `List<Product>` | 收藏列表(含商品详情) |
| GET | `/ids` | `Params: userId` | `List<Integer>` | 已收藏商品ID |

### 聊天模块 — `/api/chat`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| GET | `/history` | `Params: productId, userId1, userId2` | `List<ChatMessage>` | 两人对话历史 |
| GET | `/conversations` | `Params: userId` | `List<Map>` | 会话列表(含用户/商品信息) |
| POST | `/read` | `RequestBody: {productId, receiverId, senderId}` | `"success"` | 标记已读 |
| GET | `/unread` | `Params: userId` | `int` | 未读消息数 |

### WebSocket 端点

| 端点 | 协议 | 说明 |
|------|------|------|
| `/ws` | STOMP + SockJS | SockJS 兼容模式 |
| `/ws-native` | STOMP + 原生 WebSocket | 推荐使用（Vite 兼容） |

| 目标地址 | 方向 | 说明 |
|----------|------|------|
| `/app/chat.send` | 客户端→服务端 | 发送消息 |
| `/topic/chat.{userId}` | 服务端→客户端 | 接收实时消息 |

### 文件模块 — `/api/files`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/upload` | `Multipart: file` (≤5MB) | URL / `"error"` / `"size_exceeded"` | 上传图片，UUID命名 |

## 快速开始

### 环境要求

| 软件 | 最低版本 | 说明 |
|------|----------|------|
| Node.js | 18+ | 前端运行环境 |
| Java JDK | 21 | 后端编译运行 |
| Maven | 3.6+ | 后端依赖管理 |
| SQL Server | 2019+ | 数据库 |

### 1. 初始化数据库

```sql
CREATE DATABASE second_hand_trading;
```

执行建表脚本：

```bash
sqlcmd -S localhost -U sa -P 你的密码 -d second_hand_trading -i SQLquery/updata.sql
```

或使用 SSMS 打开 `SQLquery/updata.sql` 执行。

### 2. 配置后端

编辑 `secondHandTrading/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=second_hand_trading;encrypt=false;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=你的数据库密码
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
mybatis.configuration.map-underscore-to-camel-case=true
pagehelper.helperDialect=sqlserver
pagehelper.reasonable=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=25MB
```

### 3. 启动后端

```bash
cd secondHandTrading
.\mvnw.cmd spring-boot:run     # Windows
./mvnw spring-boot:run         # Linux / macOS
```

后端运行在 `http://localhost:8080`。

### 4. 启动前端

```bash
cd second-hand-trading
npm install
npm run dev
```

前端运行在 `http://localhost:5173`。

### 5. 导入测试数据（可选）

执行 `SQLquery/test-data.sql` 导入 5 个用户、13 件商品、收藏和聊天记录。

测试账号密码均为 `123456`。

## 设计模式与最佳实践

### 前端

- **关注点分离**：视图组件（`views/`）与复用组件（`components/`）独立管理
- **组合式函数**：WebSocket 连接管理封装为 `useChat()` composable，ChatWindow 和 MyChats 复用同一逻辑
- **路由级权限控制**：`meta.requiresAuth` + `beforeEach` 守卫
- **响应式用户状态**：`ref` + `watch(route.fullPath)` 替代 `computed(localStorage)`，确保切换账号后 UI 即时更新
- **表单重置**：`watch(route.path)` 监听路由进入，自动清空登录/注册表单
- **图片上传校验**：前端 `before-upload` 拦截 + 后端 `file.getSize()` 双重限制 5MB

### 后端

- **经典三层架构**：Controller → Service → Mapper
- **RESTful API**：URL 名词复数，HTTP 方法表达语义
- **事务保护**：商品发布 `@Transactional` 保证 Product + ProductImage 原子写入
- **敏感信息保护**：Service 层返回前擦除 password 字段
- **WebSocket 双端点**：SockJS（`/ws`）+ 原生 WebSocket（`/ws-native`）兼容不同客户端
- **会话分组**：ChatService 在 Java 层做去重分组，避免复杂 SQL，同时注入 UserMapper/ProductMapper 丰富返回数据

## 待优化项

- [ ] 密码加密存储（BCrypt 替代明文）
- [ ] Token 认证（JWT 替代 localStorage 直存用户对象）
- [ ] 前端状态管理（Pinia 统一管理用户、收藏、未读消息）
- [ ] 图片存储上云（OSS/CDN 替代本地文件系统）
- [ ] 商品评价系统（交易完成后互评）
- [ ] 消息推送通知（浏览器 Notification API）

## 许可证

本项目仅用于学习交流目的。
