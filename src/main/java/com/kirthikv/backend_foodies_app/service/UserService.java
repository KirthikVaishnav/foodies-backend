package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.UserEntity;
import com.kirthikv.backend_foodies_app.ioobject.UserRequest;
import com.kirthikv.backend_foodies_app.ioobject.UserResponse;

public interface UserService {

   UserResponse registerUser(UserRequest request);
   String findById(String id);
   UserEntity getCurrentUser();
   String getCurrentUserId();
}
