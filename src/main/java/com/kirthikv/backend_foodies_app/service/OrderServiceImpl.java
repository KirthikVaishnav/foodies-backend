package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.OrderEntity;
import com.kirthikv.backend_foodies_app.ioobject.OrderRequest;
import com.kirthikv.backend_foodies_app.ioobject.OrderResponse;
import com.kirthikv.backend_foodies_app.repository.CartRepository;
import com.kirthikv.backend_foodies_app.repository.OrderRepository;
import com.kirthikv.backend_foodies_app.exception.CustomException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;

    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Transactional
    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        logger.info("Creating a new order with payment for request: {}", request);

        OrderEntity newOrder = convertToEntity(request);

        // Associate user ID before saving
        String loggedInUserId = userService.getCurrentUserId();
        newOrder.setUserId(loggedInUserId);

        newOrder = orderRepository.save(newOrder);
        logger.info("Order saved in database with initial state: {}", newOrder);

        try {
            RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", newOrder.getAmount() * 100); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
        } catch (RazorpayException e) {
            logger.error("Error while creating Razorpay order", e);
            throw new CustomException("Failed to create Razorpay order. Please try again.", e);
        }

        // Save the order again with updated razorpayOrderId
        newOrder = orderRepository.save(newOrder);
        logger.info("Final order saved in database: {}", newOrder);

        OrderResponse response = convertToResponse(newOrder);
        logger.info("Order response created: {}", response);

        return response;
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
       String razorpayOrderId = paymentData.get("razorpay_order_id");
      OrderEntity existingOrder= orderRepository.findByRazorpayOrderId(razorpayOrderId)
               .orElseThrow(()->new RuntimeException("Order Not Found"));
       existingOrder.setPaymentStatus(status);
       existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
       existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
       orderRepository.save(existingOrder);
       if("paid".equalsIgnoreCase(status)){
           cartRepository.deleteByUserId(existingOrder.getUserId());
       }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedinUserId = userService.getCurrentUserId();
        List<OrderEntity> list = orderRepository.findByUserId(loggedinUserId);
        return list.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);

    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
       List<OrderEntity> list= orderRepository.findAll();
       return list.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
       OrderEntity entity= orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("Order Not Found"));
       entity.setOrderStatus(status);
       orderRepository.save(entity);
    }


    private OrderResponse convertToResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .id(newOrder.getId())
                .amount(newOrder.getAmount())
                .userAddress(newOrder.getUserAddress())
                .userId(newOrder.getUserId())
                .razorpayOrderId(newOrder.getRazorpayOrderId())
                .orderStatus(newOrder.getOrderStatus())
                .paymentStatus(newOrder.getPaymentStatus())
                .email(newOrder.getEmail())
                .phoneNumber(newOrder.getPhoneNumber())
                .orderItemList(newOrder.getOrderedItems())
                .build();
    }

    OrderEntity convertToEntity(OrderRequest request) {
        return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderItemList())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .orderStatus(request.getOrderStatus())
                .paymentStatus("PENDING")
                .build();
    }
}
