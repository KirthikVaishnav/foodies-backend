package com.kirthikv.backend_foodies_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "food_entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodEntity {

    @Id
    private String id; // Change id to String

    private String name;

    @Column(name = "description")
    private String desc;

    private double price;

    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @PrePersist
    private void generateId() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
