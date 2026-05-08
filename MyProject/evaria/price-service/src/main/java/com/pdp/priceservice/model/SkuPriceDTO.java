package com.pdp.priceservice.model;

import com.base.repository.model.RepositoryItem;

public class SkuPriceDTO   {

    private String skuId;
    private String skuSalePrice;
    private String skuListPrice;
    public SkuPriceDTO() {}

    public SkuPriceDTO(RepositoryItem itemPrice) {
        final String listPrice = (String)itemPrice.getPropertyValue("currencyCode") +
                itemPrice.getPropertyValue("listPrice");
        final String salePrice = (String)itemPrice.getPropertyValue("currencyCode") +
                itemPrice.getPropertyValue("salePrice");
        final String skuId = (String)itemPrice.getPropertyValue("skuId");
        this.skuListPrice = listPrice;
        this.skuSalePrice = salePrice;
        this.skuId = skuId;
        System.out.println("LIST PRICE: " + listPrice);
        System.out.println("SALE PRICE: " + salePrice);
        System.out.println("SKU ID: " + skuId);
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