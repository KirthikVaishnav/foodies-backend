package com.kirthikv.backend_foodies_app.ioobject;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderResponse {
    String id;
    String userId;
    String userAddress;
    String phoneNumber;
    String email;
    double amount;
    String paymentStatus;
    String razorpayOrderId;
    String orderStatus;
    private List<OrderItem> orderItemList;
}
