package com.pdp.model;

public class InventoryDTO {

    private String inventoryId;
    private String skuId;
    private String stockLevel;
    private String availabilityStatus;
    private String backorderLevel;
    private String preorderLevel;
    private String stockThreshold;
    private String storeId;
    private String locationId;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getBackorderLevel() {
        return backorderLevel;
    }

    public void setBackorderLevel(String backorderLevel) {
        this.backorderLevel = backorderLevel;
    }

    public String getPreorderLevel() {
        return preorderLevel;
    }

    public void setPreorderLevel(String preorderLevel) {
        this.preorderLevel = preorderLevel;
    }

    public String getStockThreshold() {
        return stockThreshold;
    }

    public void setStockThreshold(String stockThreshold) {
        this.stockThreshold = stockThreshold;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
