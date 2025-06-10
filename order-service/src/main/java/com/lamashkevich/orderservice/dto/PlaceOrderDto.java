package com.lamashkevich.orderservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PlaceOrderDto(
        @NotNull @Email String email,
        @NotNull String name,
        @NotNull @Size(min = 1) List<CreateOrderItemDto> items) {

    public record CreateOrderItemDto(
            @NotNull @Min(1) Integer quantity,
            @NotNull @DecimalMin("0.01") BigDecimal price,
            @NotNull String code,
            @NotNull String brand,
            @NotNull String externalId,
            @NotNull @Future LocalDateTime expectedDeliveryDate){
    }

}
