package com.example.carpetshop.dto;

public class VariantOptionDTO {
    private Long id;
    private String color;
    private String size;
    private Double price;
    private Double discount; // nếu bạn muốn gửi cả discount
    private Integer stock;

    public VariantOptionDTO(Long id, String color, String size, Double price,Double discount, Integer stock) {
        this.id = id;
        this.color = color;
        this.size = size;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
