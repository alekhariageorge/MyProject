package com.pdp.service;

import com.base.repository.model.RepositoryItem;
import com.pdp.client.InventoryClient;
import com.pdp.client.MediaClient;
import com.pdp.client.PriceClient;
import com.pdp.model.PdpPageDTO;
import com.pdp.model.PdpSkuSummaryDTO;
import com.productCatalog.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductCatalogRepository productRepository;
    private final InventoryClient inventoryClient;
    private final PriceClient priceClient;
    private final MediaClient mediaClient;

    public ProductService(ProductCatalogRepository productRepository,
                          InventoryClient inventoryClient,
                          PriceClient priceClient,
                          MediaClient mediaClient) {
        this.productRepository = productRepository;
        this.inventoryClient = inventoryClient;
        this.priceClient = priceClient;
        this.mediaClient = mediaClient;
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

    public PdpPageDTO getPdpPageForSku(String skuId) {
        RepositoryItem selectedSku = getSku(skuId);
        RepositoryItem product = getProductFromSku(skuId);
        List<PdpSkuSummaryDTO> siblingSkus = getSkusForProduct(product.getRepositoryId())
                .stream()
                .map(sku -> new PdpSkuSummaryDTO(
                        sku,
                        priceClient.getPrice(sku.getRepositoryId()),
                        mediaClient.getSkuMainImage(sku.getRepositoryId())
                ))
                .toList();

        return new PdpPageDTO(
                product,
                selectedSku,
                inventoryClient.getInventory(skuId),
                priceClient.getPrice(skuId),
                mediaClient.getSkuMainImage(skuId),
                siblingSkus
        );
    }

    public RepositoryItem createProduct(String productId, Map<String, Object> propertyValues) {
        return productRepository.saveProduct(productId, propertyValues);
    }

    public RepositoryItem createSku(String skuId, Map<String, Object> propertyValues) {
        return productRepository.saveSku(skuId, propertyValues);
    }
}
