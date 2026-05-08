package com.plp.model;

import com.base.repository.model.RepositoryItem;

import java.util.List;

public class PlpPageDTO {

    private CatalogDTO catalog;
    private CategoryDTO category;
    private String searchTerm;
    private List<CategoryDTO> childCategories;
    private List<ProductSummaryDTO> products;

    public CatalogDTO getCatalog() {
        return catalog;
    }

    public void setCatalog(RepositoryItem catalog) {
        this.catalog = catalog == null ? null : new CatalogDTO(catalog);
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(RepositoryItem category) {
        this.category = category == null ? null : new CategoryDTO(category);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public List<CategoryDTO> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<RepositoryItem> childCategories) {
        this.childCategories = childCategories.stream()
                .map(CategoryDTO::new)
                .toList();
    }

    public List<ProductSummaryDTO> getProducts() {
        return products;
    }

    public void setProducts(List<RepositoryItem> products) {
        this.products = products.stream()
                .map(ProductSummaryDTO::new)
                .toList();
    }
}
