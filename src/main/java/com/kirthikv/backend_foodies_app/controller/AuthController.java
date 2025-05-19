package com.kirthikv.backend_foodies_app.controller;


import com.kirthikv.backend_foodies_app.ioobject.AuthenticationRequest;
import com.kirthikv.backend_foodies_app.ioobject.AuthenticationResponse;
import com.kirthikv.backend_foodies_app.service.AppUserDetailsService;
import com.kirthikv.backend_foodies_app.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        System.out.println("User logged in: " + request.getEmail() + " | JWT: " + jwtToken); // log login
        return new AuthenticationResponse(request.getEmail(), jwtToken);
    }

}
