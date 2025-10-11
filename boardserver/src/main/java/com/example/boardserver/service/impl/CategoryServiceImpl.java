package com.example.boardserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.mapper.CategoryMapper;
import com.example.boardserver.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if (accountId != null) {
            log.info("Register category request by {} :: {}", accountId, categoryDTO);
            categoryMapper.register(categoryDTO);
        } else {
            log.error("register ERROR! {}", categoryDTO);
            throw new RuntimeException(
                    "카테고리 등록에 실패하였습니다. Category register 메서드를 호출할 때 accountId가 null입니다." + categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) {
            log.info("Update category :: {}", categoryDTO);
            categoryMapper.update(categoryDTO);
        } else {
            log.error("update ERROR! {}", categoryDTO);
            throw new RuntimeException("카테고리 수정에 실패하였습니다." + categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) {
            log.info("Delete category :: id={}", categoryId);
            categoryMapper.delete(categoryId);
        } else {
            log.error("delete ERROR! {}", categoryId);
            throw new RuntimeException("카테고리 삭제에 실패하였습니다." + categoryId);
        }
    }

}
