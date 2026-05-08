package com.pdp.inventoryservice.repository;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryRepository extends RepositoryInfo {

    public InventoryRepository() {
        super("repository/repository-definition.xml");
    }

    public RepositoryItem findInventoryBySkuId(String skuId) {
        Map<String, RepositoryItem> inventoryItems = getRepositoryItems().get("inventory");
        for (String inventoryItemId : inventoryItems.keySet()) {
            RepositoryItem inventoryItem = inventoryItems.get(inventoryItemId);
            if (skuId.equalsIgnoreCase((String) inventoryItem.getPropertyValue("skuId"))) {
                return inventoryItem;
            }
        }
        return null;
    }

    public List<RepositoryItem> findInventoryByStatus(String availabilityStatus) {
        Map<String, RepositoryItem> inventoryItems = getRepositoryItems().get("inventory");
        List<RepositoryItem> matchingItems = new ArrayList<>();
        for (String inventoryItemId : inventoryItems.keySet()) {
            RepositoryItem inventoryItem = inventoryItems.get(inventoryItemId);
            if (availabilityStatus.equalsIgnoreCase((String) inventoryItem.getPropertyValue("availabilityStatus"))) {
                matchingItems.add(inventoryItem);
            }
        }
        return matchingItems;
    }
}
