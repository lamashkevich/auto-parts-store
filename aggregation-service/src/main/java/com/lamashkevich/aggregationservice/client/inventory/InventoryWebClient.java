package com.lamashkevich.aggregationservice.client.inventory;

import com.lamashkevich.aggregationservice.client.inventory.payload.LocalInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class InventoryWebClient implements InventoryClient {

    private final WebClient webClient;

    @Override
    public Flux<LocalInventory> getByCodeAndBrand(String code, String brand) {
        return webClient
                .get()
                .uri("/api/v1/inventories/by-code-and-brand?code=%s&brand=%s".formatted(code, brand))
                .retrieve()
                .bodyToFlux(LocalInventory.class);
    }

}
