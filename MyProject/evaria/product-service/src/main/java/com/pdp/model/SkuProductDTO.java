package com.pdp.model;

import com.base.repository.model.RepositoryItem;

public class SkuProductDTO   {

    private String skuId;
    private String skuName;
    private String skuDescription;
    private String productId;
    private String productName;
    private String productDescription;
    private String color;
    private String size;

    public SkuProductDTO() {}

    public SkuProductDTO(RepositoryItem sku, RepositoryItem product) {
        this.skuId = sku.getRepositoryId();
        this.skuName = (String) sku.getPropertyValue("displayName");
        this.skuDescription = (String) sku.getPropertyValue("description");
        this.productId = product.getRepositoryId();
        this.productName = (String) product.getPropertyValue("displayName");
        this.productDescription = (String) product.getPropertyValue("description");
        this.color = (String) sku.getPropertyValue("color");
        this.size = (String) sku.getPropertyValue("size");
        System.out.println("SKUID: " + skuId);
        System.out.println("NAME: " + skuName);
        System.out.println("PRODUCT NAME: " + productName);
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDescription() {
        return skuDescription;
    }

    public void setSkuDescription(String skuDescription) {
        this.skuDescription = skuDescription;
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
