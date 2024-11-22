package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;
import com.lamashkevich.inventoryservice.entity.Storage;
import com.lamashkevich.inventoryservice.exception.StorageNotFoundException;
import com.lamashkevich.inventoryservice.mapper.StorageMapper;
import com.lamashkevich.inventoryservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;

    @Override
    public StorageDto getById(Long id) {
        log.info("Get storage with id {}", id);
        Storage storage = storageRepository.findById(id).orElseThrow(StorageNotFoundException::new);
        return storageMapper.toStorageDto(storage);
    }

    @Override
    public List<StorageDto> getAll() {
        log.info("Get all storages");
        return storageMapper.toListStoragesDto(storageRepository.findAll());
    }

    @Override
    public StorageDto create(CreateStorageDto storageDto) {
        log.info("Creating storage: {}", storageDto);
        Storage newStorage = storageMapper.toStorage(storageDto);
        Storage storage = storageRepository.save(newStorage);
        return storageMapper.toStorageDto(storage);
    }

    @Override
    public StorageDto update(Long id, CreateStorageDto storageDto) {
        log.info("Updating storage with id: {}", id);
        Storage storage = storageRepository.findById(id).orElseThrow(StorageNotFoundException::new);

        storage.setName(storageDto.name());
        storage.setLocation(storageDto.location());

        return storageMapper.toStorageDto(storageRepository.save(storage));
    }
}
