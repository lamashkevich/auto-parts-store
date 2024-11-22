package com.lamashkevich.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PlaceOrderDto(String email,
                            String name,
                            List<CreateOrderItemDto> items) {

    public record CreateOrderItemDto(Integer quantity,
                                     BigDecimal price,
                                     String code,
                                     String brand,
                                     String externalId,
                                     LocalDateTime expectedDeliveryDate){
    }

}
