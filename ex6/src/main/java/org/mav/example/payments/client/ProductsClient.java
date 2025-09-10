package org.mav.example.payments.client;

import org.mav.example.products.api.dto.ProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class ProductsClient {

    private final RestClient rc;

    public ProductsClient(RestClient productsRestClient) {
        this.rc = productsRestClient;
    }

    public List<ProductDto> findByUser(Long userId) {
        try {
            return rc.get()
                    .uri(uri -> uri.queryParam("userId", userId).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProductDto>>() {
                    });
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (RestClientException e) {
            // таймауты/сетевые и пр. - будут ретраиться
            throw e;
        }
    }

    public ProductDto findById(Long id) {
        try {
            return rc.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (RestClientException e) {
            // таймауты/сетевые и пр. - будут ретраиться
            throw e;
        }
    }
}