package com.kirthikv.backend_foodies_app.ioobject;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequest {
    List<OrderItem> orderItemList;
    String userAddress;
    double amount;
    String email;
    String phoneNumber;
    String orderStatus;
}
