package com.example.carpetshop.dto;

public class CarpetSummaryDTO {
    private Long carpetId;
    private String name;
    private int stock;

    public CarpetSummaryDTO(Long carpetId, String name, int stock) {
        this.carpetId = carpetId;
        this.name = name;
        this.stock = stock;
    }

    public Long getCarpetId() {
        return carpetId;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }
}
