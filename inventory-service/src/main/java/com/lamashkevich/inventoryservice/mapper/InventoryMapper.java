package com.lamashkevich.inventoryservice.mapper;

import com.lamashkevich.inventoryservice.dto.CreateInventoryDto;
import com.lamashkevich.inventoryservice.dto.InventoryDto;
import com.lamashkevich.inventoryservice.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MapperHelper.class})
public interface InventoryMapper {
    List<InventoryDto> toListInventoryDto(List<Inventory> inventories);

    List<Inventory> toListInventory(List<CreateInventoryDto> inventoriesDto);

    @Mapping(target = "storage", source = "storageId")
    Inventory toInventory(CreateInventoryDto inventoryDto);

    InventoryDto toInventoryDto(Inventory inventory);

}
