package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductAggregationServiceTest {

    @Mock
    private SupplierService supplierService1;

    @Mock
    private SupplierService supplierService2;

    @Mock
    private ProductInfoService productInfoService1;

    @Mock
    private ProductInfoService productInfoService2;

    @Mock
    private LocalSupplierService localService;

    @Mock
    private MarginService marginService;

    private ProductAggregationService productAggregationService;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = createProduct("code", "brand", true, false);
        product2 = createProduct("code", "brand", false, false);
        product3 = createProduct("code1", "brand1", false, true);

        List<SupplierService> suppliers = List.of(supplierService1, supplierService2);
        List<ProductInfoService> services = List.of(productInfoService1, productInfoService2);

        productAggregationService = new ProductAggregationService(suppliers, services, localService, marginService);
    }

    @Test
    void findByCodeAndBrand_WhenIncludeExternalIsFalse() {
        when(supplierService1.findByCodeAndBrand("code", "brand", true))
                .thenReturn(Flux.just(product2));

        when(supplierService2.findByCodeAndBrand("code", "brand", true))
                .thenReturn(Flux.just(product1, product3));

        when(localService.fetchInventories(anyString(), anyString()))
                .thenReturn(Flux.empty());

        ProductFilterRequest request = new ProductFilterRequest("code", "brand", false, true);

        Flux<Product> result = productAggregationService.findByCodeAndBrand(request);

        StepVerifier.create(result)
                .expectNextMatches(product ->
                        product.getCode().equals("code") &&
                                product.getBrand().equals("brand") &&
                                product.getInventories().size() == 1)
                .expectNextMatches(product ->
                        product.getCode().equals("code1") &&
                                product.getBrand().equals("brand1") &&
                                product.getInventories().size() == 1)
                .expectComplete()
                .verify();
    }

    @Test
    void findByCodeAndBrand_WhenIncludeExternalIsTrue() {
        when(supplierService1.findByCodeAndBrand("code", "brand", true))
                .thenReturn(Flux.just(product2));

        when(supplierService2.findByCodeAndBrand("code", "brand", true))
                .thenReturn(Flux.just(product1, product3));

        when(localService.fetchInventories(anyString(), anyString()))
                .thenReturn(Flux.empty());

        ProductFilterRequest request = new ProductFilterRequest("code", "brand", true, true);

        Flux<Product> result = productAggregationService.findByCodeAndBrand(request);

        StepVerifier.create(result)
                .expectNextMatches(product ->
                        product.getCode().equals("code") &&
                                product.getBrand().equals("brand") &&
                                product.getInventories().size() == 2)
                .expectNextMatches(product ->
                        product.getCode().equals("code1") &&
                                product.getBrand().equals("brand1") &&
                                product.getInventories().size() == 1)
                .expectComplete()
                .verify();
    }


    @Test
    void findByQuery() {
        var productInfo1 = ProductInfo.builder().code("code1").brand("brand").build();
        var productInfo2 = ProductInfo.builder().code("code1").brand("brand").build();
        var productInfo3 = ProductInfo.builder().code("code2").brand("brand").build();

        when(productInfoService1.findByQuery(anyString())).thenReturn(Flux.just(productInfo1));
        when(productInfoService2.findByQuery(anyString())).thenReturn(Flux.just(productInfo2, productInfo3));

        Flux<ProductInfo> result = productAggregationService.findByQuery(anyString());

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    private Product createProduct(String code, String brand, boolean isExternal, boolean isAnalog) {
        Inventory inventory = Inventory.builder()
                .supplier(Supplier.SHATE_M)
                .quantity(1)
                .price(BigDecimal.valueOf(Math.random()))
                .isExternalStorage(isExternal)
                .deliveryDate(LocalDateTime.now())
                .storageName("Storage")
                .multiplicity(1)
                .externalId("id")
                .storageCode(UUID.randomUUID().toString())
                .build();

        return Product.builder()
                .code(code)
                .brand(brand)
                .description("Description")
                .isAnalog(isAnalog)
                .inventories(Collections.singletonList(inventory))
                .build();
    }
}