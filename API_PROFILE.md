# API Profile Management

## Tổng quan
Các API để quản lý thông tin profile của user trong hệ thống CarpetShop.

## Endpoints

### 1. Lấy thông tin user hiện tại
```
GET /api/users/me
```
**Headers:**
- Authorization: Bearer {token}

**Response:**
```json
{
  "userId": 1,
  "username": "user@example.com",
  "name": "Nguyễn Văn A",
  "gender": "Nam",
  "birthDate": "1990-01-01",
  "role": "USER",
  "avatarUrl": "/uploads/avatars/abc123.jpg",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "telephoneNumber": "0123456789",
  "createdAt": "2024-01-01T00:00:00"
}
```

### 2. Cập nhật thông tin profile
```
PUT /api/users/profile
```
**Headers:**
- Authorization: Bearer {token}
- Content-Type: application/json

**Request Body:**
```json
{
  "name": "Nguyễn Văn B",
  "gender": "Nam",
  "birthDate": "1990-01-01",
  "avatarUrl": "/uploads/avatars/new-avatar.jpg",
  "address": "456 Đường XYZ, Quận 2, TP.HCM",
  "telephoneNumber": "0987654321"
}
```

**Response:**
```json
{
  "userId": 1,
  "username": "user@example.com",
  "name": "Nguyễn Văn B",
  "gender": "Nam",
  "birthDate": "1990-01-01",
  "role": "USER",
  "avatarUrl": "/uploads/avatars/new-avatar.jpg",
  "address": "456 Đường XYZ, Quận 2, TP.HCM",
  "telephoneNumber": "0987654321",
  "createdAt": "2024-01-01T00:00:00"
}
```

### 3. Đổi mật khẩu
```
PUT /api/users/change-password
```
**Headers:**
- Authorization: Bearer {token}
- Content-Type: application/json

**Request Body:**
```json
{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

**Response Success:**
```json
{
  "message": "Đổi mật khẩu thành công",
  "success": true,
  "data": null
}
```

**Response Error:**
```json
{
  "message": "Mật khẩu hiện tại không đúng",
  "success": false,
  "data": null
}
```

### 4. Upload avatar
```
POST /api/users/upload-avatar
```
**Headers:**
- Authorization: Bearer {token}
- Content-Type: multipart/form-data

**Request Body:**
- file: [image file] (max 5MB, chỉ chấp nhận image/*)

**Response Success:**
```json
{
  "message": "Upload avatar thành công",
  "success": true,
  "data": "/uploads/avatars/abc123.jpg"
}
```

**Response Error:**
```json
{
  "message": "File quá lớn (tối đa 5MB)",
  "success": false,
  "data": null
}
```

## Validation Rules

### UpdateProfileRequest
- Tất cả các trường đều optional
- `birthDate`: định dạng yyyy-MM-dd
- `gender`: string tự do
- `telephoneNumber`: string tự do

### ChangePasswordRequest
- `currentPassword`: bắt buộc, phải khớp với mật khẩu hiện tại
- `newPassword`: bắt buộc, tối thiểu 6 ký tự
- `confirmPassword`: bắt buộc, phải khớp với newPassword

### Upload Avatar
- File phải là hình ảnh (content-type: image/*)
- Kích thước tối đa: 5MB
- File sẽ được lưu với tên unique (UUID)

## Error Codes

- `401 Unauthorized`: Chưa đăng nhập hoặc token không hợp lệ
- `404 Not Found`: User không tồn tại
- `400 Bad Request`: Dữ liệu không hợp lệ
- `500 Internal Server Error`: Lỗi server

## Troubleshooting

### Lỗi avatar_url quá dài
Nếu gặp lỗi "value too long for type character varying(255)" khi cập nhật avatar:

1. Chạy script SQL để tăng độ dài field:
```sql
ALTER TABLE users ALTER COLUMN avatar_url TYPE TEXT;
```

2. Restart ứng dụng

### Thông tin không được lưu
1. Kiểm tra log để xem có lỗi gì không
2. Sử dụng endpoint test: `PUT /api/users/test-update/{id}`
3. Kiểm tra dữ liệu trước và sau khi cập nhật

### Upload avatar
- Không gửi base64 data trực tiếp qua API cập nhật profile
- Sử dụng endpoint upload avatar riêng: `POST /api/users/upload-avatar`

## Security

- Tất cả endpoints đều yêu cầu authentication
- User chỉ có thể cập nhật thông tin của chính mình
- Admin có thể cập nhật thông tin của bất kỳ user nào qua endpoint `/api/users/{id}` 