package com.gallendar.gradle.server.category.service;

import com.gallendar.gradle.server.category.domain.Category;

public interface CategorySearch {
    Category getCategoryByCategoryTitle(String categoryTitle);
}
