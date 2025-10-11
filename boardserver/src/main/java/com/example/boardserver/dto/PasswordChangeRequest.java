package com.example.boardserver.dto;

public class PasswordChangeRequest {

    private String currentPassword;
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean hasBlankFields() {
        return isBlank(currentPassword) || isBlank(newPassword);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
