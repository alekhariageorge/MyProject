package com.pdp.client;

import com.pdp.model.MediaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class MediaClient {

    private final RestClient restClient;

    public MediaClient(RestClient.Builder restClientBuilder,
                       @Value("${evaria.services.media-url}") String mediaServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(mediaServiceUrl).build();
    }

    public MediaDTO getSkuMainImage(String skuId) {
        return getMedia(skuId + "-large");
    }

    public MediaDTO getProductThumbnail(String productId) {
        return getMedia(productId + "-thumbnail");
    }

    private MediaDTO getMedia(String mediaId) {
        try {
            return restClient.get()
                    .uri("/media/{mediaId}", mediaId)
                    .retrieve()
                    .body(MediaDTO.class);
        } catch (RestClientException exception) {
            return null;
        }
    }
}
