package com.lamashkevich.productservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateProductDto(
        @NotNull(message = "Code cannot be null")
        String code,

        @NotNull(message = "Brand cannot be null")
        String brand,

        @NotNull(message = "Brand cannot be null")
        String name,

        @NotNull(message = "Brand cannot be null")
        String description
){
}
