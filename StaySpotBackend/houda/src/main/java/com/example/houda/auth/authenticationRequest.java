package com.example.houda.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class authenticationRequest {
    private String email;
    private String password;
}
