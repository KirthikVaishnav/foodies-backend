package com.kirthikv.backend_foodies_app.repository;

import com.kirthikv.backend_foodies_app.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<CartEntity,String> {
    Optional<CartEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}
