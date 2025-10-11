package com.example.boardserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {

    private String name;

    public boolean hasInvalidName() {
        return name == null || name.trim().isEmpty();
    }
}
