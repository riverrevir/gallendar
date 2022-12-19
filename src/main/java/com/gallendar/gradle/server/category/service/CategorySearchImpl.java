package com.gallendar.gradle.server.category.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.domain.CategoryRepository;
import com.gallendar.gradle.server.category.dto.CategoryListResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CategorySearchImpl implements CategorySearch {
    private final CategoryRepository categoryRepository;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;

    @Override
    public Category getCategoryByCategoryTitle(String categoryTitle) {
        beNotInCategoryTitleSave(categoryTitle);
        return categoryRepository.findByCategoryTitle(categoryTitle);
    }

    @Override
    public List<CategoryListResponse> getCategoryListByMemberId(String memberId) {
        List<CategoryListResponse> categoryList = new ArrayList<>();
        List<Board> boards = boardRepositoryCustom.findByCategory(memberId);
        List<String> distinctCategory = boards.stream()
                .map(board -> board.getCategory().getCategoryTitle()).distinct().collect(Collectors.toList());
        distinctCategory.forEach(categoryTitle -> {
            categoryList.add(CategoryListResponse.from(categoryTitle));
        });
        return categoryList;
    }

    private void beNotInCategoryTitleSave(String categoryTitle) {
        if (!categoryRepository.existsByCategoryTitle(categoryTitle)) {
            Category category = Category.builder().categoryTitle(categoryTitle).build();
            categoryRepository.save(category);
        }
    }
}
