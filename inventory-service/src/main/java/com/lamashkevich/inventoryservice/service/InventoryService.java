package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getInventoriesByCodeAndBrand(String code, String brand);

    void deleteById(Long id);

    InventoryDto create(CreateInventoryDto inventoryDto);

    List<InventoryDto> createMany(List<CreateInventoryDto> inventoriesDto);

}
