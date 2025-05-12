package com.kirthikv.backend_foodies_app.ioobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    String email;
    String token;
}
