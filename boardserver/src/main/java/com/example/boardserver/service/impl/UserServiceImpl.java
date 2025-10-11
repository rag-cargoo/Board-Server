package com.example.boardserver.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.exception.DuplicatedIdException;
import com.example.boardserver.exception.InvalidPasswordException;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SHA256Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public void register(UserDTO userProfile) {
        boolean dupleIdResult = isDuplicatedId(userProfile.getUserId());
        if (dupleIdResult) {
            throw new DuplicatedIdException("중복된 아이디 입니다.");
        }

        userProfile.setCreateTime(new Date());
        if (userProfile.getStatus() == null) {
            userProfile.setStatus(UserDTO.Status.DEFAULT);
        }
        userProfile.setAdmin(userProfile.getStatus() == UserDTO.Status.ADMIN);
        userProfile.setPassword(SHA256Util.encryptionSHA256(userProfile.getPassword()));
        int insertCount = userProfileMapper.insertUserProfile(userProfile);
        if (insertCount != 1) {
            log.error("insert register ERROR! {}", userProfile);
            throw new RuntimeException("회원가입에 실패하였습니다.");
        }
    }

    @Override
    public UserDTO login(String id, String password) {
        String encryptedPassword = SHA256Util.encryptionSHA256(password);
        UserDTO user = userProfileMapper.findByIdAndPassword(id, encryptedPassword);
        if (user != null) {
            if (user.isAdmin()) {
                user.setStatus(UserDTO.Status.ADMIN);
            } else if (user.getStatus() == null) {
                user.setStatus(UserDTO.Status.DEFAULT);
            }
        }
        return user;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.idCheck(id) == 1;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        UserDTO user = userProfileMapper.getUserProfile(userId);
        if (user == null) {
            return null;
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String encryptedBeforePassword = SHA256Util.encryptionSHA256(beforePassword);
        UserDTO user = userProfileMapper.findByIdAndPassword(id, encryptedBeforePassword);
        if (user == null) {
            throw new InvalidPasswordException("기존 비밀번호가 일치하지 않습니다.");
        }
        String encryptedAfterPassword = SHA256Util.encryptionSHA256(afterPassword);
        int updateCount = userProfileMapper.updatePassword(id, encryptedAfterPassword);
        if (updateCount != 1) {
            log.error("update password ERROR! {}", user);
            throw new RuntimeException("비밀번호 변경에 실패하였습니다.");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String encryptedPassword = SHA256Util.encryptionSHA256(password);
        UserDTO user = userProfileMapper.findByIdAndPassword(id, encryptedPassword);
        if (user == null) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        int deleteCount = userProfileMapper.deleteUserProfile(id);
        if (deleteCount != 1) {
            log.error("delete user ERROR! {}", user);
            throw new RuntimeException("회원 탈퇴에 실패하였습니다.");
        }
    }

}
