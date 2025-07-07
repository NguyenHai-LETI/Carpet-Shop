package com.example.carpetshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
public class LoginController {

    // ✅ Trả về thông tin người dùng Google sau khi đăng nhập thành công
    @GetMapping("/user")
    public Map<String, Object> getUserInfo(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttributes(); // chứa name, email, picture...
    }

    // ✅ Tuỳ chọn: chuyển hướng về frontend sau khi login thành công
    @GetMapping("/success")
    public void redirectAfterLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://https://carpetshop.netlify.app/oauth2-success"); // React route sau khi login
    }
}
