package com.example.carpetshop.service;

import com.example.carpetshop.dto.OrderDTO;
import com.example.carpetshop.dto.OrderRequest;
import com.example.carpetshop.entity.*;
import com.example.carpetshop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarpetOptionRepository carpetOptionRepository;

    @Transactional
    public Order placeOrder(OrderRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("❌ Thiếu userId");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy user"));

        double totalProductPrice = 0;
        for (OrderRequest.ItemRequest item : request.getItems()) {
            if (item.getCarpetOptionId() == null) {
                throw new RuntimeException("❌ Thiếu ID sản phẩm");
            }

            CarpetOption variant = carpetOptionRepository.findById(item.getCarpetOptionId())
                    .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy sản phẩm"));
            totalProductPrice += variant.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus("pending");
        order.setNote(request.getNote());
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTotalPrice(totalProductPrice + request.getShippingFee() - request.getDiscount());

        orderRepository.save(order);

        for (OrderRequest.ItemRequest item : request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setVariant(carpetOptionRepository.findById(item.getCarpetOptionId()).get());

            orderItemRepository.save(orderItem);
        }

        return order;
    }
    public List<OrderDTO> getAllOrdersWithCustomerName() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> new OrderDTO(
                order.getOrderId(),
                order.getUser().getName(), // ✅ Tên khách hàng
                order.getTotalPrice(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getNote(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        )).toList();
    }

}
