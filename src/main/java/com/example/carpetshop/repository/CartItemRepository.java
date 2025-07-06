package com.example.carpetshop.repository;

import com.example.carpetshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserUserIdAndVariantId(Long userId, Long variantId);

    // Tìm các cart item theo ID của người dùng
    List<CartItem> findByUser_UserId(Long userId);

}