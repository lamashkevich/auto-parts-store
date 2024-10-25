package com.lamashkevich.aggregationservice.client.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ProductWebClientConfig {

    @Value("${client.product.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    @Bean
    public ProductWebClient productWebClient() {
        return new ProductWebClient(
                webClientBuilder
                        .baseUrl(baseUrl)
                        .build()
        );
    }

}
