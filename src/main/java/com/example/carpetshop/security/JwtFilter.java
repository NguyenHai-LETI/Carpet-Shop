package com.example.carpetshop.security;

import com.example.carpetshop.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();



        // Bỏ qua kiểm tra JWT cho các endpoint công khai
        if (path.startsWith("/api/login")
                || path.startsWith("/api/register")
                || path.startsWith("/api/reset-password")
                || path.startsWith("/oauth2")
                || path.startsWith("/login/oauth2/**")
                || path.startsWith("/api/carpets/**")) {
            filterChain.doFilter(request, response);
            System.out.println("JwtFilter: Bỏ qua kiểm tra JWT cho route public: " + path);
            return;
        }

        // Phần còn lại: kiểm tra JWT
        String authHeader = request.getHeader("Authorization");
        //check token from header of request
        System.out.println("📥 [JwtFilter] Đường dẫn request: " + request.getRequestURI());
        System.out.println("📥 [JwtFilter] Authorization Header: " + authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            System.out.println("📥 [JwtFilter] Đã tách token: " + token);

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Setter methods nếu cần set bằng tay
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
