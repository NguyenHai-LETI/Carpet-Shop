package com.example.carpetshop.service;

import com.example.carpetshop.dto.CartItemDTO;
import com.example.carpetshop.dto.CartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.carpetshop.entity.CartItem;
import com.example.carpetshop.entity.CarpetOption;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.CartItemRepository;
import com.example.carpetshop.repository.CarpetOptionRepository;
import com.example.carpetshop.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarpetOptionRepository carpetOptionRepository;

    @Transactional
    public void addToCart(CartItemRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CarpetOption variant = carpetOptionRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        cartItemRepository.findByUserUserIdAndVariantId(request.getUserId(), request.getVariantId())
                .ifPresentOrElse(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    cartItemRepository.save(existing);
                }, () -> {
                    CartItem newItem = new CartItem();
                    newItem.setUser(user);
                    newItem.setVariant(variant);
                    newItem.setQuantity(request.getQuantity());
                    cartItemRepository.save(newItem);
                });
    }

    @org.springframework.transaction.annotation.Transactional
    public List<CartItemDTO> getCartItemsByUserDTO(Long userId) {
        return cartItemRepository.findByUser_UserId(userId).stream().map(item -> {
            CarpetOption variant = item.getVariant();
            var colorOption = variant.getCarpetColorOption();
            var carpet = colorOption.getCarpet();

            String imageUrl = carpet.getColorOptions().stream()
                    .flatMap(opt -> opt.getImages().stream())
                    .filter(img -> img.isHover())
                    .map(img -> img.getUrl())
                    .findFirst()
                    .orElse(null);

            return new CartItemDTO(
                    item.getId(),
                    carpet.getName(),
                    variant.getSize().getValue(),
                    colorOption.getColor().getValue(),
                    item.getQuantity(),
                    variant.getPrice(),
                    imageUrl
            );
        }).collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public void updateQuantity(Long itemId, Integer newQuantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (newQuantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCartByUserId(Long userId) {
        var items = cartItemRepository.findByUser_UserId(userId);
        cartItemRepository.deleteAll(items);
    }
}