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
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            System.out.println("🔄 Processing order request: " + request.getUserId());
            OrderSuccessResponseDTO response = orderService.placeOrderAndReturnDetails(request);
            cartItemService.clearCartByUserId(request.getUserId());
            System.out.println("✅ Order placed successfully for user: " + request.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error placing order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Order failed: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersWithCustomerName());
    }
}
