package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.dto.ProductInfo;
import reactor.core.publisher.Flux;

public interface ProductInfoService {

    Flux<ProductInfo> findByQuery(String query);

}
