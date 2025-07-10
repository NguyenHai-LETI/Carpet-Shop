package com.example.carpetshop.dto;

import java.time.LocalDate;

public class UpdateProfileRequest {
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String avatarUrl;
    private String address;
    private String telephoneNumber;

    // Constructor mặc định
    public UpdateProfileRequest() {
    }

    // Constructor đầy đủ
    public UpdateProfileRequest(String name, String gender, LocalDate birthDate, String avatarUrl, String address, String telephoneNumber) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.avatarUrl = avatarUrl;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
    }

    // Getters and Setters
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

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

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", address='" + address + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
} 