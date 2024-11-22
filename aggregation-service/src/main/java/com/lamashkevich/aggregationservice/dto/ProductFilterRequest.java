package com.lamashkevich.aggregationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {

    @NotNull(message = "Code cannot be null")
    private String code;

    @NotNull(message = "Brand cannot be null")
    private String brand;

    private boolean includeExternal = false;

    private boolean includeAnalogues = true;
}
