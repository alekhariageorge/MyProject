package com.pdp.model;

import com.base.repository.model.RepositoryItem;

public class SkuDetailsDTO {

    private String skuId;
    private String skuName;
    private String skuDescription;
    private String longDescription;
    private String color;
    private String size;
    private String available;

    public SkuDetailsDTO() {}

    public SkuDetailsDTO(RepositoryItem sku) {
        this.skuId = sku.getRepositoryId();
        this.skuName = (String) sku.getPropertyValue("displayName");
        this.skuDescription = (String) sku.getPropertyValue("description");
        this.longDescription = (String) sku.getPropertyValue("longDescription");
        this.color = (String) sku.getPropertyValue("color");
        this.size = (String) sku.getPropertyValue("size");
        this.available = (String) sku.getPropertyValue("available");
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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
