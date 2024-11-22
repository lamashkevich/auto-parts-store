package com.lamashkevich.aggregationservice.client.shatem;

import com.lamashkevich.aggregationservice.client.shatem.payload.TokenResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Configuration
public class ShatemWebClientConfig {

    @Value("${client.shate-m.auth.login}")
    private String login;

    @Value("${client.shate-m.auth.password}")
    private String password;

    @Value("${client.shate-m.base-url}")
    private String baseUrl;

    private final AtomicReference<TokenResource> token = new AtomicReference<>(null);

    @Bean
    public ShatemWebClient shatemWebClient() {
        return new ShatemWebClient(WebClient.builder()
                .baseUrl(baseUrl)
                .filter(authFilter())
                .build());
    }

    private ExchangeFilterFunction authFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest ->
                getToken()
                        .flatMap(token -> {
                            ClientRequest newRequest = ClientRequest.from(clientRequest)
                                    .header("Authorization", "Bearer " + token.getAccessToken())
                                    .build();
                            return Mono.just(newRequest);
                        })
        );
    }

    private Mono<TokenResource> fetchAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("login", login);
        body.add("password", password);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TokenResource.class)
                .doOnError(e -> log.error("Failed to fetch token", e))
                .doOnNext(token::set);
    }

    private Mono<TokenResource> getToken() {
        TokenResource currentToken = token.get();
        if (tokenIsValid(currentToken)) {
            return Mono.just(currentToken);
        }
        return fetchAccessToken();
    }

    private boolean tokenIsValid(TokenResource token) {
        return token != null && token.getExpiresIn().isAfter(LocalDateTime.now());
    }
}
