package com.example.boardserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.CategoryRequest;
import com.example.boardserver.service.CategoryService;
import com.example.boardserver.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/categories")
@Log4j2
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public ResponseEntity<Void> registerCategory(@RequestBody CategoryDTO categoryDTO, HttpSession session) {
        if (categoryDTO == null || isBlank(categoryDTO.getName())) {
            return ResponseEntity.badRequest().build();
        }
        String adminId = SessionUtil.getLoginAdminId(session);
        log.info("Register category request from {} :: {}", adminId, categoryDTO);
        categoryService.register(adminId, categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public ResponseEntity<Void> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequest categoryRequest,
            HttpSession session) {
        if (categoryId <= 0 || categoryRequest == null || categoryRequest.hasInvalidName()) {
            return ResponseEntity.badRequest().build();
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);
        categoryDTO.setName(categoryRequest.getName());
        log.info("Update category request :: {}", categoryDTO);
        categoryService.update(categoryDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId, HttpSession session) {
        if (categoryId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Delete category request :: id={}", categoryId);
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
