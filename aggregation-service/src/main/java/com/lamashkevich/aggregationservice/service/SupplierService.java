package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.dto.Product;
import reactor.core.publisher.Flux;

public interface SupplierService {

    Flux<Product> findByCodeAndBrand(String code, String brand, boolean includeAnalogues);

}
