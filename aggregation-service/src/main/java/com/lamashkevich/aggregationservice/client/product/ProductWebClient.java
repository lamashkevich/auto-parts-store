package com.lamashkevich.aggregationservice.client.product;

import com.lamashkevich.aggregationservice.client.product.payload.LocalProduct;
import com.lamashkevich.aggregationservice.client.product.payload.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ProductWebClient implements ProductClient {

    private final WebClient webClient;

    @Override
    public Flux<LocalProduct> findProducts(String query) {
        return webClient
                .get()
                .uri("/api/v1/products/search?query=%s".formatted(query))
                .retrieve()
                .bodyToFlux(LocalProduct.class);
    }

    @Override
    public Flux<LocalProduct> findProductsByFilter(SearchFilter filter) {
        return webClient
                .post()
                .uri("/api/v1/products/search")
                .bodyValue(filter)
                .retrieve()
                .bodyToFlux(LocalProduct.class);
    }
}
