package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.UserEntity;
import com.kirthikv.backend_foodies_app.ioobject.UserRequest;
import com.kirthikv.backend_foodies_app.ioobject.UserResponse;
import com.kirthikv.backend_foodies_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findById(String id) {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return loggedInUser.getId();
    }

    @Override
    public String getCurrentUserId() {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return loggedInUser.getId();
    }

    @Override
    public UserEntity getCurrentUser() {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        return userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }



    private UserEntity  convertToEntity(UserRequest request){
          return UserEntity.builder()
                  .email(request.getEmail())
                  .name(request.getName())
                  .password(passwordEncoder.encode(request.getPassword()))
                  .build();
    }

    private UserResponse convertToResponse(UserEntity registeredUser){
        return UserResponse.builder()
                .id(registeredUser.getId())
                .email(registeredUser.getEmail())
                .name(registeredUser.getName())
                .build();
    }
}
