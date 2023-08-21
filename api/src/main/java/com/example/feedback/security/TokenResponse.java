package com.example.feedback.security;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
 
    public TokenResponse() { }
     
    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}