package com.lamashkevich.orderservice.exception;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException() {
        super("Order item not found");
    }
}
