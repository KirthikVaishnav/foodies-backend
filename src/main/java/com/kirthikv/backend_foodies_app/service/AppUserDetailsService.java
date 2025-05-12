package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.UserEntity;
import com.kirthikv.backend_foodies_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService{
  final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       UserEntity user= userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
      return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }

}
