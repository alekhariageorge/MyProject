package com.pdp.priceservice.controller;

import com.base.repository.model.RepositoryItem;
import com.pdp.priceservice.model.SkuPriceDTO;
import com.pdp.priceservice.service.PriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pdp")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/sku/{skuId}")
    public SkuPriceDTO getSkuPrice(@PathVariable String skuId) {
        RepositoryItem skuPrice = priceService.getSkuPrice(skuId);
        return new SkuPriceDTO(skuPrice);
    }
}