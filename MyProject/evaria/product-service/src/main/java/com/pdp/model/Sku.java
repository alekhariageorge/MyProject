package com.pdp.model;

public class Sku {
    private String repositoryId;
    private String displayName;
    private String description;
    private String productId;
    private String color;
    private String size;

    public Sku() {}

    public Sku(String skuId,
               String displayName,
               String description,
               String productId,
               String color,
               String size) {

        this.repositoryId = skuId;
        this.displayName = displayName;
        this.description = description;
        this.productId = productId;
        this.color = color;
        this.size = size;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
