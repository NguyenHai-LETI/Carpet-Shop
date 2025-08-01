package com.example.carpetshop.service;

import com.example.carpetshop.dto.CartItemDTO;
import com.example.carpetshop.dto.OrderDTO;
import com.example.carpetshop.dto.OrderRequest;
import com.example.carpetshop.dto.OrderSuccessResponseDTO;
import com.example.carpetshop.entity.CarpetOption;
import com.example.carpetshop.entity.Order;
import com.example.carpetshop.entity.OrderItem;
import com.example.carpetshop.entity.User;
import com.example.carpetshop.repository.CarpetOptionRepository;
import com.example.carpetshop.repository.OrderItemRepository;
import com.example.carpetshop.repository.OrderRepository;
import com.example.carpetshop.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private MailService mailService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Order placeOrder(OrderRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("Missing userId");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        double totalProductPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequest.ItemRequest itemReq : request.getItems()) {
            if (itemReq.getCarpetOptionId() == null) {
                throw new RuntimeException("Missing product variant ID (carpetOptionId) in order item: " + itemReq);
            }

            CarpetOption option = carpetOptionRepository.findById(itemReq.getCarpetOptionId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found: id=" + itemReq.getCarpetOptionId()));

            int currentStock = option.getStock() != null ? option.getStock() : 0;
            System.out.println("[ORDER] CarpetOptionId: " + itemReq.getCarpetOptionId() + ", CurrentStock: " + currentStock + ", Quantity: " + itemReq.getQuantity());
            int newStock = currentStock - itemReq.getQuantity();
            if (newStock < 0) {
                String errMsg = String.format("Insufficient stock for variantId=%d. Current stock: %d, requested: %d", itemReq.getCarpetOptionId(), currentStock, itemReq.getQuantity());
                System.err.println("❌ " + errMsg);
                throw new RuntimeException(errMsg);
            }

            option.setStock(newStock);
            carpetOptionRepository.saveAndFlush(option);
            System.out.println("[ORDER] After update: CarpetOptionId: " + itemReq.getCarpetOptionId() + ", NewStock: " + newStock);

            totalProductPrice += option.getPrice() * itemReq.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setVariant(option);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItems.add(orderItem);
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

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        // Gửi email xác nhận đơn hàng
        try {
            String subject = "Xác nhận đơn hàng #" + order.getOrderId();
            String content = buildEmailContent(order, user);
            mailService.sendOrderConfirmationEmail(user.getUsername(), subject, content);
            System.out.println("Email sent successfully to: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send email: " + e.getMessage());
            
        }

        // Clear JPA cache để đảm bảo lần fetch tiếp theo lấy dữ liệu mới nhất
        entityManager.clear();

        return order;
    }

    public List<OrderDTO> getAllOrdersWithCustomerName() {
        return orderRepository.findAll().stream()
                .map(o -> new OrderDTO(
                        o.getOrderId(),
                        o.getUser().getName(),
                        o.getTotalPrice(),
                        o.getStatus(),
                        o.getPaymentMethod(),
                        o.getShippingAddress(),
                        o.getNote(),
                        o.getCreatedAt(),
                        o.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public OrderSuccessResponseDTO placeOrderAndReturnDetails(OrderRequest request) {
        Order order = placeOrder(request); // đã xử lý gửi mail bên trong
        List<CartItemDTO> items = cartItemService.getCartItemsByUserDTO(request.getUserId());
        return new OrderSuccessResponseDTO(
                new OrderDTO(
                        order.getOrderId(),
                        order.getUser().getName(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getPaymentMethod(),
                        order.getShippingAddress(),
                        order.getNote(),
                        order.getCreatedAt(),
                        order.getUpdatedAt()
                ),
                items
        );
    }

    // nội dung email xác nhận
    private String buildEmailContent(Order order, User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Xin chào ").append(user.getName()).append(",\n\n");
        sb.append("Cảm ơn bạn đã đặt hàng tại CarpetShop!\n\n");
        sb.append("Mã đơn hàng: ").append(order.getOrderId()).append("\n");
        sb.append("Ngày đặt: ").append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        sb.append("Tổng tiền: ").append(order.getTotalPrice()).append(" $\n\n");
        sb.append("Chúng tôi sẽ liên hệ với bạn để xác nhận giao hàng.\n\n");
        sb.append("Trân trọng,\nCarpetShop");
        return sb.toString();
    }
}
