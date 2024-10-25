package com.lamashkevich.aggregationservice.controller;

import com.lamashkevich.aggregationservice.dto.Product;
import com.lamashkevich.aggregationservice.dto.ProductFilterRequest;
import com.lamashkevich.aggregationservice.dto.ProductInfo;
import com.lamashkevich.aggregationservice.service.ProductAggregationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/aggregation/products")
public class ProductAggregationController {

    private final ProductAggregationService aggregationService;

    @GetMapping
    public Flux<ProductInfo> findByQuery(@RequestParam String query) {
        return aggregationService.findByQuery(query);
    }

    @PostMapping
    public Flux<Product> getByCodeAndBrand(@Valid @RequestBody ProductFilterRequest request) {
        return aggregationService.findByCodeAndBrand(request);
    }

}
