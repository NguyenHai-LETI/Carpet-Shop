-- Script để tăng độ dài field avatar_url trong bảng users
-- Chạy script này trong database PostgreSQL

-- Tăng độ dài field avatar_url từ VARCHAR(255) lên TEXT để có thể lưu URL dài hơn
ALTER TABLE users ALTER COLUMN avatar_url TYPE TEXT;

-- Hoặc nếu muốn giữ VARCHAR nhưng tăng độ dài
-- ALTER TABLE users ALTER COLUMN avatar_url TYPE VARCHAR(1000);

-- Kiểm tra kết quả
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'users' AND column_name = 'avatar_url'; 