package com.pdp.service;

import com.base.repository.model.RepositoryItem;
import com.productCatalog.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductCatalogRepository productRepository;

    public ProductService(ProductCatalogRepository productRepository) {

        this.productRepository = productRepository;
    }

    public RepositoryItem getProduct(final String productId) {
        return productRepository.findProductById(productId);
    }

    public RepositoryItem getProductFromSku(final String skuId) {
        return productRepository.getProductBySkuId(skuId);
    }

    public RepositoryItem getSku(final String skuId) {
        return productRepository.findSkuById(skuId);
    }

    public List<RepositoryItem> getSkusForProduct(String productId) {
        return productRepository.findSkusByProductId(productId);
    }

    public RepositoryItem createProduct(String productId, Map<String, Object> propertyValues) {
        return productRepository.saveProduct(productId, propertyValues);
    }

    public RepositoryItem createSku(String skuId, Map<String, Object> propertyValues) {
        return productRepository.saveSku(skuId, propertyValues);
    }
}
