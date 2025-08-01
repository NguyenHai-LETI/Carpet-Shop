package com.example.carpetshop.dto;

//DTO(data transfer object) này dùng để xử lý API/register - tác vụ đăng ký
public class RegisterRequest {
    private String email;
    private String password;

    // Constructors rỗng dùng cho annotation @RequestBody
    public RegisterRequest() {}

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}