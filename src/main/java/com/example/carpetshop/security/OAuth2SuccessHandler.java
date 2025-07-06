package com.example.carpetshop.security;


import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.UserRepository;
import com.example.carpetshop.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không lấy được email từ Google");
            return;
        }

        // Kiểm tra hoặc tạo user trong DB
        Optional<User> existingUser = userRepository.findByUsername(email);
        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setName(name);
            newUser.setRole(User.Role.USER);
            return userRepository.save(newUser);
        });

        // Sinh JWT
        String token = jwtUtil.generateToken(user.getUsername(), List.of(user.getRole().name()));

        System.out.println(">>>>> Đăng nhập Google thành công cho: " + email);
        System.out.println(">>>>> JWT token sinh ra: " + token);


        // Redirect về frontend
        String redirectUrl = "http://localhost:3000/oauth-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}
