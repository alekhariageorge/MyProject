package com.pdp.controller;

import com.base.repository.model.RepositoryItem;
import com.pdp.model.PdpPageDTO;
import com.pdp.model.ProductDetailsDTO;
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
    public PdpPageDTO getSkuDetails(@PathVariable String skuId) {
        return productService.getPdpPageForSku(skuId);
    }

    @GetMapping("/product/{productId}")
    public ProductDetailsDTO getProductDetails(@PathVariable String productId) {

        RepositoryItem product = productService.getProduct(productId);
        List<RepositoryItem> skus = productService.getSkusForProduct(productId);
        return new ProductDetailsDTO(product, skus);
    }
}
