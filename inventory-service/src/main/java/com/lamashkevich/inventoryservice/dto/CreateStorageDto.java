package com.lamashkevich.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateStorageDto(
        @NotNull(message = "Name cannot be null")
        String name,

        @NotNull(message = "Location cannot be null")
        String location) {
}