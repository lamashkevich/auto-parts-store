package com.lamashkevich.orderservice.service;

import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(String user);

    OrderDto placeOrder(PlaceOrderDto placeOrderDto, String userId);
}
