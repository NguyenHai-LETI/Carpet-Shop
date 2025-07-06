package com.example.carpetshop.dto;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private String shippingAddress;
    private String note;
    private String paymentMethod;
    private Double shippingFee;
    private Double discount;
    private List<ItemRequest> items;

    public static class ItemRequest {
        private Long carpetOptionId;
        private Integer quantity;

        // Getters and setters
        public Long getCarpetOptionId() {
            return carpetOptionId;
        }

        public void setCarpetOptionId(Long carpetOptionId) {
            this.carpetOptionId = carpetOptionId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    // Getters và setters cho các trường còn lại
}
