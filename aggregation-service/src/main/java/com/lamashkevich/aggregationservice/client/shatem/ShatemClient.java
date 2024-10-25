package com.lamashkevich.aggregationservice.client.shatem;

import com.lamashkevich.aggregationservice.client.shatem.payload.ArticleCard;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticleFilter;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticlePriceCard;
import com.lamashkevich.aggregationservice.client.shatem.payload.ArticlePriceFilterKey;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ShatemClient {

    Flux<ArticleCard> searchByQuery(String query);

    Flux<ArticleCard> searchByFilter(ArticleFilter request);

    Flux<ArticlePriceCard> searchPricesWithArticle(List<ArticlePriceFilterKey> request);

}
