package com.example.carpetshop.controller;

import com.example.carpetshop.dto.OrderDTO;
import com.example.carpetshop.dto.OrderRequest;
import com.example.carpetshop.dto.OrderSuccessResponseDTO;
import com.example.carpetshop.entity.Order;
import com.example.carpetshop.service.CartItemService;
import com.example.carpetshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;



    @PostMapping
    public ResponseEntity<OrderSuccessResponseDTO> placeOrder(@RequestBody OrderRequest request) {
        OrderSuccessResponseDTO response = orderService.placeOrderAndReturnDetails(request);
        cartItemService.clearCartByUserId(request.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersWithCustomerName());
    }


}
