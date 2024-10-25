package com.lamashkevich.aggregationservice.service;

import com.lamashkevich.aggregationservice.client.inventory.InventoryClient;
import com.lamashkevich.aggregationservice.client.inventory.payload.LocalInventory;
import com.lamashkevich.aggregationservice.client.product.ProductClient;
import com.lamashkevich.aggregationservice.client.product.payload.LocalProduct;
import com.lamashkevich.aggregationservice.client.product.payload.SearchFilter;
import com.lamashkevich.aggregationservice.dto.Inventory;
import com.lamashkevich.aggregationservice.dto.Product;
import com.lamashkevich.aggregationservice.dto.ProductInfo;
import com.lamashkevich.aggregationservice.dto.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalSupplierService implements ProductInfoService, SupplierService {

    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    public Flux<Inventory> fetchInventories(String code, String brand) {
        return inventoryClient.getByCodeAndBrand(code, brand)
                .map(this::mapToInventory);
    }

    @Override
    public Flux<ProductInfo> findByQuery(String query) {
        return productClient.findProducts(query)
                .map(this::mapToProduct);
    }

    @Override
    public Flux<Product> findByCodeAndBrand(String code, String brand, boolean includeAnalogues) {
        Flux<LocalProduct> products = productClient.findProductsByFilter(new SearchFilter("", code, brand));
        return products.map(this::mapToProduct);
    }

    private Product mapToProduct(LocalProduct localProduct) {
        return Product.builder()
                .code(localProduct.getCode())
                .brand(localProduct.getBrand())
                .description(String.format("%s %s", localProduct.getName(), localProduct.getDescription()))
                .inventories(List.of())
                .build();
    }

    private Inventory mapToInventory(LocalInventory localInventory) {
        return Inventory.builder()
                .supplier(Supplier.LOCAL)
                .price(localInventory.getPrice())
                .quantity(localInventory.getQuantity())
                .isExternalStorage(false)
                .storageCode(localInventory.getStorage().getId().toString())
                .storageName(localInventory.getStorage().getName())
                .deliveryDate(LocalDateTime.now().plusHours(1))
                .externalId(localInventory.getId().toString())
                .multiplicity(1)
                .build();
    }
}
