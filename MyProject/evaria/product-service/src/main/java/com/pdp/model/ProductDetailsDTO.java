package com.pdp.model;

import com.base.repository.model.RepositoryItem;

import java.util.List;

public class ProductDetailsDTO {

    private String productId;
    private String productName;
    private String productDescription;
    private String longDescription;
    private String brand;
    private String type;
    private String defaultSku;
    private String parentCategories;
    private String catalogs;
    private List<SkuDetailsDTO> skus;

    public ProductDetailsDTO() {}

    public ProductDetailsDTO(RepositoryItem product, List<RepositoryItem> skuItems) {
        if (product == null) {
            return;
        }

        this.productId = product.getRepositoryId();
        this.productName = (String) product.getPropertyValue("displayName");
        this.productDescription = (String) product.getPropertyValue("description");
        this.longDescription = (String) product.getPropertyValue("longDescription");
        this.brand = (String) product.getPropertyValue("brand");
        this.type = (String) product.getPropertyValue("type");
        this.defaultSku = (String) product.getPropertyValue("defaultSku");
        this.parentCategories = (String) product.getPropertyValue("parentCategories");
        this.catalogs = (String) product.getPropertyValue("catalogs");
        this.skus = skuItems.stream()
                .map(SkuDetailsDTO::new)
                .toList();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultSku() {
        return defaultSku;
    }

    public void setDefaultSku(String defaultSku) {
        this.defaultSku = defaultSku;
    }

    public String getParentCategories() {
        return parentCategories;
    }

    public void setParentCategories(String parentCategories) {
        this.parentCategories = parentCategories;
    }

    public String getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(String catalogs) {
        this.catalogs = catalogs;
    }

    public List<SkuDetailsDTO> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuDetailsDTO> skus) {
        this.skus = skus;
    }
}
