package com.example.carpetshop.dto;


import java.util.List;

public class OrderSuccessResponseDTO {
    private OrderDTO order;
    private List<CartItemDTO> items;

    public OrderSuccessResponseDTO(OrderDTO order, List<CartItemDTO> items) {
        this.order = order;
        this.items = items;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }
}
