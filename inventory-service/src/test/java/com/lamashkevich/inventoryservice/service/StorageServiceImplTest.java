package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.TestcontainersInit;
import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;
import com.lamashkevich.inventoryservice.exception.StorageNotFoundException;
import com.lamashkevich.inventoryservice.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class StorageServiceImplTest extends TestcontainersInit {
    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageServiceImpl storageService;

    @BeforeEach
    void setUp() {
        storageRepository.deleteAll();
    }

    @Test
    public void getById_ShouldReturnStorage() {
        var createStorageDto = new CreateStorageDto("Storage 1", "Location 1");
        StorageDto createdStorage = storageService.create(createStorageDto);

        StorageDto fetchedStorage = storageService.getById(createdStorage.id());

        assertNotNull(fetchedStorage);
        assertEquals(createdStorage.id(), fetchedStorage.id());
        assertEquals(createStorageDto.name(), fetchedStorage.name());
    }

    @Test
    public void getAll_ShouldReturnAllStorages() {
        var dto1 = new CreateStorageDto("Storage 1", "Location 1");
        var dto2 = new CreateStorageDto("Storage 2", "Location 2");
        storageService.create(dto1);
        storageService.create(dto2);

        List<StorageDto> storages = storageService.getAll();

        assertEquals(2, storages.size());
    }

    @Test
    public void create_ShouldReturnCreatedStorage() {
        var dto = new CreateStorageDto("Storage 1", "Location 1");

        StorageDto createdStorage = storageService.create(dto);

        assertNotNull(createdStorage);
        assertNotNull(createdStorage.id());
        assertEquals(dto.name(), createdStorage.name());
        assertEquals(dto.location(), createdStorage.location());
    }

    @Test
    public void update_ShouldUpdateAndReturnUpdatedStorage() {
        CreateStorageDto createStorageDto = new CreateStorageDto("Storage 1", "Location");
        StorageDto createdStorage = storageService.create(createStorageDto);

        CreateStorageDto updateDto = new CreateStorageDto("Storage 2", "Location 2");
        StorageDto updatedStorage = storageService.update(createdStorage.id(), updateDto);

        assertNotNull(updatedStorage);
        assertEquals(createdStorage.id(), updatedStorage.id());
        assertEquals("Storage 2", updatedStorage.name());
        assertEquals("Location 2", updatedStorage.location());
    }

    @Test
    public void update_ShouldThrowStorageNotFoundException_WhenStorageDoesNotExist() {
        var updateDto = new CreateStorageDto("Storage", "Location");

        assertThrows(StorageNotFoundException.class, () -> {
            storageService.update(Long.MAX_VALUE, updateDto);
        });
    }
}