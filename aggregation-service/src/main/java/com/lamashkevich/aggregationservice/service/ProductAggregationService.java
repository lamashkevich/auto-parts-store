package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.dto.Inventory;
import com.lamashkevich.aggregationservice.dto.Product;
import com.lamashkevich.aggregationservice.dto.ProductFilterRequest;
import com.lamashkevich.aggregationservice.dto.ProductInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAggregationService {

    private final List<SupplierService> suppliers;
    private final List<ProductInfoService> productInfoServices;
    private final LocalSupplierService localService;

    public Flux<Product> findByCodeAndBrand(ProductFilterRequest request) {
        String code = request.getCode();
        String brand = request.getBrand();
        boolean includeExternal = request.isIncludeExternal();
        boolean includeAnalogues = request.isIncludeAnalogues();

        log.info("Find {} from {} suppliers", request, suppliers.size());
        return fetchProducts(code, brand, includeAnalogues)
                .transform(this::groupByCodeAndBrand)
                .flatMap(this::addLocalInventories)
                .transform(flux -> includeExternal ? flux : flux.flatMap(this::filterExternalInventories))
                .map(product -> markAnalog(product, code, brand))
                .filter(product -> !product.getIsAnalog() || !product.getInventories().isEmpty())
                .sort(sortProducts())
                .map(this::sortInventories)
                .onErrorResume((error) -> {
                    log.error(error.getMessage());
                    return Flux.empty();
                });
    }

    public Flux<ProductInfo> findByQuery(String query) {
        log.info("Find by {} from {} services", query, productInfoServices.size());
        return Flux.fromIterable(productInfoServices)
                .flatMap(service -> service.findByQuery(query)
                        .timeout(java.time.Duration.ofSeconds(5))
                        .onErrorResume(error -> {
                            log.error("{}: {}", service.getClass().getSimpleName(), error.getMessage());
                            return Flux.empty();
                        }))
                .distinct(product -> normalize(product.getCode()) + "&" + normalize(product.getBrand()));
    }

    private Flux<Product> fetchProducts(String code, String brand, boolean includeAnalogues) {
        return Flux.fromIterable(suppliers)
                .flatMap(supplier -> supplier.findByCodeAndBrand(code, brand, includeAnalogues)
                        .timeout(Duration.ofSeconds(10), Flux.empty())
                        .onErrorResume(error -> {
                            log.warn("{}: {}", supplier.getClass().getSimpleName(), error.getMessage());
                            return Flux.empty();
                        }))
                .timeout(java.time.Duration.ofSeconds(15))
                .onErrorResume(error -> {
                    log.error("{}", error.getMessage());
                    return Flux.empty();
                });
    }

    private Flux<Product> groupByCodeAndBrand(Flux<Product> productFlux) {
        return productFlux
                .groupBy(product -> new ProductKey(normalize(product.getCode()), normalize(product.getBrand())))
                .flatMap(grouped -> grouped.reduce(this::mergeProducts));
    }

    private Product mergeProducts(Product product1, Product product2) {
        List<Inventory> mergedInventories = Stream
                .concat(product1.getInventories().stream(), product2.getInventories().stream())
                .toList();

        product1.setInventories(mergedInventories);

        return product1;
    }

    private Mono<Product> addLocalInventories(Product product) {
        return localService.fetchInventories(product.getCode(), product.getBrand())
                .collectList()
                .map(inventories -> {
                    List<Inventory> updatedInventories = new ArrayList<>(product.getInventories());
                    updatedInventories.addAll(inventories);
                    product.setInventories(updatedInventories);
                    return product;
                });
    }

    private Flux<Product> filterExternalInventories(Product product) {
        List<Inventory> filteredInventories = product.getInventories().stream()
                .filter(inventory -> !inventory.isExternalStorage())
                .toList();

        product.setInventories(filteredInventories);
        return  Flux.just(product);
    }

    private Product markAnalog(Product product, String code, String brand) {
        product.setIsAnalog(!normalize(product.getBrand()).equalsIgnoreCase(normalize(brand)) ||
                !normalize(product.getCode()).equalsIgnoreCase(normalize(code)));
        return product;
    }

    private Comparator<Product> sortProducts() {
        return Comparator.comparing(Product::getIsAnalog)
                .thenComparing(product -> product.getInventories().stream()
                        .map(inventory -> inventory.getDeliveryDate().toLocalDate())
                        .min(LocalDate::compareTo)
                        .orElse(LocalDate.MAX))
                .thenComparing(product -> product.getInventories().stream()
                        .map(Inventory::getPrice)
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO));
    }

    private Product sortInventories(Product product) {
        List<Inventory> sortedInventories = product.getInventories().stream()
                .sorted(Comparator.comparing(Inventory::getPrice)
                        .thenComparing(Inventory::getDeliveryDate))
                .limit(10)
                .toList();

        product.setInventories(sortedInventories);
        return product;
    }

    private String normalize(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    private record ProductKey(String code, String brand) {}

}
