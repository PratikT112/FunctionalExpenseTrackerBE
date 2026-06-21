package com.pratikt112.expensetrackermain.DTO;

import lombok.*;

public class AuthDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LoginResponse {
        private String token;
        private String userId;
        private String username;
        private String email;
        private String role;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }
}
