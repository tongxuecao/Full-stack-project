-- ===================================================
-- 校园二手交易平台 — 演示测试数据
-- 在 SSMS 中执行本文件即可
--
-- 图片说明：
--   picsum.photos/id/{N} — 每张图固定不变，取自 Unsplash
--   如需替换真实商品照片：将下面的 URL 替换为你的图片地址
--   或通过平台"发布商品"页面上传真实照片
-- ===================================================

DELETE FROM ChatMessage;
DELETE FROM Favorite;
DELETE FROM ProductImage;
DELETE FROM Product;
DELETE FROM [User];

DBCC CHECKIDENT ('[User]', RESEED, 0);
DBCC CHECKIDENT ('Product', RESEED, 0);
DBCC CHECKIDENT ('ProductImage', RESEED, 0);
DBCC CHECKIDENT ('Favorite', RESEED, 0);
DBCC CHECKIDENT ('ChatMessage', RESEED, 0);

-- ===================================================
-- 用户 (密码均为 123456)
-- ===================================================
INSERT INTO [User] (username, password, phone, school_id, avatar) VALUES
(N'张三', '123456', '13800001001', '20210001', 'https://api.dicebear.com/7.x/initials/svg?seed=ZS&backgroundColor=409eff'),
(N'李四', '123456', '13800001002', '20210002', 'https://api.dicebear.com/7.x/initials/svg?seed=LS&backgroundColor=67c23a'),
(N'王五', '123456', '13800001003', '20210003', 'https://api.dicebear.com/7.x/initials/svg?seed=WW&backgroundColor=e6a23c'),
(N'赵六', '123456', '13800001004', '20210004', 'https://api.dicebear.com/7.x/initials/svg?seed=ZL&backgroundColor=f56c6c'),
(N'周七', '123456', '13800001005', '20210005', 'https://api.dicebear.com/7.x/initials/svg?seed=ZQ&backgroundColor=909399');

-- ===================================================
-- 商品 (status: 0=待售, 1=已售出)
-- ===================================================

-- ---- 数码3C ----
INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'9成新 iPhone 14 128G 星光色', 3200.00, N'去年10月购入，全程贴膜带壳使用，屏幕无划痕，电池健康90%，配件齐全，因换新机转手。校内面交验货。', 0, 1, NULL, N'数码3C');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'罗技 G502 电竞鼠标 用了半年', 180.00, N'京东自营原价399。DPI最高25600适合FPS游戏。换了无线款故出，包装齐全。', 0, 1, NULL, N'数码3C');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'索尼 WH-1000XM4 降噪耳机 黑色', 900.00, N'殿堂级降噪，出差必备。成色95新，耳罩无磨损。送收纳盒+3.5mm线。', 1, 2, 3, N'数码3C');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'机械革命 Code01 程序员笔记本', 2800.00, N'R7-6800H/16G/512G/2.5K屏。写代码跑虚拟机流畅。毕业换MacBook故出。', 0, 2, NULL, N'数码3C');

-- ---- 书籍资料 ----
INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'2025考研数学一 李永乐全套 9成新', 80.00, N'复习全书+660题+历年真题，铅笔笔记已擦除。送张宇36讲电子版。', 0, 1, NULL, N'书籍资料');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'新东方六级词汇 词根+联想记忆法', 15.00, N'基本全新乱序版，原价38元。买就送四六级真题电子版。', 0, 3, NULL, N'书籍资料');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'Python编程 从入门到实践 第3版', 35.00, N'计算机必修课教材，几乎全新。附赠源码和习题答案，适合零基础。', 1, 1, 4, N'书籍资料');

-- ---- 生活用品 ----
INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'飞利浦 LED 护眼台灯 三档调光', 65.00, N'三档亮度色温USB充电，不频闪不刺眼。换了米家智能灯故出。', 0, 2, NULL, N'生活用品');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'轻便折叠小风扇 USB充电', 25.00, N'三档风力超静音，充满电续航4-5小时，夏天图书馆自习神器。', 0, 3, NULL, N'生活用品');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'24寸变速山地自行车 八成新', 350.00, N'凤凰牌通勤用车，变速顺畅。轮胎去年刚换，送车锁+打气筒。', 0, 2, NULL, N'生活用品');

-- ---- 美妆服饰 ----
INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'Nike Air Force 1 纯白 42码', 380.00, N'专柜正品保真，买大了只穿两次，鞋底零磨损。支持任意平台鉴定。', 0, 4, NULL, N'美妆服饰');

INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'日系简约帆布斜挎包 全新', 45.00, N'朋友送的礼物风格不太适合。大容量可装iPad+水杯+雨伞。', 1, 3, 5, N'美妆服饰');

-- ---- 其他闲置 ----
INSERT INTO Product (title, price, description, status, seller_id, buyer_id, category) VALUES
(N'宿舍用小功率电煮锅 1.5L', 30.00, N'低于300W宿舍安全可用。煮面煮粥火锅，不粘内胆完好，送木铲蒸架。', 0, 4, NULL, N'其他闲置');

-- ===================================================
-- 商品图片
--   使用 picsum.photos/id/{N} — 每张图唯一且永不改变
--   (Unsplash 专业摄影，全球 CDN，加载快)
--   更换真实商品图：替换下面 URL 中的数字即可
-- ===================================================

-- iPhone 14 (product 1) —— 3张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (1, 'https://picsum.photos/id/1/400/400',   0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (1, 'https://picsum.photos/id/20/400/400',  1);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (1, 'https://picsum.photos/id/160/400/400', 2);

-- 罗技鼠标 (product 2) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (2, 'https://picsum.photos/id/60/400/400',  0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (2, 'https://picsum.photos/id/96/400/400',  1);

-- 索尼耳机 (product 3) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (3, 'https://picsum.photos/id/175/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (3, 'https://picsum.photos/id/180/400/400', 1);

-- 笔记本 (product 4) —— 3张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (4, 'https://picsum.photos/id/0/400/400',   0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (4, 'https://picsum.photos/id/201/400/400', 1);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (4, 'https://picsum.photos/id/366/400/400', 2);

-- 考研数学 (product 5) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (5, 'https://picsum.photos/id/24/400/400',  0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (5, 'https://picsum.photos/id/445/400/400', 1);

-- 六级词汇 (product 6) —— 1张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (6, 'https://picsum.photos/id/24/400/400',  0);

-- Python编程 (product 7) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (7, 'https://picsum.photos/id/367/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (7, 'https://picsum.photos/id/24/400/400',  1);

-- 台灯 (product 8) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (8, 'https://picsum.photos/id/500/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (8, 'https://picsum.photos/id/530/400/400', 1);

-- 小风扇 (product 9) —— 1张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (9, 'https://picsum.photos/id/580/400/400', 0);

-- 自行车 (product 10) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (10, 'https://picsum.photos/id/600/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (10, 'https://picsum.photos/id/605/400/400', 1);

-- Nike AF1 (product 11) —— 3张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (11, 'https://picsum.photos/id/534/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (11, 'https://picsum.photos/id/620/400/400', 1);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (11, 'https://picsum.photos/id/640/400/400', 2);

-- 帆布包 (product 12) —— 1张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (12, 'https://picsum.photos/id/26/400/400',  0);

-- 电煮锅 (product 13) —— 2张图
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (13, 'https://picsum.photos/id/680/400/400', 0);
INSERT INTO ProductImage (product_id, image_url, sort_order) VALUES (13, 'https://picsum.photos/id/700/400/400', 1);

-- ===================================================
-- 收藏数据
-- ===================================================
INSERT INTO Favorite (user_id, product_id) VALUES (3, 1);
INSERT INTO Favorite (user_id, product_id) VALUES (3, 2);
INSERT INTO Favorite (user_id, product_id) VALUES (4, 8);
INSERT INTO Favorite (user_id, product_id) VALUES (4, 10);
INSERT INTO Favorite (user_id, product_id) VALUES (5, 1);
INSERT INTO Favorite (user_id, product_id) VALUES (5, 4);
INSERT INTO Favorite (user_id, product_id) VALUES (5, 11);

-- ===================================================
-- 聊天记录
-- ===================================================
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 3, 2, N'你好，耳机还在吗？');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 2, 3, N'在的，有什么想问的吗');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 3, 2, N'耳罩有磨损吗？音质怎么样');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 2, 3, N'几乎没有磨损，索尼的降噪是顶级的');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 3, 2, N'好的，明天下午3点图书馆门口方便吗');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (3, 2, 3, N'没问题，到时候见');

INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (7, 4, 1, N'这本Python书适合零基础吗？');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (7, 1, 4, N'非常适合，我就是从零开始学的，作者讲解很清晰');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (7, 4, 1, N'好的我要了，今天下午能拿吗');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (7, 1, 4, N'可以，我在3号楼实验室，到了发消息');

INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (12, 5, 3, N'请问这个包能放A4纸吗？');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (12, 3, 5, N'能放A4和iPad，大概35cm×25cm，上课很实用');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (12, 5, 3, N'好，怎么交易');
INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (12, 3, 5, N'明天中午食堂门口见面吧');

-- ===================================================
-- 验证
-- ===================================================
-- SELECT * FROM [User];
-- SELECT id, title, price, category, status FROM Product ORDER BY id;
-- SELECT p.id, p.title, COUNT(pi.id) img_count FROM Product p LEFT JOIN ProductImage pi ON p.id = pi.product_id GROUP BY p.id, p.title ORDER BY p.id;
