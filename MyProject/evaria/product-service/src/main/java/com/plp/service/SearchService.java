package com.plp.service;

import com.base.repository.model.RepositoryItem;
import com.productCatalog.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class SearchService {

    private final ProductCatalogRepository productCatalogRepository;

    public SearchService(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    public List<RepositoryItem> searchProducts(String term) {
        if (term == null || term.isBlank()) {
            return List.of();
        }

        String normalizedTerm = term.toLowerCase(Locale.ROOT);
        Map<String, RepositoryItem> productItems =
                productCatalogRepository.getRepositoryItems().get("product");
        Map<String, RepositoryItem> matchingProducts = new LinkedHashMap<>();

        for (String productId : productItems.keySet()) {
            RepositoryItem product = productItems.get(productId);
            if (matches(product, normalizedTerm)) {
                matchingProducts.put(product.getRepositoryId(), product);
            }
        }

        Map<String, RepositoryItem> skuItems =
                productCatalogRepository.getRepositoryItems().get("sku");
        for (String skuId : skuItems.keySet()) {
            RepositoryItem sku = skuItems.get(skuId);
            if (matchesSku(sku, normalizedTerm)) {
                RepositoryItem product =
                        productCatalogRepository.getItemById("product", (String) sku.getPropertyValue("productId"));
                if (product != null) {
                    matchingProducts.put(product.getRepositoryId(), product);
                }
            }
        }

        Map<String, RepositoryItem> categoryItems =
                productCatalogRepository.getRepositoryItems().get("category");
        for (String categoryId : categoryItems.keySet()) {
            RepositoryItem category = categoryItems.get(categoryId);
            if (matchesCategory(category, normalizedTerm)) {
                addCategoryProducts(category, matchingProducts);
            }
        }

        return List.copyOf(matchingProducts.values());
    }

    private boolean matches(RepositoryItem product, String normalizedTerm) {
        return contains(product.getRepositoryId(), normalizedTerm)
                || contains((String) product.getPropertyValue("displayName"), normalizedTerm)
                || contains((String) product.getPropertyValue("description"), normalizedTerm)
                || contains((String) product.getPropertyValue("longDescription"), normalizedTerm)
                || contains((String) product.getPropertyValue("brand"), normalizedTerm)
                || contains((String) product.getPropertyValue("type"), normalizedTerm);
    }

    private boolean matchesSku(RepositoryItem sku, String normalizedTerm) {
        return contains(sku.getRepositoryId(), normalizedTerm)
                || contains((String) sku.getPropertyValue("displayName"), normalizedTerm)
                || contains((String) sku.getPropertyValue("description"), normalizedTerm)
                || contains((String) sku.getPropertyValue("longDescription"), normalizedTerm)
                || contains((String) sku.getPropertyValue("color"), normalizedTerm)
                || contains((String) sku.getPropertyValue("size"), normalizedTerm);
    }

    private boolean matchesCategory(RepositoryItem category, String normalizedTerm) {
        return contains(category.getRepositoryId(), normalizedTerm)
                || contains((String) category.getPropertyValue("displayName"), normalizedTerm)
                || contains((String) category.getPropertyValue("description"), normalizedTerm)
                || contains((String) category.getPropertyValue("longDescription"), normalizedTerm);
    }

    private void addCategoryProducts(RepositoryItem category, Map<String, RepositoryItem> matchingProducts) {
        addProductsByIds((String) category.getPropertyValue("childProducts"), matchingProducts);

        String childCategories = (String) category.getPropertyValue("childCategories");
        if (childCategories == null || childCategories.isBlank()) {
            return;
        }

        for (String childCategoryId : childCategories.split(",")) {
            RepositoryItem childCategory =
                    productCatalogRepository.getItemById("category", childCategoryId.trim());
            if (childCategory != null) {
                addCategoryProducts(childCategory, matchingProducts);
            }
        }
    }

    private void addProductsByIds(String productIds, Map<String, RepositoryItem> matchingProducts) {
        if (productIds == null || productIds.isBlank()) {
            return;
        }

        for (String productId : productIds.split(",")) {
            RepositoryItem product =
                    productCatalogRepository.getItemById("product", productId.trim());
            if (product != null) {
                matchingProducts.put(product.getRepositoryId(), product);
            }
        }
    }

    private boolean contains(String value, String normalizedTerm) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(normalizedTerm);
    }
}
