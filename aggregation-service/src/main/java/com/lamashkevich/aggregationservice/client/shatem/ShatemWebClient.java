package com.lamashkevich.aggregationservice.client.shatem;

import com.lamashkevich.aggregationservice.client.shatem.payload.ArticleCard;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticleFilter;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticlePriceCard;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticlePriceFilterKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ShatemWebClient implements ShatemClient {

    private final WebClient webClient;

    @Override
    public Flux<ArticleCard> searchByQuery(String query) {
        return this.webClient
                .get()
                .uri("articles/search?searchString=%s".formatted(query))
                .retrieve()
                .bodyToFlux(ArticleCard.class);
    }

    @Override
    public Flux<ArticleCard> searchByFilter(ArticleFilter request) {
        return this.webClient
                .post()
                .uri("articles/search")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(ArticleCard.class);
    }

    @Override
    public Flux<ArticlePriceCard> searchPricesWithArticle(List<ArticlePriceFilterKey> request) {
        return this.webClient
                .post()
                .uri("prices/search/with_article_info")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(ArticlePriceCard.class);
    }
}
