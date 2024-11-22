package com.lamashkevich.cartservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItem {
    private String id;
    private Integer quantity;
    private String code;
    private String brand;
    private String externalId;
    private BigDecimal salePrice;
}
