package com.example.carpetshop.controller;

import com.example.carpetshop.dto.UserDTO;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.UserRepository;
import com.example.carpetshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserRepository userRepo;

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

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    @Transactional
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

    // Mapping entity -> DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getGender(),
                user.getBirthDate(),
                user.getRole().name(),
                user.getAvatarUrl()
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
        return ResponseEntity.ok(mapToDTO(user));
    }

}


