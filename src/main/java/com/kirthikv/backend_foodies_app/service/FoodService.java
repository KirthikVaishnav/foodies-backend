package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.ioobject.FoodRequest;
import com.kirthikv.backend_foodies_app.ioobject.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

   String uploadFile(MultipartFile file);

   FoodResponse addFood(FoodRequest request,MultipartFile file);

   List<FoodResponse> readFoods();

   FoodResponse readFoodsById(String id);

   boolean deleteFile(String fileName);

   void deleteFood(String id);
}
