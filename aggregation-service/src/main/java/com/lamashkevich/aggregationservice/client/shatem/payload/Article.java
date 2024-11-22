package com.lamashkevich.aggregationservice.client.shatem.payload;

import lombok.Data;

@Data
public class Article {
    private Long id;
    private String code;
    private String tradeMarkName;
    private String name;
    private String description;
    private String unitOfMeasure;
    private boolean isRepaired;
}
