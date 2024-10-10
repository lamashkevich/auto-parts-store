package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.TestcontainersInit;
import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;
import com.lamashkevich.inventoryservice.entity.Storage;
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
        Storage storage = saveStorage("Storage", "Location");

        StorageDto fetchedStorage = storageService.getById(storage.getId());

        assertNotNull(fetchedStorage);
        assertEquals(storage.getId(), fetchedStorage.id());
        assertEquals(storage.getName(), fetchedStorage.name());
    }

    @Test
    public void getById_ThrowStorageNotFoundException_WhenStorageDoesNotExist() {
        assertThrows(StorageNotFoundException.class, () -> storageService.getById(Long.MAX_VALUE));
    }

    @Test
    public void getAll_ShouldReturnAllStorages() {
        Storage storage1 = saveStorage("Storage 1", "Location 1");
        Storage storage2 = saveStorage("Storage 2", "Location 2");

        List<StorageDto> storages = storageService.getAll();

        assertEquals(2, storages.size());
        assertEquals(storage1.getName(), storages.get(0).name());
        assertEquals(storage1.getLocation(), storages.get(0).location());
        assertEquals(storage2.getName(), storages.get(1).name());
        assertEquals(storage2.getLocation(), storages.get(1).location());
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
        Storage storage = saveStorage("Storage", "Location");

        CreateStorageDto updateDto = new CreateStorageDto("Storage 2", "Location 2");
        StorageDto updatedStorage = storageService.update(storage.getId(), updateDto);

        assertNotNull(updatedStorage);
        assertEquals(storage.getId(), updatedStorage.id());
        assertEquals("Storage 2", updatedStorage.name());
        assertEquals("Location 2", updatedStorage.location());
    }

    @Test
    public void update_ShouldThrowStorageNotFoundException_WhenStorageDoesNotExist() {
        var updateDto = new CreateStorageDto("Storage", "Location");
        assertThrows(StorageNotFoundException.class, () -> storageService.update(Long.MAX_VALUE, updateDto));
    }

    private Storage saveStorage(String name, String location) {
        Storage storage = Storage.builder().name(name).location(location).build();
        return storageRepository.save(storage);
    }
}