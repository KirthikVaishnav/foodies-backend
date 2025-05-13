package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.CartEntity;
import com.kirthikv.backend_foodies_app.ioobject.CartRequest;
import com.kirthikv.backend_foodies_app.ioobject.CartResponse;
import com.kirthikv.backend_foodies_app.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{
    final CartRepository cartRepository;
    final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
       String loggedInUserId= userService.findById(request.getFoodId());
       Optional<CartEntity> cartOptional= cartRepository.findByUserId(loggedInUserId);
       CartEntity cart= cartOptional.orElseGet(()->new CartEntity(loggedInUserId, new HashMap<>()));
       Map<String,Integer> cartItems=cart.getItems();
       cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(),0)+1);
       cart.setItems(cartItems);
       cart =cartRepository.save(cart);
       return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId= userService.getCurrentUserId();
        CartEntity cart= cartRepository.findByUserId(loggedInUserId)
                .orElse(new CartEntity(null,loggedInUserId,new HashMap<>()));
        return convertToResponse(cart);
    }

    @Transactional
    @Override
    public void clearCart() {
        String loggedInUserId = userService.getCurrentUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String loggedInUserId = userService.getCurrentUserId();
        CartEntity cartEntity=cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(()->new RuntimeException("Cart is not Found"));
        Map<String,Integer> cartItems = cartEntity.getItems();
         if (cartItems.containsKey(request.getFoodId())){
            int currentQty = cartItems.get(request.getFoodId());
            if (currentQty>0){
                cartItems.put(request.getFoodId(),currentQty-1);
            }else {
                cartItems.remove(request.getFoodId());
            }
            cartEntity= cartRepository.save(cartEntity);

         }
         return convertToResponse(cartEntity);
    }

    public CartResponse convertToResponse(CartEntity entity){
        return CartResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .items(entity.getItems())
                .build();
    }
}
