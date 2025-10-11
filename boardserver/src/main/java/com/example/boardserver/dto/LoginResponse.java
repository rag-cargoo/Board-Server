package com.example.boardserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final boolean success;
    private final String userId;
    private final boolean admin;
    private final String message;

    public static LoginResponse success(String userId, boolean admin, String message) {
        return LoginResponse.builder()
                .success(true)
                .userId(userId)
                .admin(admin)
                .message(message)
                .build();
    }

    public static LoginResponse success(UserDTO user, String message) {
        boolean isAdmin = user != null && user.getStatus() == UserDTO.Status.ADMIN;
        String userId = user != null ? user.getUserId() : null;
        return success(userId, isAdmin, message);
    }

    public static LoginResponse failure(String message) {
        return LoginResponse.builder()
                .success(false)
                .userId(null)
                .admin(false)
                .message(message)
                .build();
    }
}
