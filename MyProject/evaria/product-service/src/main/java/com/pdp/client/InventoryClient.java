package com.pdp.client;

import com.pdp.model.InventoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(RestClient.Builder restClientBuilder,
                           @Value("${evaria.services.inventory-url}") String inventoryServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(inventoryServiceUrl).build();
    }

    public InventoryDTO getInventory(String skuId) {
        try {
            return restClient.get()
                    .uri("/inventory/sku/{skuId}", skuId)
                    .retrieve()
                    .body(InventoryDTO.class);
        } catch (RestClientException exception) {
            return null;
        }
    }
}
