package com.kirthikv.backend_foodies_app.ioobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {

    private String name;

    private String description;

    private double price;

    private String category;
}
