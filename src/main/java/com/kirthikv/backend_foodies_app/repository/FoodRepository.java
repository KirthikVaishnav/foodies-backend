package com.kirthikv.backend_foodies_app.repository;

import com.kirthikv.backend_foodies_app.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity,String> {
}
