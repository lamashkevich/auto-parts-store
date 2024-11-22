package com.lamashkevich.inventoryservice.repository;

import com.lamashkevich.inventoryservice.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}
