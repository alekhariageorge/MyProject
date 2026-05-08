package com.pdp.client;

import com.pdp.model.SkuPriceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class PriceClient {

    private final RestClient restClient;

    public PriceClient(RestClient.Builder restClientBuilder,
                       @Value("${evaria.services.price-url}") String priceServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(priceServiceUrl).build();
    }

    public SkuPriceDTO getPrice(String skuId) {
        try {
            return restClient.get()
                    .uri("/pdp/sku/{skuId}", skuId)
                    .retrieve()
                    .body(SkuPriceDTO.class);
        } catch (RestClientException exception) {
            return null;
        }
    }
}
