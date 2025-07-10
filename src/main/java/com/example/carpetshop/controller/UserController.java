package com.example.carpetshop.controller;

import com.example.carpetshop.dto.UserDTO;
import com.example.carpetshop.dto.UpdateProfileRequest;
import com.example.carpetshop.dto.ChangePasswordRequest;
import com.example.carpetshop.dto.ApiResponse;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.UserRepository;
import com.example.carpetshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    // Lấy thông tin user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        UserDTO dto = mapToDTO(user);
        return ResponseEntity.ok(dto);
    }

    // Cập nhật thông tin người dùng theo ID (cho admin)
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        System.out.println("===> PUT /api/users/" + id);
        System.out.println("Received DTO: " + dto.getName() + ", " + dto.getBirthDate() + ", " + dto.getAvatarUrl());

        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        // Cập nhật các trường được phép sửa
        user.setName(dto.getName());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());
        user.setAvatarUrl(dto.getAvatarUrl());

        // Lưu lại
        userRepo.save(user);
        return ResponseEntity.ok(mapToDTO(user));
    }

    // Cập nhật profile của user hiện tại
    @PutMapping("/profile")
    @Transactional
    public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, 
                                                @RequestBody UpdateProfileRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }

        String username = principal.getUsername();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        System.out.println("===> PUT /api/users/profile for user: " + username);
        System.out.println("Received request: " + request.toString());

        // Cập nhật các trường được phép sửa
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.setName(request.getName().trim());
        }
        if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
            user.setGender(request.getGender().trim());
        }
        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().trim().isEmpty()) {
            // Kiểm tra nếu avatarUrl là base64 data thì không lưu trực tiếp
            String avatarUrl = request.getAvatarUrl().trim();
            if (avatarUrl.startsWith("data:image")) {
                // Nếu là base64 data, bỏ qua việc cập nhật avatar
                System.out.println("Avatar URL contains base64 data, skipping update");
            } else {
                user.setAvatarUrl(avatarUrl);
            }
        }
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            user.setAddress(request.getAddress().trim());
        }
        if (request.getTelephoneNumber() != null && !request.getTelephoneNumber().trim().isEmpty()) {
            user.setTelephoneNumber(request.getTelephoneNumber().trim());
        }

        try {
            // Lưu lại
            userRepo.save(user);
            System.out.println("Profile updated successfully for user: " + username);
            return ResponseEntity.ok(mapToDTO(user));
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // Đổi mật khẩu
    @PutMapping("/change-password")
    @Transactional
    public ResponseEntity<ApiResponse> changePassword(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                           @RequestBody ChangePasswordRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String username = principal.getUsername();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        System.out.println("===> PUT /api/users/change-password for user: " + username);

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getHashPassword())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu hiện tại không đúng"));
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu mới và xác nhận không khớp"));
        }

        // Kiểm tra độ dài mật khẩu mới
        if (request.getNewPassword().length() < 6) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu mới phải có ít nhất 6 ký tự"));
        }

        // Cập nhật mật khẩu mới
        user.setHashPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công"));
    }

    // Upload avatar
    @PostMapping("/upload-avatar")
    public ResponseEntity<ApiResponse> uploadAvatar(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                         @RequestParam("file") MultipartFile file) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String username = principal.getUsername();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        System.out.println("===> POST /api/users/upload-avatar for user: " + username);

        // Kiểm tra file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File không được để trống"));
        }

        // Kiểm tra loại file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Chỉ chấp nhận file hình ảnh"));
        }

        // Kiểm tra kích thước file (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File quá lớn (tối đa 5MB)"));
        }

        try {
            // Tạo thư mục uploads nếu chưa có
            String uploadDir = "uploads/avatars";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // XÓA ẢNH CŨ nếu có
            String oldAvatarUrl = user.getAvatarUrl();
            if (oldAvatarUrl != null && !oldAvatarUrl.isBlank() && !oldAvatarUrl.equals("/user.jpg")) {
                // Chỉ xóa nếu không phải ảnh mặc định
                Path oldFilePath = Paths.get(".", oldAvatarUrl);
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException ex) {
                    System.err.println("Không thể xóa ảnh avatar cũ: " + ex.getMessage());
                }
            }

            // Tạo tên file unique
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // Cập nhật avatar URL trong database
            String avatarUrl = "/uploads/avatars/" + filename;
            user.setAvatarUrl(avatarUrl);
            userRepo.save(user);

            return ResponseEntity.ok(ApiResponse.success("Upload avatar thành công", avatarUrl));

        } catch (IOException e) {
            System.err.println("Lỗi khi upload avatar: " + e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.error("Lỗi khi upload file"));
        }
    }

    // Mapping entity -> DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getGender(),
                user.getBirthDate(),
                user.getRole().name(),
                user.getAvatarUrl(),
                user.getAddress(),
                user.getTelephoneNumber(),
                user.getCreatedAt()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String username = principal.getUsername();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        System.out.println("===> GET /api/users/me for user: " + username);
        System.out.println("User data: " + user.getName() + ", " + user.getAddress() + ", " + user.getTelephoneNumber());
        return ResponseEntity.ok(mapToDTO(user));
    }

    // Test endpoint để kiểm tra cập nhật profile
    @PutMapping("/test-update/{id}")
    @Transactional
    public ResponseEntity<UserDTO> testUpdateUser(@PathVariable Long id, @RequestBody UpdateProfileRequest request) {
        System.out.println("===> PUT /api/users/test-update/" + id);
        System.out.println("Test request: " + request.toString());

        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        System.out.println("Before update: " + user.getName() + ", " + user.getAddress() + ", " + user.getTelephoneNumber());

        // Cập nhật các trường
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.setName(request.getName().trim());
        }
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            user.setAddress(request.getAddress().trim());
        }
        if (request.getTelephoneNumber() != null && !request.getTelephoneNumber().trim().isEmpty()) {
            user.setTelephoneNumber(request.getTelephoneNumber().trim());
        }

        try {
            userRepo.save(user);
            System.out.println("After update: " + user.getName() + ", " + user.getAddress() + ", " + user.getTelephoneNumber());
            return ResponseEntity.ok(mapToDTO(user));
        } catch (Exception e) {
            System.err.println("Error in test update: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}


