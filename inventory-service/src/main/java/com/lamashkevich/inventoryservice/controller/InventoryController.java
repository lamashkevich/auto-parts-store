package com.lamashkevich.inventoryservice.controller;

import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;
import com.lamashkevich.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/by-code-and-brand")
    public List<InventoryDto> getByCodeAndBrand(@RequestParam String code, @RequestParam String brand) {
        return inventoryService.getInventoriesByCodeAndBrand(code, brand);
    }

    @PostMapping
    public InventoryDto create(@Valid @RequestBody CreateInventoryDto inventoryDto) {
        return inventoryService.create(inventoryDto);
    }

    @PostMapping("/many")
    public List<InventoryDto> createMany(@Valid @RequestBody List<CreateInventoryDto> inventoriesDto) {
        return inventoryService.createMany(inventoriesDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        inventoryService.deleteById(id);
    }

}
