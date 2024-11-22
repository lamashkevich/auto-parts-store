package com.lamashkevich.aggregationservice.client.inventory;

import com.lamashkevich.aggregationservice.client.inventory.payload.LocalInventory;
import reactor.core.publisher.Flux;

public interface InventoryClient {

    Flux<LocalInventory> getByCodeAndBrand(String code, String brand);

}
