package com.lamashkevich.inventoryservice.exception;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException() {
        super("Storage Not Found");
    }
}