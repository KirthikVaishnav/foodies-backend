package com.kirthikv.backend_foodies_app.ioobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationRequest {
    String email;
    String password;
}
