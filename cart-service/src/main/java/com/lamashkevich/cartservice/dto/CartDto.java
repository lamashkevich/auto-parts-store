package com.lamashkevich.cartservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(String id, List<CartItemDto> items) {

    public record CartItemDto(String id,
                              Integer quantity,
                              String code,
                              String brand,
                              BigDecimal salePrice,
                              String externalId) {
    }

}
