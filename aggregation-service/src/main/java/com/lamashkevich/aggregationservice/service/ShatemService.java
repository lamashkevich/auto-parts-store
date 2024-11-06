package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.client.shatem.ShatemClient;
import com.lamashkevich.aggregationservice.client.shatem.payload.*;
import com.lamashkevich.aggregationservice.dto.Inventory;
import com.lamashkevich.aggregationservice.dto.Product;
import com.lamashkevich.aggregationservice.dto.ProductInfo;
import com.lamashkevich.aggregationservice.dto.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShatemService implements SupplierService, ProductInfoService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final String INVENTORY_URL = "https://shate-m.by/personal/search/part/";

    private final ShatemClient shatemClient;

    @Override
    public Flux<Product> findByCodeAndBrand(String code, String brand, boolean includeAnalogues) {
        return shatemClient
                .searchByFilter(createArticleFilter(code, brand))
                .flatMap(resp -> shatemClient
                        .searchPricesWithArticle(createArticlePriceFilterKey(resp.article().getId(), includeAnalogues)))
                .map(this::maptoProduct);
    }

    @Override
    public Flux<ProductInfo> findByQuery(String query) {
        return shatemClient
                .searchByQuery(query)
                .map(this::mapToProductInfo);
    }

    private ArticleFilter createArticleFilter(String code, String brand) {
        return new ArticleFilter(List.of(new ArticleFilter.Key(code, brand)));
    }

    private List<ArticlePriceFilterKey> createArticlePriceFilterKey(Long id, boolean includeAnalogues) {
        return List.of(new ArticlePriceFilterKey(id, includeAnalogues));
    }

    private Product maptoProduct(ArticlePriceCard card) {
        return Product.builder()
                .code(card.article().getCode())
                .brand(card.article().getTradeMarkName())
                .description(String.format("%s %s", card.article().getName(), card.article().getDescription()))
                .inventories(card.prices().stream().map(this::mapInventory).toList())
                .build();
    }

    private Inventory mapInventory(ArticlePrice price) {
        return Inventory.builder()
                .supplier(Supplier.SHATE_M)
                .quantity(price.getQuantity().available())
                .price(price.getPrice().value())
                .deliveryDate(LocalDateTime.parse(price.getDeliveryDateTimes().getFirst().deliveryDateTime(), FORMATTER))
                .storageCode(price.getLocationCode())
                .storageName(price.getAddInfo().city())
                .isExternalStorage(price.getType().equals(ArticlePrice.ArticlePriceType.External))
                .multiplicity(price.getQuantity().multiplicity())
                .externalId(price.getId())
                .url(INVENTORY_URL + price.getArticleId())
                .build();
    }

    private ProductInfo mapToProductInfo(ArticleCard card) {
        return ProductInfo.builder()
                .code(card.article().getCode())
                .brand(card.article().getTradeMarkName())
                .description(card.article().getDescription())
                .build();
    }

}
