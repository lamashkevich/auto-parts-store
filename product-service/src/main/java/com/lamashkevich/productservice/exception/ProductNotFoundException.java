package com.lamashkevich.productservice.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product Not Found");
    }
}
