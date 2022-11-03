package com.hust.vitech.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String jwtToken;

    private Long id;

    private String username;

    private List<String> roles;
}
