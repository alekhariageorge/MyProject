package com.plp.droplet;

import com.base.repository.model.RepositoryItem;
import com.plp.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductList {

    private final CategoryService categoryService;

    public ProductList(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<RepositoryItem> getProducts(String categoryId) {
        return categoryService.getChildProducts(categoryId);
    }
}
