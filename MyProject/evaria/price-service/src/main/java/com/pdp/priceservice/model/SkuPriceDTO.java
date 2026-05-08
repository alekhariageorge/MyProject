package com.pdp.priceservice.model;

import com.base.repository.model.RepositoryItem;

public class SkuPriceDTO   {

    private String skuId;
    private String skuSalePrice;
    private String skuListPrice;
    public SkuPriceDTO() {}

    public SkuPriceDTO(RepositoryItem itemPrice) {
        final String currencyCode = getStringValue(itemPrice, "currencyCode");
        final String listPrice = formatPrice(currencyCode, getStringValue(itemPrice, "listPrice"));
        final String salePrice = formatPrice(currencyCode, getStringValue(itemPrice, "salePrice"));
        final String skuId = getStringValue(itemPrice, "skuId");
        this.skuListPrice = listPrice;
        this.skuSalePrice = salePrice;
        this.skuId = skuId;
    }

    private String getStringValue(RepositoryItem itemPrice, String propertyName) {
        Object value = itemPrice.getPropertyValue(propertyName);
        return value == null ? "" : value.toString();
    }

    private String formatPrice(String currencyCode, String price) {
        if (price.isBlank()) {
            return null;
        }
        if (currencyCode.isBlank()) {
            return price;
        }
        return currencyCode + " " + price;
    }

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
