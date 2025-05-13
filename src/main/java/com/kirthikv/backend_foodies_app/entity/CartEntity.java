package com.kirthikv.backend_foodies_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    @ElementCollection
    @CollectionTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id") // this joins with CartEntity.id
    )
    @MapKeyColumn(name = "item_id") // column for the Map key
    @Column(name = "quantity") // column for the Map value
    private Map<String, Integer> items = new HashMap<>();

    public CartEntity(String userId, Map<String, Integer> items) {
        this.userId = userId;
        this.items = items;
    }
}
