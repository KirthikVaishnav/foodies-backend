package com.kirthikv.backend_foodies_app.repository;

import com.kirthikv.backend_foodies_app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
   Optional<UserEntity> findByEmail(String id);
}
