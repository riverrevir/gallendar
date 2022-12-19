package com.gallendar.gradle.server.category.service;

import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.dto.CategoryListResponse;

import java.util.List;

public interface CategorySearch {
    Category getCategoryByCategoryTitle(String categoryTitle);

    List<CategoryListResponse> getCategoryListByMemberId(String memberId);
}
