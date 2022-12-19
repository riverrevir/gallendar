package com.gallendar.gradle.server.category.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryDeleteImpl implements CategoryDelete {
    private final BoardRepositoryCustomImpl boardRepositoryCustom;
    private final CategoryRepository categoryRepository;

    @Override
    public void delete(Board board) {
        final int count = boardRepositoryCustom.findByCategoryCount(board.getCategory().getCategoryId());
        if (count == 1) {
            Category category = categoryRepository.findByCategoryTitle(board.getCategory().getCategoryTitle());
            categoryRepository.delete(category);
        }
    }
}
