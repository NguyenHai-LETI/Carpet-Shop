// 📁 src/main/java/com/example/carpetshop/dto/OrderDTO.java
package com.example.carpetshop.dto;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long orderId;
    private String customerName;
    private Double totalPrice;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public OrderDTO(Long orderId, String customerName, Double totalPrice, String status,
                    String paymentMethod, String shippingAddress, String note,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters và setters
    public Long getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public Double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getShippingAddress() { return shippingAddress; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
