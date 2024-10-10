package com.lamashkevich.inventoryservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateInventoryDto(
        @NotNull(message = "Code cannot be null")
        String code,

        @NotNull(message = "Brand cannot be null")
        String brand,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0.00")
        @Digits(integer = 19, fraction = 2)
        BigDecimal price,

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Storage Id cannot be null")
        Long storageId) {
}
