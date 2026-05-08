package com.plp.service;

import com.base.repository.model.RepositoryItem;
import com.productCatalog.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    private final ProductCatalogRepository productCatalogRepository;

    public CategoryService(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    public RepositoryItem getCategory(String categoryId) {
        return productCatalogRepository.getItemById("category", categoryId);
    }

    public List<RepositoryItem> getChildProducts(String categoryId) {
        RepositoryItem category = getCategory(categoryId);
        if (category == null) {
            return Collections.emptyList();
        }
        return getItemsByIds("product", (String) category.getPropertyValue("childProducts"));
    }

    public List<RepositoryItem> getChildCategories(String categoryId) {
        RepositoryItem category = getCategory(categoryId);
        if (category == null) {
            return Collections.emptyList();
        }
        return getItemsByIds("category", (String) category.getPropertyValue("childCategories"));
    }

    private List<RepositoryItem> getItemsByIds(String itemDescriptorName, String itemIds) {
        if (itemIds == null || itemIds.isBlank()) {
            return Collections.emptyList();
        }

        return List.of(itemIds.split(","))
                .stream()
                .map(String::trim)
                .map(itemId -> productCatalogRepository.getItemById(itemDescriptorName, itemId))
                .filter(item -> item != null)
                .toList();
    }
}
