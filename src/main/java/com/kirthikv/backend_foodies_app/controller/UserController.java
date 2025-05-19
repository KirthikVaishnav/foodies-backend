package com.kirthikv.backend_foodies_app.controller;

import com.kirthikv.backend_foodies_app.ioobject.UserRequest;
import com.kirthikv.backend_foodies_app.ioobject.UserResponse;
import com.kirthikv.backend_foodies_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request){
          return userService.registerUser(request);
    }
}
