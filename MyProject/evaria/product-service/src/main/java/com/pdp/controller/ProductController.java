package com.pdp.controller;

import com.base.repository.model.RepositoryItem;
import com.pdp.model.ProductDetailsDTO;
import com.pdp.model.SkuProductDTO;
import com.pdp.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdp")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @GetMapping("/sku/{skuId}")
    public SkuProductDTO getSkuDetails(@PathVariable String skuId) {

        RepositoryItem sku = productService.getSku(skuId);
        RepositoryItem product = productService.getProductFromSku(skuId);
        return new SkuProductDTO(sku, product);
    }

    @GetMapping("/product/{productId}")
    public ProductDetailsDTO getProductDetails(@PathVariable String productId) {

        RepositoryItem product = productService.getProduct(productId);
        List<RepositoryItem> skus = productService.getSkusForProduct(productId);
        return new ProductDetailsDTO(product, skus);
    }
}
