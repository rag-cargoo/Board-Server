package com.example.boardserver.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    public enum Status {
        DEFAULT, ADMIN, DELETED, BLOCKED
    }

    private int id;
    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String nickName;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

    public static boolean hasNullDataBeforeRegister(UserDTO user) {
        if (user == null) {
            return true;
        }
        return isBlank(user.getUserId())
                || isBlank(user.getPassword())
                || isBlank(user.getName())
                || isBlank(user.getEmail())
                || isBlank(user.getPhone())
                || isBlank(user.getAddress());
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
