package com.pdp.model;

import com.base.repository.model.RepositoryItem;

import java.util.List;

public class PdpPageDTO {

    private String productId;
    private String productName;
    private String productDescription;
    private String longDescription;
    private String selectedSkuId;
    private String selectedSkuName;
    private String color;
    private String size;
    private InventoryDTO inventory;
    private SkuPriceDTO price;
    private MediaDTO mainImage;
    private List<PdpSkuSummaryDTO> siblingSkus;

    public PdpPageDTO() {
    }

    public PdpPageDTO(RepositoryItem product,
                      RepositoryItem selectedSku,
                      InventoryDTO inventory,
                      SkuPriceDTO price,
                      MediaDTO mainImage,
                      List<PdpSkuSummaryDTO> siblingSkus) {
        this.productId = product.getRepositoryId();
        this.productName = (String) product.getPropertyValue("displayName");
        this.productDescription = (String) product.getPropertyValue("description");
        this.longDescription = (String) product.getPropertyValue("longDescription");
        this.selectedSkuId = selectedSku.getRepositoryId();
        this.selectedSkuName = (String) selectedSku.getPropertyValue("displayName");
        this.color = (String) selectedSku.getPropertyValue("color");
        this.size = (String) selectedSku.getPropertyValue("size");
        this.inventory = inventory;
        this.price = price;
        this.mainImage = mainImage;
        this.siblingSkus = siblingSkus;
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

    public String getSelectedSkuId() {
        return selectedSkuId;
    }

    public void setSelectedSkuId(String selectedSkuId) {
        this.selectedSkuId = selectedSkuId;
    }

    public String getSelectedSkuName() {
        return selectedSkuName;
    }

    public void setSelectedSkuName(String selectedSkuName) {
        this.selectedSkuName = selectedSkuName;
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

    public InventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

    public SkuPriceDTO getPrice() {
        return price;
    }

    public void setPrice(SkuPriceDTO price) {
        this.price = price;
    }

    public MediaDTO getMainImage() {
        return mainImage;
    }

    public void setMainImage(MediaDTO mainImage) {
        this.mainImage = mainImage;
    }

    public List<PdpSkuSummaryDTO> getSiblingSkus() {
        return siblingSkus;
    }

    public void setSiblingSkus(List<PdpSkuSummaryDTO> siblingSkus) {
        this.siblingSkus = siblingSkus;
    }
}
