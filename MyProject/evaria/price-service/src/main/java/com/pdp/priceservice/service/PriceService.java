package com.pdp.priceservice.service;

import com.base.repository.model.RepositoryItem;
import com.pdp.priceservice.repository.PriceListRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final PriceListRepository priceListRepository;

    public PriceService(PriceListRepository priceListRepository) {

        this.priceListRepository = priceListRepository;
    }
    public RepositoryItem getSkuPrice(final String skuId) {

        return priceListRepository.findPriceBySkuId(skuId);
    }
}
