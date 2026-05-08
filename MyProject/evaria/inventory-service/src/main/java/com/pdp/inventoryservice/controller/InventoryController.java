package com.pdp.inventoryservice.controller;

import com.base.repository.model.RepositoryItem;
import com.pdp.inventoryservice.model.InventoryDTO;
import com.pdp.inventoryservice.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/sku/{skuId}")
    public InventoryDTO getSkuInventory(@PathVariable String skuId) {
        RepositoryItem inventory = inventoryService.getInventoryForSku(skuId);
        return new InventoryDTO(inventory);
    }

    @GetMapping("/status/{availabilityStatus}")
    public List<InventoryDTO> getInventoryByStatus(@PathVariable String availabilityStatus) {
        return inventoryService.getInventoryByStatus(availabilityStatus)
                .stream()
                .map(InventoryDTO::new)
                .toList();
    }
}
