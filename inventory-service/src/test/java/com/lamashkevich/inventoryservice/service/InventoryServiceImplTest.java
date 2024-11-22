package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.TestcontainersInit;
import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;
import com.lamashkevich.inventoryservice.entity.Inventory;
import com.lamashkevich.inventoryservice.exception.InventoryNotFoundException;
import com.lamashkevich.inventoryservice.exception.StorageNotFoundException;
import com.lamashkevich.inventoryservice.repository.InventoryRepository;
import com.lamashkevich.inventoryservice.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql({"/db/init.sql"})
public class InventoryServiceImplTest extends TestcontainersInit {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
    }

    @Test
    public void getInventoriesByCodeAndBrand_ShouldReturnCorrectInventories() {
        Inventory inventory1 = saveInventory("CODE1", "Brand1", BigDecimal.valueOf(1.11), 1, 1L);
        saveInventory("CODE2", "Brand2", BigDecimal.valueOf(2.22), 2, 2L);

        List<InventoryDto> inventories = inventoryService.getInventoriesByCodeAndBrand("CODE1", "Brand1");

        assertEquals(1, inventories.size());
        assertEquals(inventory1.getStorage().getId(), inventories.getFirst().storage().id());
        assertEquals(inventory1.getPrice(), inventories.getFirst().price());
        assertEquals(inventory1.getQuantity(), inventories.getFirst().quantity());
    }

    @Test
    public void create_ShouldReturnCreatedInventory() {
        var dto = new CreateInventoryDto("CODE123", "Brand", BigDecimal.valueOf(1.11), 1, 1L);

        InventoryDto createdInventory = inventoryService.create(dto);

        assertNotNull(createdInventory);
        assertNotNull(createdInventory.id());
        assertEquals(dto.price(), createdInventory.price());
        assertEquals(dto.quantity(), createdInventory.quantity());
        assertEquals(dto.storageId(), createdInventory.storage().id());
    }

    @Test
    public void createMany_ShouldReturnCreatedInventories() {
        var dto1 = new CreateInventoryDto("CODE123", "Brand1", BigDecimal.valueOf(1.11), 1, 1L);
        var dto2 = new CreateInventoryDto("CODE456", "Brand2", BigDecimal.valueOf(2.22), 2, 2L);

        List<InventoryDto> createdInventories = inventoryService.createMany(List.of(dto1, dto2));

        assertEquals(2, createdInventories.size());
        assertNotNull(createdInventories.get(0).id());
        assertNotNull(createdInventories.get(1).id());
    }

    @Test
    public void deleteById_ShouldDeleteInventory() {
        Inventory inventory = saveInventory("CODE123", "Brand", BigDecimal.valueOf(1.11), 1, 1L);

        inventoryService.deleteById(inventory.getId());

        assertFalse(inventoryRepository.findById(inventory.getId()).isPresent());
    }

    @Test
    public void deleteById_ShouldThrowInventoryNotFoundException() {
        assertThrows(InventoryNotFoundException.class, () -> inventoryService.deleteById(Long.MAX_VALUE));
    }

    private Inventory saveInventory(String code, String brand, BigDecimal price, Integer quantity, Long sorageId) {
        var inventory = Inventory.builder()
                .code(code)
                .brand(brand)
                .price(price)
                .quantity(quantity)
                .storage(storageRepository.findById(sorageId).orElseThrow(StorageNotFoundException::new))
                .build();
        return inventoryRepository.save(inventory);
    }
}
