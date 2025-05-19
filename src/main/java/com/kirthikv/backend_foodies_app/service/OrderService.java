package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.exception.CustomException;
import com.kirthikv.backend_foodies_app.ioobject.OrderRequest;
import com.kirthikv.backend_foodies_app.ioobject.OrderResponse;
import com.razorpay.RazorpayException;


import java.util.List;
import java.util.Map;

public interface OrderService {

  OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException, CustomException;

  void verifyPayment(Map<String,String> paymentData, String status);

  List<OrderResponse> getUserOrders();

  void removeOrder(String orderId);

  List<OrderResponse> getOrdersOfAllUsers();

  void updateOrderStatus(String orderId, String status);

}
