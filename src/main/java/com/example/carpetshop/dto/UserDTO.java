package com.example.carpetshop.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {
    private Long userId;
    private String username;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String role;

    private String avatarUrl;
    private String address;
    private String telephoneNumber;
    private LocalDateTime createdAt;


    public UserDTO() {
    }

    public UserDTO(Long userId, String username, String name, String gender, LocalDate birthDate, String role, String avatarUrl) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    // ✅ Constructor mới đầy đủ thông tin từ User entity
    public UserDTO(Long userId, String username, String name, String gender, LocalDate birthDate, String role, String avatarUrl, String address, String telephoneNumber, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.createdAt = createdAt;
    }


    // Getters & Setters


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
