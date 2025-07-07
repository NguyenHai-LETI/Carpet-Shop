package com.example.carpetshop.controller;

import com.example.carpetshop.dto.OrderDTO;
import com.example.carpetshop.dto.OrderRequest;
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
@CrossOrigin(origins = "https://carpetshop.netlify.app/")
public class OrderController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(request);  // xử lý lưu đơn hàng
        cartItemService.clearCartByUserId(request.getUserId()); // 🧹 Xóa giỏ hàng
        return ResponseEntity.ok("✅ Đặt hàng thành công với mã đơn #" + order.getOrderId());
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersWithCustomerName());
    }

}
