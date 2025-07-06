package com.example.carpetshop.controller;

import com.example.carpetshop.dto.CartItemDTO;
import com.example.carpetshop.dto.CartItemRequest;
import com.example.carpetshop.entity.CartItem;
import com.example.carpetshop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemRequest request) {
        cartItemService.addToCart(request);
        return ResponseEntity.ok("🛒 Đã thêm sản phẩm vào giỏ hàng thành công!");
    }

    // (Tuỳ chọn) API lấy giỏ hàng của 1 user

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItemDTO>> getCartItemsByUser(@PathVariable Long userId) {
        List<CartItemDTO> items = cartItemService.getCartItemsByUserDTO(userId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long itemId, @RequestBody Map<String, Integer> body) {
        Integer quantity = body.get("quantity");
        if (quantity == null) {
            return ResponseEntity.badRequest().body("Thiếu trường quantity");
        }

        cartItemService.updateQuantity(itemId, quantity);
        return ResponseEntity.ok("Đã cập nhật số lượng");
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long itemId) {
        cartItemService.deleteItem(itemId);
        return ResponseEntity.ok("🗑️ Đã xóa sản phẩm khỏi giỏ hàng!");
    }





}
