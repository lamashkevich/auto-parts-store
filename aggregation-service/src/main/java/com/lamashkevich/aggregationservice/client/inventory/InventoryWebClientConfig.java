package com.lamashkevich.aggregationservice.client.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class InventoryWebClientConfig {

    @Value("${client.inventory.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    @Bean
    public InventoryWebClient inventoryWebClient() {
        return new InventoryWebClient(
                webClientBuilder
                        .baseUrl(baseUrl)
                        .build()
        );
    }

}
