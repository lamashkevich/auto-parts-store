package com.lamashkevich.cartservice.dto;

import java.math.BigDecimal;

public record CreateItemRequest(Integer quantity,
                                String code,
                                String brand,
                                String externalId,
                                BigDecimal salePrice) {
}
