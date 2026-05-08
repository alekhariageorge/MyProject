package com.plp.model;

import com.base.repository.model.RepositoryItem;

public class ProductSummaryDTO {

    private String productId;
    private String displayName;
    private String description;
    private String brand;
    private String type;
    private String defaultSku;

    public ProductSummaryDTO() {}

    public ProductSummaryDTO(RepositoryItem product) {
        this.productId = product.getRepositoryId();
        this.displayName = (String) product.getPropertyValue("displayName");
        this.description = (String) product.getPropertyValue("description");
        this.brand = (String) product.getPropertyValue("brand");
        this.type = (String) product.getPropertyValue("type");
        this.defaultSku = (String) product.getPropertyValue("defaultSku");
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
