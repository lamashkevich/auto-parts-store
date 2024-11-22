package com.lamashkevich.aggregationservice.client.product;

import com.lamashkevich.aggregationservice.client.product.payload.LocalProduct;
import com.lamashkevich.aggregationservice.client.product.payload.SearchFilter;
import reactor.core.publisher.Flux;

public interface ProductClient {

    Flux<LocalProduct> findProducts(String query);

    Flux<LocalProduct> findProductsByFilter(SearchFilter filter);

}
