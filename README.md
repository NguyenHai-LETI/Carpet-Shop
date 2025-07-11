# CarpetShop - Hệ thống bán thảm trực tuyến
Live demo: https://carpetshop.netlify.app/

## Tổng quan

CarpetShop là một ứng dụng web bán thảm trực tuyến được xây dựng với kiến trúc monolithic, bao gồm:

- **Backend**: Spring Boot API với Java 21
- **Frontend**: React.js với Bootstrap 5
- **Database**: PostgreSQL
- **Authentication**: JWT + OAuth2 (Google)
- **Email Service**: Gmail SMTP

## Kiến trúc hệ thống

```
carpetshop/
├── carpetshop_be/          # Backend Spring Boot
│   ├── src/main/java/
│   │   └── com/example/carpetshop/
│   │       ├── controller/     # REST Controllers
│   │       ├── entity/         # JPA Entities
│   │       ├── repository/     # Data Access Layer
│   │       ├── service/        # Business Logic
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── config/        # Configuration
│   │       ├── security/      # Security & JWT
│   │       └── util/          # Utilities
│   └── src/main/resources/
│       └── application.properties
├── carpetshop-fe/           # Frontend React
│   ├── src/
│   │   ├── pages/           # React Components
│   │   ├── components/      # Reusable Components
│   │   └── App.js          # Main App Component
│   └── public/             # Static Assets
└── uploads/                # File Uploads
    └── avatars/           # User Avatars
```

## Tính năng chính

### Quản lý người dùng
- Đăng ký/Đăng nhập với email và mật khẩu
- Đăng nhập bằng Google OAuth2
- Quản lý profile người dùng
- Upload avatar
- Đổi mật khẩu
- Quên mật khẩu (gửi email reset)

### Mua sắm
- Xem danh sách sản phẩm thảm
- Tìm kiếm và lọc sản phẩm
- Xem chi tiết sản phẩm với nhiều màu sắc và kích thước
- Thêm vào giỏ hàng
- Quản lý giỏ hàng
- Đặt hàng và thanh toán

### Quản trị (Admin)
- Quản lý sản phẩm (thêm, sửa, xóa)
- Quản lý người dùng
- Quản lý đơn hàng
- Dashboard thống kê

## Công nghệ sử dụng

### Backend
- **Java 21** 
- **Spring Boot 3.5.3** 
- **Spring Security** 
- **Spring Data JPA** 
- **PostgreSQL**
- **JWT** 
- **OAuth2** 
- **Spring Mail**
- **Maven**

### Frontend
- **React 19.1.0** 
- **React Router DOM** 
- **Bootstrap 5.3.7** 
- **Bootstrap Icons** 
- **Axios** 
- **React Select** 

## Cài đặt và chạy

### Yêu cầu hệ thống
- Java 21+
- Node.js 18+
- PostgreSQL 12+
- Maven 3.6+

### 1. Clone repository
```bash
git clone <repository-url>
cd carpetshop
```

### 2. Cấu hình Database
Tạo database PostgreSQL và cập nhật thông tin trong file `.env`:

```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/carpetshop
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# Google OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Gmail SMTP
GMAIL_APP_PASSWORD=your_gmail_app_password
```

### 3. Chạy Backend
```bash
cd carpetshop_be
mvn spring-boot:run
```

Backend sẽ chạy tại: http://localhost:8080

### 4. Chạy Frontend
```bash
cd carpetshop-fe
npm install
npm start
```

Frontend sẽ chạy tại: http://localhost:3000

## Cấu trúc Database

![Database UML](/database_UML.png)

### Các bảng chính:
- **users** - Thông tin người dùng
- **carpets** - Sản phẩm thảm
- **carpet_color_options** - Tùy chọn màu sắc
- **carpet_types** - Loại thảm (kích thước, giá)
- **cart_items** - Giỏ hàng
- **orders** - Đơn hàng
- **order_items** - Chi tiết đơn hàng
- **password_reset_tokens** - Token reset mật khẩu

## API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/forgot-password` - Quên mật khẩu
- `POST /api/reset-password` - Reset mật khẩu
- `GET /oauth2/authorization/google` - OAuth2 Google

### User Management
- `GET /api/users/me` - Lấy thông tin user hiện tại
- `PUT /api/users/profile` - Cập nhật profile
- `PUT /api/users/change-password` - Đổi mật khẩu
- `POST /api/users/upload-avatar` - Upload avatar

### Products
- `GET /api/carpets` - Lấy danh sách thảm
- `GET /api/carpets/{id}` - Lấy chi tiết thảm
- `GET /api/carpets/filter` - Lọc thảm

### Cart & Orders
- `GET /api/cart` - Lấy giỏ hàng
- `POST /api/cart/add` - Thêm vào giỏ hàng
- `PUT /api/cart/update` - Cập nhật giỏ hàng
- `DELETE /api/cart/remove/{id}` - Xóa khỏi giỏ hàng
- `POST /api/orders` - Tạo đơn hàng
- `GET /api/orders` - Lấy danh sách đơn hàng

### Admin (Yêu cầu quyền ADMIN)
- `GET /api/admin/users` - Quản lý người dùng
- `GET /api/admin/carpets` - Quản lý sản phẩm
- `GET /api/admin/orders` - Quản lý đơn hàng

## Docker Deployment

### Build và chạy với Docker
```bash
# Build backend
cd carpetshop_be
docker build -t carpetshop-backend .

# Run backend
docker run -p 8080:8080 --env-file .env carpetshop-backend

# Build frontend
cd carpetshop-fe
docker build -t carpetshop-frontend .

# Run frontend
docker run -p 3000:3000 carpetshop-frontend
```

## Cấu hình môi trường

### Environment Variables

#### Backend (.env)
```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/carpetshop
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

# JWT
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=86400000

# Google OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Gmail SMTP
GMAIL_APP_PASSWORD=your_gmail_app_password
```