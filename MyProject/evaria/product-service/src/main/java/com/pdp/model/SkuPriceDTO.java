package com.pdp.model;

public class SkuPriceDTO {

    private String skuId;
    private String skuSalePrice;
    private String skuListPrice;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuSalePrice() {
        return skuSalePrice;
    }

    public void setSkuSalePrice(String skuSalePrice) {
        this.skuSalePrice = skuSalePrice;
    }

    public String getSkuListPrice() {
        return skuListPrice;
    }

    public void setSkuListPrice(String skuListPrice) {
        this.skuListPrice = skuListPrice;
    }
}
