package com.gallendar.gradle.server.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryListResponse {

    private String categoryTitle;

    public static CategoryListResponse from(String categoryTitle) {

        return CategoryListResponse.builder()
                .categoryTitle(categoryTitle)
                .build();
    }
}