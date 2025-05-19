package com.kirthikv.backend_foodies_app.repository;

import com.kirthikv.backend_foodies_app.entity.OrderEntity;
import com.kirthikv.backend_foodies_app.ioobject.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,String> {

   List<OrderEntity> findByUserId(String userId);
   Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId);
}
