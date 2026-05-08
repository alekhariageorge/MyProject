package com.pdp.model;

import com.base.repository.model.RepositoryItem;

public class PdpSkuSummaryDTO {

    private String skuId;
    private String skuName;
    private String color;
    private String size;
    private SkuPriceDTO price;
    private MediaDTO thumbnailImage;

    public PdpSkuSummaryDTO() {
    }

    public PdpSkuSummaryDTO(RepositoryItem sku, SkuPriceDTO price, MediaDTO thumbnailImage) {
        this.skuId = sku.getRepositoryId();
        this.skuName = (String) sku.getPropertyValue("displayName");
        this.color = (String) sku.getPropertyValue("color");
        this.size = (String) sku.getPropertyValue("size");
        this.price = price;
        this.thumbnailImage = thumbnailImage;
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

    public SkuPriceDTO getPrice() {
        return price;
    }

    public void setPrice(SkuPriceDTO price) {
        this.price = price;
    }

    public MediaDTO getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(MediaDTO thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
}
