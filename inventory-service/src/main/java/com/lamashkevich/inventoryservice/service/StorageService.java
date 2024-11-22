package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;

import java.util.List;

public interface StorageService {
    StorageDto getById(Long id);

    List<StorageDto> getAll();

    StorageDto create(CreateStorageDto storageDto);

    StorageDto update(Long id, CreateStorageDto storage);
}
