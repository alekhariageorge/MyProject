package com.plp.service;

import com.base.repository.model.RepositoryItem;
import com.productCatalog.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CatalogService {

    private final ProductCatalogRepository productCatalogRepository;

    public CatalogService(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    public RepositoryItem getCatalog(String catalogId) {
        return productCatalogRepository.getItemById("catalog", catalogId);
    }

    public List<RepositoryItem> getRootCategories(String catalogId) {
        RepositoryItem catalog = getCatalog(catalogId);
        if (catalog == null) {
            return Collections.emptyList();
        }
        return getItemsByIds("category", (String) catalog.getPropertyValue("rootCategories"));
    }

    public List<RepositoryItem> getCatalogProducts(String catalogId) {
        RepositoryItem catalog = getCatalog(catalogId);
        if (catalog == null) {
            return Collections.emptyList();
        }
        return getItemsByIds("product", (String) catalog.getPropertyValue("allProducts"));
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
