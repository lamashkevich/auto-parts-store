package com.lamashkevich.aggregationservice.client.shatem.payload;

import java.util.List;

public record ArticlePriceCard(Article article, List<ArticlePrice> prices) {
}
