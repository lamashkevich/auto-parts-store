package com.lamashkevich.aggregationservice.client.shatem.payload;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArticlePrice {
    private String id;
    private String articleId;
    private String locationCode;
    private String locationCodeReal;
    private ArticlePriceType type;
    private String agreementCode;
    private Boolean isRepaired;
    private Price price;
    private ArticlePriceQuantity quantity;
    private ArticlePriceSupplyProbability supplyProbability;
    private ArticlePriceAddInfo addInfo;
    private Integer priority;
    private Integer hash;
    private List<DeliveryDateInfo> deliveryDateTimes;
    private String shippingDateTime;
    private boolean isImport;
    private boolean isFree;

    public enum ArticlePriceType {
        Internal, External, Api
    }

    public record Price(BigDecimal value, BigDecimal valueWithMargin,
                        BigDecimal valueRecommended, String currencyCode) {
    }

    public record ArticlePriceQuantity(Integer available, String availableType,
                                       Integer multiplicity, Integer minimum, Integer maximum) {
    }

    public record ArticlePriceSupplyProbability(String lastUpdateDateTime, Double rating) {
    }

    public record ArticlePriceAddInfo(String city, Boolean isSale, String comment,
                                      Boolean isReturnAllowed, String warningText) {
    }

    public record DeliveryDateInfo(String deliveryAddressCode, String deliveryDateTime) {
    }
}
