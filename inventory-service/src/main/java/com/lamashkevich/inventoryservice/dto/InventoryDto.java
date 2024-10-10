package com.lamashkevich.inventoryservice.dto;

import java.math.BigDecimal;

public record InventoryDto(Long id, BigDecimal price, Integer quantity, StorageDto storage) {
}
