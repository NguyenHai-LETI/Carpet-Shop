package com.example.carpetshop.service;

import com.example.carpetshop.dto.CartItemDTO;
import com.example.carpetshop.dto.CartItemRequest;
import com.example.carpetshop.entity.*;
import com.example.carpetshop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        CarpetOption variant = carpetOptionRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm"));

        CartItem existingItem = cartItemRepository
                .findByUserUserIdAndVariantId(request.getUserId(), request.getVariantId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setVariant(variant);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
    }

    // (Tuỳ chọn) lấy danh sách sản phẩm trong giỏ hàng theo user
    public List<CartItemDTO> getCartItemsByUserDTO(Long userId) {
        List<CartItem> items = cartItemRepository.findByUser_UserId(userId);

        return items.stream().map(item -> {
            CarpetOption variant = item.getVariant();
            CarpetColorOption colorOption = variant.getCarpetColorOption();

            // In ra các ảnh của sản phẩm để kiểm tra
            System.out.println("🧵 CarpetColorOption ID: " + colorOption.getId());
            colorOption.getImages().forEach(img ->
                    System.out.println("🖼️ Img URL: " + img.getUrl() + " | hover: " + img.isHover())
            );

            Carpet carpet = colorOption.getCarpet();

            String imageUrl = carpet.getColorOptions().stream()
                    .flatMap(opt -> opt.getImages().stream())
                    .filter(Img::isHover)
                    .map(Img::getUrl)
                    .findFirst()
                    .orElse(null);


            System.out.println("✅ imageUrl được chọn: " + imageUrl);

            return new CartItemDTO(
                    item.getId(),
                    colorOption.getCarpet().getName(),
                    variant.getSize().getValue(),
                    colorOption.getColor().getValue(),
                    item.getQuantity(),
                    variant.getPrice(),
                    imageUrl
            );
        }).toList();
    }



    @Transactional
    public void updateQuantity(Long itemId, Integer newQuantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        if (newQuantity <= 0) {
            cartItemRepository.delete(item); // hoặc xử lý khác tuỳ bạn
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void deleteItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCartByUserId(Long userId) {
        List<CartItem> items = cartItemRepository.findByUser_UserId(userId);
        cartItemRepository.deleteAll(items);
    }

}
