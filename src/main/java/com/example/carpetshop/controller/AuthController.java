package com.example.carpetshop.controller;

import com.example.carpetshop.dto.LoginRequest;
import com.example.carpetshop.dto.RegisterRequest;
import com.example.carpetshop.dto.UserDTO;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.service.UserService;
import com.example.carpetshop.util.JwtUtil;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth;
            //try {
                // Xác thực người dùng và lấy thông tin UserDetails
                auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(), loginRequest.getPassword()
                        )
                );
//            } catch (BadCredentialsException ex) {
//                logger.warn("Sai thông tin đăng nhập cho email: {}", loginRequest.getEmail());
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
//            } catch (DisabledException | LockedException | CredentialsExpiredException | AccountExpiredException ex) {
//                logger.warn("Tài khoản bị vô hiệu hóa: {}", ex.getMessage());
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản bị khóa hoặc không hợp lệ");
//            } catch (AuthenticationException ex) {
//                logger.error("Lỗi xác thực khác: {}", ex.getMessage());
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Xác thực thất bại: " + ex.getMessage());
//            };

            logger.info("Received login request: email = {}, password = {}", loginRequest.getEmail(), loginRequest.getPassword());


            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            // Lấy danh sách roles của user từ UserDetails
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList());

            // Tạo JWT token với username và roles
            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

            //Thêm dòng này để lấy username:
            String username = userDetails.getUsername();
            // Truy vấn lại User từ DB để trả về thông tin đầy đủ
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không tìm thấy người dùng sau khi xác thực");
            }

            // Tạo DTO trả về
            UserDTO userDTO = new UserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getName(),
                    user.getGender(),
                    user.getBirthDate(),
                    user.getRole().name(),
                    user.getAvatarUrl()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userDTO);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
        } catch (DisabledException | LockedException | CredentialsExpiredException | AccountExpiredException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản không hợp lệ: " + ex.getMessage());
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Lỗi xác thực: " + ex.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request, @Value("${frontend.url:http://localhost:3000}") String frontendUrl) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email không được để trống");
        }
        try {
            // appUrl là url FE để người dùng bấm vào link reset
            String appUrl = frontendUrl;
            userService.createPasswordResetTokenAndSendMail(email, appUrl);
            return ResponseEntity.ok("Đã gửi email đặt lại mật khẩu (nếu email tồn tại trong hệ thống)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (token == null || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token và mật khẩu mới không được để trống");
        }
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/oauth2/authorization/google")
    public ResponseEntity<?> redirectToGoogle() {
        String frontendUrl = System.getenv("FRONTEND_URL");
        
        String googleAuthUrl = frontendUrl + "/oauth2/authorization/google";
        return ResponseEntity.ok().body("Redirect to: " + googleAuthUrl);
    }
}