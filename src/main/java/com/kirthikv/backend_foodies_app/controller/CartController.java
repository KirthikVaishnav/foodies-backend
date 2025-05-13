package com.kirthikv.backend_foodies_app.controller;

import com.kirthikv.backend_foodies_app.ioobject.CartRequest;
import com.kirthikv.backend_foodies_app.ioobject.CartResponse;
import com.kirthikv.backend_foodies_app.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request){
          String foodId = request.getFoodId();
          if (foodId == null || foodId.isEmpty()){
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId Not Found");
          }
         return cartService.addToCart(request);

    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  clearCart(){
        cartService.clearCart();
    }

    @PostMapping("/remove")
    public CartResponse removeFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId Not Found");
        }
        return cartService.removeFromCart(request);
    }
}
