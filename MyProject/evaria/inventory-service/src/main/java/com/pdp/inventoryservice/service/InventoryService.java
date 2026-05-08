package com.pdp.inventoryservice.service;

import com.base.repository.model.RepositoryItem;
import com.pdp.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public RepositoryItem getInventoryForSku(final String skuId) {
        return inventoryRepository.findInventoryBySkuId(skuId);
    }

    public List<RepositoryItem> getInventoryByStatus(final String availabilityStatus) {
        return inventoryRepository.findInventoryByStatus(availabilityStatus);
    }
}
