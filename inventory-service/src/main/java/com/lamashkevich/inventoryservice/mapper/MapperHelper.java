package com.lamashkevich.inventoryservice.mapper;

import com.lamashkevich.inventoryservice.entity.Storage;
import com.lamashkevich.inventoryservice.exception.StorageNotFoundException;
import com.lamashkevich.inventoryservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperHelper {
    private final StorageRepository storageRepository;

    public Storage mapStorageIdToStorage(Long storageId) {
        return storageRepository.findById(storageId).orElseThrow(StorageNotFoundException::new);
    }
}
