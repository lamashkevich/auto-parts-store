package com.lamashkevich.inventoryservice.controller;

import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;
import com.lamashkevich.inventoryservice.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/storages")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public List<StorageDto> getAll() {
        return storageService.getAll();
    }

    @GetMapping("/{id}")
    public StorageDto getById(@PathVariable Long id) {
        return storageService.getById(id);
    }

    @PostMapping
    public StorageDto create(@Valid @RequestBody CreateStorageDto storageDto) {
        return storageService.create(storageDto);
    }

    @PutMapping("/{id}")
    public StorageDto update(@PathVariable Long id,@Valid @RequestBody CreateStorageDto storageDto) {
        return storageService.update(id, storageDto);
    }

}
