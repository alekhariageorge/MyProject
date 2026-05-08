package com.productCatalog.repository;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductCatalogRepository extends RepositoryInfo {

    public ProductCatalogRepository() {
        super("repository/repository-definition.xml");
    }

    public RepositoryItem findProductById(String productId) {
        return getItemById("product", productId);
    }

    public RepositoryItem findSkuById(String skuId) {
        return getItemById("sku", skuId);
    }

    public List<RepositoryItem> findSkusByProductId(String productId) {
        Map<String, RepositoryItem> skuItems = getRepositoryItems().get("sku");
        List<RepositoryItem> skus = new ArrayList<>();
        for (String skuItemId : skuItems.keySet()) {
            RepositoryItem skuItem = skuItems.get(skuItemId);
            if (productId.equalsIgnoreCase((String) skuItem.getPropertyValue("productId"))) {
                skus.add(skuItem);
            }
        }
        return skus;
    }

    public RepositoryItem getProductBySkuId(String skuId) {
        RepositoryItem sku = findSkuById(skuId);
        final String productId = (String) sku.getPropertyValue("productId");
        return findProductById(productId);
    }

    public RepositoryItem saveProduct(String productId, Map<String, Object> propertyValues) {
        addItem(productId, "product", propertyValues);
        return findProductById(productId);
    }

    public RepositoryItem saveSku(String skuId, Map<String, Object> propertyValues) {
        addItem(skuId, "sku", propertyValues);
        return findSkuById(skuId);
    }
}
