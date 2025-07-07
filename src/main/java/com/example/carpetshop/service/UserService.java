package com.example.carpetshop.service;

import com.example.carpetshop.dto.RegisterRequest;
import com.example.carpetshop.dto.UserDTO;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepo;
    @Autowired private BCryptPasswordEncoder encoder;

    public void register(RegisterRequest req) {
        if (userRepo.findByUsername(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }
        com.example.carpetshop.entity.User u = new com.example.carpetshop.entity.User();
        u.setUsername(req.getEmail());
        u.setHashPassword(encoder.encode(req.getPassword()));
        u.setRole(User.Role.USER); // hoặc ROLE_GUEST nếu bạn có enum đó
        u.setCreatedAt(LocalDateTime.now());
        userRepo.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.carpetshop.entity.User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        // Sử dụng User từ Spring Security (org.springframework.security.core.userdetails.User)
        String password = user.getHashPassword() != null ? user.getHashPassword() : "";
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(password)
                .roles(user.getRole().name())
                .build();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll(); // Đúng tên biến đã khai báo
        return users.stream().map(user -> new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getGender(),
                user.getBirthDate(),
                user.getRole().toString(),
                user.getAvatarUrl(),
                user.getAddress(),
                user.getTelephoneNumber(),
                user.getCreatedAt()
        )).collect(Collectors.toList());
    }



}
