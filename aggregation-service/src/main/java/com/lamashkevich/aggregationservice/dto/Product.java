package com.lamashkevich.aggregationservice.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String code;

    private String brand;

    private String description;

    private Boolean isAnalog;

    private List<Inventory> inventories;

}
