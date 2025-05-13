package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.ioobject.CartRequest;
import com.kirthikv.backend_foodies_app.ioobject.CartResponse;

public interface CartService {
   CartResponse addToCart(CartRequest request);
   CartResponse getCart();
   void clearCart();
   CartResponse removeFromCart(CartRequest request);
}
