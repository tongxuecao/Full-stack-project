-- 1. ���� User �� (�û���)
CREATE TABLE [User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    school_id VARCHAR(50)
);

-- 2. ���� Product �� (��Ʒ��)
CREATE TABLE Product (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description NVARCHAR(500),
    status INT DEFAULT 0, -- 0: ����, 1: ������, 2: ����
    seller_id INT         -- ����ID
);

-- 3. ֱ������һ���������ݣ�(���ʡȥ�����ֶ������ݵ��鷳)
INSERT INTO Product (title, price, description, status, seller_id) 
VALUES (N'���Զ������г�', 150.00, N'�ų��£����п��Ը�ϰ����ר��', 0, 1);
--�¼�buyer_id
-- ����Ϊ�գ���Ϊ��Ʒ�շ���ʱ��û������
ALTER TABLE Product ADD buyer_id INT NULL;
-- ȷ������û���������Щ�����ֶ�
ALTER TABLE [User] ADD phone VARCHAR(20) NULL;
ALTER TABLE Product ADD category NVARCHAR(50) NULL;

-- 1. ������ƷͼƬ��
CREATE TABLE ProductImage (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,          -- ������Ʒ������
    image_url VARCHAR(255) NOT NULL,  -- ͼƬ�ķ���·��
    sort_order INT DEFAULT 0          -- ͼƬ���򣨿�ѡ������ָ�������Ƿ��棩
);

-- 2. (��ѡ) �������Լ������֤����һ����
ALTER TABLE ProductImage
ADD CONSTRAINT FK_Product_Image
FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE;

-- Add avatar column for user profile picture
ALTER TABLE [User] ADD avatar VARCHAR(255) NULL;

-- Favorite (wishlist) table
CREATE TABLE Favorite (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL
);

-- Chat messages for buyer-seller communication
CREATE TABLE ChatMessage (
    id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    content NVARCHAR(500) NOT NULL,
    create_time DATETIME DEFAULT GETDATE(),
    is_read INT DEFAULT 0
);


DELETE FROM ChatMessage;
DELETE FROM Favorite;
DELETE FROM ProductImage;
DELETE FROM Product;
DELETE FROM [User];
INSERT INTO [User] (username, password, phone, school_id, avatar) VALUES
(N'张三', '123456', '13800001001', '20240001', 'https://api.dicebear.com/7.x/initials/svg?seed=ZS&backgroundColor=409eff'),
(N'李四', '123456', '13800001002', '20240002', 'https://api.dicebear.com/7.x/initials/svg?seed=LS&backgroundColor=67c23a'),
(N'王五', '123456', '13800001003', '20240003', 'https://api.dicebear.com/7.x/initials/svg?seed=WW&backgroundColor=e6a23c')


-- 假设你当前的电脑 IP 是 10.240.165.107
UPDATE ProductImage 
SET image_url = REPLACE(image_url, 'http://localhost:8080', 'http://10.240.165.107:8080')
WHERE image_url LIKE '%http://localhost:8080%';