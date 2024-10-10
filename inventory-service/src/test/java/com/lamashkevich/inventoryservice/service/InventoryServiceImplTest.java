package com.lamashkevich.inventoryservice.service;

import com.lamashkevich.inventoryservice.TestcontainersInit;
import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;
import com.lamashkevich.inventoryservice.exception.InventoryNotFoundException;
import com.lamashkevich.inventoryservice.repository.InventoryRepository;
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
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
    }

    @Test
    public void getInventoriesByCodeAndBrand_ShouldReturnCorrectInventories() {
        var dto1 = new CreateInventoryDto("CODE123", "Brand1", BigDecimal.valueOf(1.11), 1, 1L);
        var dto2 = new CreateInventoryDto("CODE123", "Brand2", BigDecimal.valueOf(2.22), 2, 2L);
        inventoryService.create(dto1);
        inventoryService.create(dto2);

        List<InventoryDto> inventories = inventoryService.getInventoriesByCodeAndBrand("CODE123", "Brand1");

        assertEquals(1, inventories.size());
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
        var dto = new CreateInventoryDto("CODE123", "Brand", BigDecimal.valueOf(1.11), 1, 1L);
        InventoryDto createdInventory = inventoryService.create(dto);

        inventoryService.deleteById(createdInventory.id());

        assertFalse(inventoryRepository.findById(createdInventory.id()).isPresent());
    }

    @Test
    public void deleteById_ShouldThrowInventoryNotFoundException() {
        assertThrows(InventoryNotFoundException.class, () -> inventoryService.deleteById(Long.MAX_VALUE));
    }

}
