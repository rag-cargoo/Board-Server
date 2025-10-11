package com.example.boardserver.aop;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.boardserver.dto.LoginResponse;
import com.example.boardserver.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class LoginCheckAspect {

    private static final String NEED_LOGIN_MESSAGE = "로그인이 필요합니다.";
    private static final String NEED_ADMIN_MESSAGE = "관리자 권한이 필요합니다.";

    @Around("@annotation(loginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = (HttpSession) Arrays.stream(joinPoint.getArgs())
                .filter(HttpSession.class::isInstance)
                .findFirst()
                .orElse(null);

        if (session == null) {
            throw new IllegalStateException("@LoginCheck is used on a method without HttpSession parameter");
        }

        String userId = SessionUtil.getLoginMemberId(session);
        if (userId == null) {
            return unauthorizedResponse(joinPoint, NEED_LOGIN_MESSAGE);
        }

        if (loginCheck.type() == LoginCheck.UserType.ADMIN) {
            String adminId = SessionUtil.getLoginAdminId(session);
            if (adminId == null) {
                return unauthorizedResponse(joinPoint, NEED_ADMIN_MESSAGE, HttpStatus.FORBIDDEN);
            }
        }

        return joinPoint.proceed();
    }

    private Object unauthorizedResponse(ProceedingJoinPoint joinPoint, String message) {
        return unauthorizedResponse(joinPoint, message, HttpStatus.UNAUTHORIZED);
    }

    private Object unauthorizedResponse(ProceedingJoinPoint joinPoint, String message, HttpStatus status) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = signature.getReturnType();

        if (ResponseEntity.class.isAssignableFrom(returnType)) {
            ResponseEntity<?> response = buildResponseEntity(signature, status, message);
            return returnType.cast(response);
        }

        throw new IllegalStateException("@LoginCheck currently supports methods returning ResponseEntity only");
    }

    private ResponseEntity<?> buildResponseEntity(MethodSignature signature, HttpStatus status, String message) {
        Type genericReturnType = signature.getMethod().getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType parameterizedType) {
            Type actualType = parameterizedType.getActualTypeArguments()[0];
            if (actualType == LoginResponse.class) {
                return ResponseEntity.status(status)
                        .body(LoginResponse.failure(message));
            }
        }
        return ResponseEntity.status(status).build();
    }
}
