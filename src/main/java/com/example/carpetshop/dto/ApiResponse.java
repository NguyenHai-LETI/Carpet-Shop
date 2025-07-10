package com.example.carpetshop.dto;

public class ApiResponse {
    private String message;
    private boolean success;
    private Object data;

    // Constructor mặc định
    public ApiResponse() {
    }

    // Constructor cho success message
    public ApiResponse(String message) {
        this.message = message;
        this.success = true;
    }

    // Constructor đầy đủ
    public ApiResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Static methods để tạo response
    public static ApiResponse success(String message) {
        return new ApiResponse(message, true, null);
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(message, true, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(message, false, null);
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
} 