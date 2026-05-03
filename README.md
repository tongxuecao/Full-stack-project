# 校园二手交易平台

一个采用前后端分离架构的全栈 Web 应用，为在校学生提供安全、便捷、可靠的闲置物品交易服务。

## 技术栈

### 前端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue 3** | 3.5 | 前端核心框架，采用 Composition API + `<script setup>` 语法糖编写 |
| **Vite** | 8 | 下一代前端构建工具，基于原生 ES 模块实现极速开发服务器和高效打包 |
| **Vue Router** | 4.6 | Vue 官方路由管理器，实现 SPA 页面导航与路由守卫 |
| **Element Plus** | 2.13 | 国产企业级 UI 组件库，提供按钮、表格、对话框等 80+ 高质量组件 |
| **Axios** | 1.15 | 基于 Promise 的 HTTP 客户端，用于与后端 REST API 通信 |

**核心技术特性：**

- **Composition API + `<script setup>`**：所有组件采用 Vue 3 推荐的组合式 API 写法，代码更简洁，逻辑复用更方便
- **路由懒加载**：所有页面组件通过 `() => import(...)` 动态导入，首屏只加载必要代码
- **导航守卫**：`router.beforeEach` 拦截路由，未登录用户访问私有页面自动跳转登录页，支持登录后回跳原页面
- **深色模式**：通过 Element Plus 官方暗黑主题 CSS 变量实现，一键切换亮色/暗色，偏好设置持久化到 localStorage
- **响应式布局**：使用 Element Plus 的 24 栏栅格系统，适配不同屏幕尺寸

### 后端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 4.0 | Java 企业级微服务框架，提供自动配置、嵌入式服务器和依赖管理 |
| **MyBatis** | 3.x | 轻量级持久层框架，通过注解直接编写 SQL，灵活控制查询逻辑 |
| **PageHelper** | 2.1 | MyBatis 分页插件，自动拦截 SQL 并在底层追加 LIMIT 和 COUNT 语句 |
| **SQL Server** | — | 微软关系型数据库，通过 JDBC 驱动连接 |

**核心技术特性：**

- **三层架构**：Controller（接口层）→ Service（业务逻辑层）→ Mapper（数据访问层），职责清晰
- **CORS 跨域**：通过 `@CrossOrigin` 注解允许前端（5173 端口）跨域访问后端（8080 端口）
- **文件上传**：`MultipartFile` 接收图片，UUID 重命名防止文件名冲突，静态资源映射提供访问 URL
- **安全处理**：返回用户数据前擦除密码字段，防止敏感信息泄露到前端

### 项目整体架构

```
┌─────────────────────────────────────────────────────┐
│                   浏览器 (Browser)                    │
│              http://localhost:5173                   │
└─────────────┬───────────────────────────┬───────────┘
              │                           │
     ┌────────▼────────┐          ┌───────▼────────┐
     │   Vue 3 前端     │   HTTP   │  Spring Boot   │
     │   (Vite 构建)    │◄────────►│  后端 (Maven)   │
     │   port: 5173     │   REST   │   port: 8080    │
     └─────────────────┘          └───────┬─────────┘
                                          │
                                  ┌───────▼─────────┐
                                  │   SQL Server    │
                                  │   port: 1433     │
                                  │   Database:      │
                                  │   second_hand_   │
                                  │   trading        │
                                  └─────────────────┘
```

## 项目结构

```
Full-stack-project/
│
├── second-hand-trading/               # Vue 3 前端项目
│   ├── src/
│   │   ├── views/
│   │   │   ├── public/                # 公开页面（无需登录）
│   │   │   │   ├── Home.vue           # 首页：分类商品展示、分页浏览
│   │   │   │   ├── Detail.vue         # 商品详情：图片轮播、卖家信息、收藏购买
│   │   │   │   ├── Search.vue         # 搜索结果：关键词查询、模糊匹配
│   │   │   │   ├── Login.vue          # 登录：Canvas 图形验证码、表单校验
│   │   │   │   ├── Register.vue       # 注册：学号校验、重复用户名检测
│   │   │   │   └── About.vue          # 帮助中心：防骗指南 + 常见问题
│   │   │   ├── private/               # 私有页面（需登录）
│   │   │   │   ├── UserCenter.vue     # 个人中心布局：侧边栏 + 子路由
│   │   │   │   ├── MyPublished.vue    # 我发布的商品：表格展示、下架操作
│   │   │   │   ├── MyOrders.vue       # 我买到的宝贝：交易记录
│   │   │   │   ├── MyFavorites.vue    # 我的收藏：卡片网格、取消收藏
│   │   │   │   └── Publish.vue        # 发布商品：图片上传、分类选择
│   │   │   └── system/
│   │   │       └── NotFound.vue       # 404 页面
│   │   ├── components/
│   │   │   └── ProductCard.vue        # 商品卡片组件：图片、价格、收藏、购买
│   │   ├── router/
│   │   │   └── index.js               # 路由配置 + 全局导航守卫
│   │   ├── App.vue                    # 根组件：顶部导航栏、搜索、主题切换
│   │   ├── main.js                    # 应用入口：挂载插件、导入暗黑主题
│   │   └── style.css                  # 全局样式：滚动条、深色模式过渡
│   ├── index.html                     # HTML 入口
│   ├── vite.config.js                 # Vite 构建配置
│   └── package.json                   # 依赖与脚本
│
├── secondHandTrading/                 # Spring Boot 后端项目
│   └── src/main/
│       ├── java/com/example/secondHandTrading/
│       │   ├── entity/                # 数据实体（POJO）
│       │   │   ├── User.java          # 用户：id, username, password, schoolId, phone, avatar
│       │   │   ├── Product.java       # 商品：id, title, price, description, status, category, sellerId, buyerId
│       │   │   ├── ProductImage.java  # 商品图片：id, productId, imageUrl, sortOrder
│       │   │   └── Favorite.java      # 收藏：id, userId, productId
│       │   ├── mapper/                # MyBatis 数据访问层（注解式 SQL）
│       │   │   ├── UserMapper.java    # 用户 CRUD：注册/登录/查询/更新头像/改名/改密
│       │   │   ├── ProductMapper.java # 商品 CRUD：发布/购买/搜索/分页/下架
│       │   │   ├── ProductImageMapper.java  # 商品图片：批量插入/按商品查询
│       │   │   └── FavoriteMapper.java      # 收藏管理：添加/删除/列表/去重检查
│       │   ├── service/               # 业务逻辑层
│       │   │   ├── UserService.java   # 用户服务：密码校验、用户名去重、敏感信息擦除
│       │   │   ├── ProductService.java # 商品服务：事务性发布、分页搜索
│       │   │   └── FavoriteService.java # 收藏服务：防重复添加
│       │   ├── controller/            # REST API 控制器
│       │   │   ├── UserController.java     # /api/users/*
│       │   │   ├── ProductController.java  # /api/products/*
│       │   │   ├── FileController.java     # /api/files/upload
│       │   │   └── FavoriteController.java # /api/favorites/*
│       │   └── config/
│       │       └── WebConfig.java     # 静态资源映射：/uploads/** → 本地文件系统
│       └── resources/
│           └── application.properties # 数据源配置、MyBatis 配置
│
└── SQLquery/                          # 数据库脚本
    ├── updata.sql                     # 建表语句 + 字段迁移记录
    └── select.sql                     # 调试查询语句
```

## 数据库设计

### 表结构概览

```
                          ┌──────────────────────┐
                          │       [User]         │
                          ├──────────────────────┤
                          │ id (PK, IDENTITY)    │
                          │ username (UNIQUE)     │
                          │ password             │
                          │ school_id            │
                          │ phone                │
                          │ avatar               │
                          └──────┬───────────────┘
                                 │ 1
                                 │
                    ┌────────────┼────────────┐
                    │ seller_id  │            │ buyer_id
                    ▼            │            ▼
          ┌──────────────┐      │    ┌──────────────┐
          │   Product    │      │    │   Favorite   │
          ├──────────────┤      │    ├──────────────┤
          │ id (PK)      │      │    │ id (PK)      │
          │ title        │      │    │ user_id (FK) │
          │ price        │      │    │ product_id   │
          │ description  │      │    │    (FK)      │
          │ status       │      │    └──────────────┘
          │ category     │      │
          │ seller_id(FK)├──────┘
          │ buyer_id (FK)│
          └──────┬───────┘
                 │ 1
                 │
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
- **Product.category**：中文分类名称（数码3C、书籍资料、生活用品、美妆服饰、其他闲置）
- **User.avatar**：头像图片的完整 URL，为空时显示用户名首字母
- **Favorite**：防重复添加，通过 `(user_id, product_id)` 唯一组合判断

## 功能详细介绍

### 1. 用户认证系统

- **注册流程**：填写用户名、手机号、学号、密码 → 前端表单校验（两次密码一致、必填项非空）→ 后端查重（用户名唯一）→ 写入数据库
- **登录流程**：输入用户名密码 → 前端 Canvas 绘制验证码（随机 4 位字符 + 干扰线）→ 验证码校验 → 后端明文密码比对 → 成功后将用户信息（不含密码）存入 localStorage
- **登录态维持**：通过 `localStorage.getItem('user')` 判断登录状态，App.vue 和路由守卫均依赖此判断
- **Canvas 验证码**：在 `<canvas>` 上动态绘制随机字符串和干扰线，点击刷新，增加自动化攻击门槛

### 2. 个人中心

- **头像管理**：点击头像触发隐藏的 `<input type="file">` → 上传图片到 `/api/files/upload` → 获取返回 URL → 调用 `/api/users/avatar` 保存 → 刷新本地用户信息
- **昵称修改**：弹窗输入新名称 → 后端查重（排除自身）→ 更新数据库 → 同步 localStorage
- **密码修改**：输入旧密码 + 新密码 + 确认新密码 → 前端校验一致性和非空 → 后端校验旧密码正确 → 更新密码 → 清空登录态重新登录
- **退出登录**：二次确认弹窗 → 清除 localStorage → 跳转登录页

### 3. 商品发布与展示

- **发布流程**：填写标题、选择分类、上传图片（最多 5 张，UUID 重命名）、设置价格、编写描述 → 后端开启事务（插入商品记录 + 批量插入图片记录）
- **首页展示**：分类标签页切换 → 后端 PageHelper 分页查询（每页 8 条）→ 前端 `el-row/el-col` 栅格布局渲染 ProductCard 组件
- **商品搜索**：导航栏搜索框输入关键词 → 跳转 `/search?keyword=xxx` → 后端 SQL `LIKE` 模糊匹配标题和描述

### 4. 商品交易

- **购买流程**：点击"立即购买"→ 确认对话框 → 后端更新商品状态为已售出（status=1）→ 记录 buyerId → 前端即时更新 UI
- **防自购**：后端和前端均校验买家不能是卖家本人
- **状态展示**：已售出商品显示灰色遮罩 + "已售出"文字，按钮变为不可点击

### 5. 收藏系统

- **快捷收藏**：商品卡片右上角星形按钮 → 点击收藏/取消 → 图标即时切换（空心↔实心）→ 通知后端 → Home/Search 页面初始化时批量查询已收藏 ID 列表
- **收藏管理**：个人中心"我的收藏"页面 → 卡片网格展示 → 点击"取消收藏"移除 → 支持跳转商品详情
- **详情页收藏**：商品详情页查看时自动检测收藏状态，按钮文字和图标动态切换

### 6. 深色模式

- **实现原理**：Element Plus 提供暗黑主题 CSS 变量文件（`element-plus/theme-chalk/dark/css-vars.css`），通过在 `<html>` 元素添加 `dark` class 激活
- **切换方式**：导航栏右侧太阳/月亮图标按钮 → 旋转缩放过渡动画 → 偏好保存至 localStorage
- **主题适配**：所有硬编码颜色（`#fff`、`#303133` 等）替换为 Element Plus CSS 变量（`var(--el-bg-color)`、`var(--el-text-color-primary)` 等），确保深色模式下视觉和谐
- **实时响应**：Home.vue 使用 `MutationObserver` 监听 `<html>` 的 class 变化，banner 渐变色即时切换

### 7. 帮助与防骗

- **防骗指南**：6 条安全提示（低价陷阱、校内面交、身份核实、实物验货、隐私保护、举报渠道），折叠面板形式呈现
- **常见问题**：5 个高频问题（如何发布、联系卖家、交易流程、修改信息、收藏管理）
- **平台信息**：右侧边栏展示平台优势和联系方式

## API 接口文档

### 用户模块 — `/api/users`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/register` | `RequestBody: User` | `"success"` / `"exists"` / `"fail"` | 注册新用户，检查用户名唯一性 |
| POST | `/login` | `RequestBody: {username, password}` | `User` (无密码) / `null` | 登录校验，成功返回用户信息 |
| GET | `/info/{id}` | `PathVariable: id` | `User` (无密码) | 获取用户公开信息 |
| PUT | `/avatar` | `RequestBody: {id, avatarUrl}` | `"success"` / `"fail"` | 更新头像 URL |
| PUT | `/username` | `RequestBody: {id, username}` | `"success"` / `"exists"` / `"fail"` | 修改昵称，检查重复 |
| PUT | `/password` | `RequestBody: {id, oldPassword, newPassword}` | `"success"` / `"wrong_password"` / `"fail"` | 修改密码，校验旧密码 |
| POST | `/avatar/upload` | `Multipart: file` | `"http://...url"` / `"error"` | 上传头像图片，返回访问 URL |
| GET | `/refresh/{id}` | `PathVariable: id` | `User` (无密码) | 获取最新用户信息 |

### 商品模块 — `/api/products`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| GET | `/page` | `Params: pageNum, pageSize, category` | `PageInfo: {list, total}` | 分页 + 分类查询 |
| GET | `/detail/{id}` | `PathVariable: id` | `Product` (含图片) | 单商品详情 |
| GET | `/search` | `Params: keyword` | `List<Product>` | 标题/描述模糊搜索 |
| POST | `/add` | `RequestBody: Product` | `"success"` / `"fail"` | 发布商品（事务写入） |
| POST | `/buy/{id}` | `Params: buyerId` | `"success"` / `"fail"` | 购买商品 |
| DELETE | `/delete/{id}` | `Params: userId` | `"success"` | 逻辑下架（status=-1） |
| GET | `/my-published` | `Params: userId` | `List<Product>` | 我发布的商品列表 |
| GET | `/my-bought` | `Params: userId` | `List<Product>` | 我买到的商品列表 |

### 收藏模块 — `/api/favorites`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/add` | `Params: userId, productId` | `"success"` / `"already"` / `"fail"` | 添加收藏，防重复 |
| DELETE | `/remove` | `Params: userId, productId` | `"success"` / `"fail"` | 取消收藏 |
| GET | `/check` | `Params: userId, productId` | `boolean` | 检查是否已收藏 |
| GET | `/list` | `Params: userId` | `List<Product>` | 收藏商品列表（含详情） |
| GET | `/ids` | `Params: userId` | `List<Integer>` | 已收藏商品 ID 列表 |

### 文件模块 — `/api/files`

| 方法 | 路径 | 请求参数 | 返回 | 说明 |
|------|------|----------|------|------|
| POST | `/upload` | `Multipart: file` | URL 字符串 / `"error"` | 上传图片，UUID 命名 |

## 快速开始

### 环境要求

| 软件 | 最低版本 | 说明 |
|------|----------|------|
| Node.js | 18+ | 前端运行环境 |
| Java JDK | 21 | 后端编译运行 |
| Maven | 3.6+ | 后端依赖管理 |
| SQL Server | 2019+ | 数据库（也可使用 Docker 部署） |

### 1. 初始化数据库

在 SQL Server 中创建数据库：

```sql
CREATE DATABASE second_hand_trading;
```

然后执行项目中的建表脚本：

```bash
sqlcmd -S localhost -U sa -P 你的密码 -d second_hand_trading -i SQLquery/updata.sql
```

或使用 SQL Server Management Studio (SSMS) 打开 `SQLquery/updata.sql` 执行。

### 2. 配置后端

编辑 `secondHandTrading/src/main/resources/application.properties`：

```properties
spring.application.name=secondHandTa
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=second_hand_trading;encrypt=false;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=你的数据库密码
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
mybatis.configuration.map-underscore-to-camel-case=true
pagehelper.helperDialect=sqlserver
pagehelper.reasonable=true
```

### 3. 启动后端

```bash
cd secondHandTrading

# Windows (PowerShell)
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

后端服务运行在 `http://localhost:8080`，上传的图片可通过 `http://localhost:8080/uploads/xxxx.png` 访问。

### 4. 启动前端

```bash
cd second-hand-trading
npm install
npm run dev
```

前端开发服务器运行在 `http://localhost:5173`，API 请求自动代理到后端。

### 5. 验证

浏览器打开 `http://localhost:5173`，你应该看到：

1. 顶部导航栏（校园二手平台、首页、防骗指南、搜索框、登录按钮、主题切换）
2. 首页横幅（"发现校园好物"）
3. 分类标签页（全部闲置、数码3C 等）
4. 商品卡片网格

注册一个账号后即可体验完整功能。

## 设计模式与最佳实践

### 前端

- **关注点分离**：视图组件（`views/`）与复用组件（`components/`）独立管理
- **路由级权限控制**：通过 `meta.requiresAuth` 标记 + 全局 `beforeEach` 守卫实现
- **计算属性缓存**：`localStorage` 读取通过 `computed` 包装，避免每次渲染都解析 JSON
- **组合式函数思维**：相关逻辑（如收藏状态管理）集中在一个 `setup` 函数中，而非分散在多个选项

### 后端

- **经典三层架构**：Controller → Service → Mapper，层次分明，易于测试和维护
- **RESTful API 设计**：URL 使用名词复数（`/api/users`、`/api/products`），HTTP 方法表达操作语义
- **事务保护**：商品发布涉及 Product 表和 ProductImage 表同时写入，通过 `@Transactional` 保证原子性
- **敏感信息保护**：Service 层统一在返回前擦除 password 字段，避免视图层遗漏

## 待优化项

- [ ] 密码加密存储（BCrypt 替代明文存储）
- [ ] Token 认证机制（JWT 替代 localStorage 直接存用户对象）
- [ ] 前端状态管理（Pinia 统一管理用户状态、收藏列表）
- [ ] 真实图片存储（OSS/云存储替代本地文件系统）
- [ ] 站内消息通知（购买成功、收藏商品降价等）
- [ ] 商品评价系统（交易完成后双方互评）

## 许可证

本项目仅用于学习交流目的。
