package com.lamashkevich.aggregationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Inventory {

    private Supplier supplier;

    private BigDecimal price;

    private Integer quantity;

    private LocalDateTime deliveryDate;

    private String storageName;

    private String storageCode;

    private boolean isExternalStorage;

    private Integer multiplicity;

    private String externalId;

}
