-- 1. 创建 User 表 (用户表)
CREATE TABLE [User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    school_id VARCHAR(50)
);

-- 2. 创建 Product 表 (商品表)
CREATE TABLE Product (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description NVARCHAR(500),
    status INT DEFAULT 0, -- 0: 待售, 1: 交易中, 2: 已售
    seller_id INT         -- 卖家ID
);

-- 3. 直接塞入一条测试数据！(这就省去了你手动填数据的麻烦)
INSERT INTO Product (title, price, description, status, seller_id) 
VALUES (N'测试二手自行车', 150.00, N'九成新，期中考试复习代步专用', 0, 1);
--新加buyer_id
-- 允许为空，因为商品刚发布时还没有人买
ALTER TABLE Product ADD buyer_id INT NULL;
-- 确保你的用户表里有这些核心字段
ALTER TABLE [User] ADD phone VARCHAR(20) NULL;
ALTER TABLE Product ADD category NVARCHAR(50) NULL;

-- 1. 创建商品图片表
CREATE TABLE ProductImage (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,          -- 关联商品的主键
    image_url VARCHAR(255) NOT NULL,  -- 图片的访问路径
    sort_order INT DEFAULT 0          -- 图片排序（可选，用于指定哪张是封面）
);

-- 2. (可选) 建立外键约束，保证数据一致性
ALTER TABLE ProductImage 
ADD CONSTRAINT FK_Product_Image 
FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE;