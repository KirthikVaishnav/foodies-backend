package com.kirthikv.backend_foodies_app.entity;

import com.kirthikv.backend_foodies_app.ioobject.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use GenerationType.IDENTITY if UUID is not supported.
    private String id;


    private String userId;

    private String userAddress;

    private String phoneNumber;


    private String email;

    @ElementCollection // For non-entity collections
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItem> orderedItems;

    private double amount;

    private String paymentStatus;

    private String razorpayOrderId;

    private String razorpaySignature;

    private String orderStatus;

    String razorpayPaymentId;
}
