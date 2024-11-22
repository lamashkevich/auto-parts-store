package com.lamashkevich.inventoryservice.repository;

import com.lamashkevich.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByCodeIgnoreCaseAndBrandIgnoreCase(String code, String brand);
}
