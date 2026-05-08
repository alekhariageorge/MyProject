package com.plp.droplet;

import com.base.repository.model.RepositoryItem;
import com.plp.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryLookup {

    private final CategoryService categoryService;

    public CategoryLookup(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    public RepositoryItem getCategory(String categoryId) {
        return categoryService.getCategory(categoryId);
    }

    public List<RepositoryItem> getChildCategories(String categoryId) {
        return categoryService.getChildCategories(categoryId);
    }
}
