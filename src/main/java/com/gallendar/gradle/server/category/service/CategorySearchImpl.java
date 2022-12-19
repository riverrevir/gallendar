package com.gallendar.gradle.server.category.service;

import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategorySearchImpl implements CategorySearch{
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryByCategoryTitle(String categoryTitle) {
        beNotInCategoryTitleSave(categoryTitle);
        return categoryRepository.findByCategoryTitle(categoryTitle);
    }
    private void beNotInCategoryTitleSave(String categoryTitle){
        if(!categoryRepository.existsByCategoryTitle(categoryTitle)){
            Category category= Category.builder().categoryTitle(categoryTitle).build();
            categoryRepository.save(category);
        }
    }
}
