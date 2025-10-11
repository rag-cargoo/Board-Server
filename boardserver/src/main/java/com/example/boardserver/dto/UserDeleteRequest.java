package com.example.boardserver.dto;

public class UserDeleteRequest {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasBlankPassword() {
        return password == null || password.trim().isEmpty();
    }
}
