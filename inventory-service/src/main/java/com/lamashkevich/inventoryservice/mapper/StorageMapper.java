package com.lamashkevich.inventoryservice.mapper;

import com.lamashkevich.inventoryservice.dto.CreateStorageDto;
import com.lamashkevich.inventoryservice.dto.StorageDto;
import com.lamashkevich.inventoryservice.entity.Storage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    Storage toStorage(CreateStorageDto storageDto);
    StorageDto toStorageDto(Storage storage);
    List<StorageDto> toListStoragesDto(List<Storage> storages);
}
