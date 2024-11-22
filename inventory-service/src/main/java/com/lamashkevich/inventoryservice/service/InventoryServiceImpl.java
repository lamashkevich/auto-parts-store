package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;
import com.lamashkevich.inventoryservice.entity.Inventory;
import com.lamashkevich.inventoryservice.exception.InventoryNotFoundException;
import com.lamashkevich.inventoryservice.mapper.InventoryMapper;
import com.lamashkevich.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getInventoriesByCodeAndBrand(String code, String brand) {
        log.info("Get inventories for code: {} and brand: {}", code, brand);
        List<Inventory> inventories = inventoryRepository.findAllByCodeIgnoreCaseAndBrandIgnoreCase(code, brand);
        return inventoryMapper.toListInventoryDto(inventories);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting inventory with id {}", id);
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(InventoryNotFoundException::new);
        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDto create(CreateInventoryDto inventoryDto) {
        log.info("Creating inventory: {}", inventoryDto);
        Inventory newInventory = inventoryMapper.toInventory(inventoryDto);
        Inventory inventory = inventoryRepository.save(newInventory);
        return inventoryMapper.toInventoryDto(inventory);
    }

    @Override
    public List<InventoryDto> createMany(List<CreateInventoryDto> inventoriesDto) {
        log.info("Creating {} inventories", inventoriesDto.size());
        List<Inventory> newInventories = inventoryMapper.toListInventory(inventoriesDto);
        List<Inventory> inventories = inventoryRepository.saveAll(newInventories);
        return inventoryMapper.toListInventoryDto(inventories);
    }
}
