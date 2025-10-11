package com.example.boardserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.LoginResponse;
import com.example.boardserver.dto.PasswordChangeRequest;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.UserDeleteRequest;
import com.example.boardserver.dto.UserLoginRequest;
import com.example.boardserver.exception.DuplicatedIdException;
import com.example.boardserver.exception.InvalidPasswordException;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<LoginResponse> register(@RequestBody UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeRegister(userDTO)) {
            return ResponseEntity.badRequest().body(LoginResponse.failure("필수 항목이 누락되었습니다."));
        }
        log.info("Registering user: {}", userDTO);
        try {
            userService.register(userDTO);
            boolean isAdmin = userDTO.getStatus() == UserDTO.Status.ADMIN;
            return ResponseEntity.ok(LoginResponse.success(userDTO.getUserId(), isAdmin, "회원가입이 완료되었습니다."));
        } catch (DuplicatedIdException duplicatedIdException) {
            log.warn("Duplicated user id detected: {}", userDTO.getUserId(), duplicatedIdException);
            return ResponseEntity.status(409)
                    .body(LoginResponse.failure(duplicatedIdException.getMessage()));
        } catch (RuntimeException runtimeException) {
            log.error("Failed to register user: {}", userDTO.getUserId(), runtimeException);
            return ResponseEntity.internalServerError()
                    .body(LoginResponse.failure("회원가입 처리 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest loginRequest, HttpSession session) {
        log.info("Logging in user: {}", loginRequest.getUserId());
        UserDTO user = userService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(LoginResponse.failure("아이디 또는 비밀번호가 일치하지 않습니다."));
        }
        SessionUtil.clearLoginInfo(session);

        UserDTO.Status status = user.getStatus();

        if (status == UserDTO.Status.ADMIN) {
            SessionUtil.setLoginAdminId(session, user.getUserId());
            SessionUtil.setLoginMemberId(session, user.getUserId());
            return ResponseEntity.ok(LoginResponse.success(user, "관리자 로그인 성공"));
        }

        if (status == null || status == UserDTO.Status.DEFAULT) {
            SessionUtil.setLoginMemberId(session, user.getUserId());
            SessionUtil.removeLoginAdminId(session);
            return ResponseEntity.ok(LoginResponse.success(user, "로그인 성공"));
        }

        SessionUtil.clearLoginInfo(session);
        return ResponseEntity.status(403)
                .body(LoginResponse.failure("알 수 없는 사용자 상태입니다."));
    }

    @GetMapping("my-info")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<UserDTO> getMyInfo(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        UserDTO user = userService.getUserInfo(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        boolean admin = SessionUtil.getLoginAdminId(session) != null;
        SessionUtil.clear(session);
        return ResponseEntity.ok(LoginResponse.success(userId, admin, "로그아웃 되었습니다."));
    }

    @PatchMapping("/password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest,
            HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (passwordChangeRequest == null || passwordChangeRequest.hasBlankFields()) {
            return ResponseEntity.badRequest().body(LoginResponse.failure("필수 항목이 누락되었습니다."));
        }
        try {
            userService.updatePassword(userId, passwordChangeRequest.getCurrentPassword(),
                    passwordChangeRequest.getNewPassword());
            boolean admin = SessionUtil.getLoginAdminId(session) != null;
            return ResponseEntity.ok(LoginResponse.success(userId, admin, "비밀번호가 성공적으로 변경되었습니다."));
        } catch (InvalidPasswordException invalidPasswordException) {
            log.warn("Failed to change password for user {}", userId, invalidPasswordException);
            return ResponseEntity.status(403).body(LoginResponse.failure(invalidPasswordException.getMessage()));
        } catch (RuntimeException runtimeException) {
            log.error("Unexpected error while changing password for user {}", userId, runtimeException);
            return ResponseEntity.internalServerError()
                    .body(LoginResponse.failure("비밀번호 변경 처리 중 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/delete")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> deleteUser(@RequestBody UserDeleteRequest deleteRequest, HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (deleteRequest == null || deleteRequest.hasBlankPassword()) {
            return ResponseEntity.badRequest().body(LoginResponse.failure("필수 항목이 누락되었습니다."));
        }
        try {
            userService.deleteId(userId, deleteRequest.getPassword());
            SessionUtil.clear(session);
            return ResponseEntity.ok(LoginResponse.success(userId, false, "회원 탈퇴가 완료되었습니다."));
        } catch (InvalidPasswordException invalidPasswordException) {
            log.warn("Failed to delete user {} due to password mismatch", userId, invalidPasswordException);
            return ResponseEntity.status(403).body(LoginResponse.failure(invalidPasswordException.getMessage()));
        } catch (RuntimeException runtimeException) {
            log.error("Unexpected error while deleting user {}", userId, runtimeException);
            return ResponseEntity.internalServerError()
                    .body(LoginResponse.failure("회원 탈퇴 처리 중 오류가 발생했습니다."));
        }
    }
}
