package com.example.carpetshop.dto;

public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    // Constructor mặc định
    public ChangePasswordRequest() {
    }

    // Constructor đầy đủ
    public ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getters and Setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "currentPassword='" + (currentPassword != null ? "[HIDDEN]" : "null") + '\'' +
                ", newPassword='" + (newPassword != null ? "[HIDDEN]" : "null") + '\'' +
                ", confirmPassword='" + (confirmPassword != null ? "[HIDDEN]" : "null") + '\'' +
                '}';
    }
} 