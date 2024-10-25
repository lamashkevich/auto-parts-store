package com.lamashkevich.aggregationservice.client.inventory.payload;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocalInventory {

    private Long id;

    private BigDecimal price;

    private Integer quantity;

    private LocalStorage storage;

}
