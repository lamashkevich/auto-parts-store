package com.lamashkevich.orderservice.service;

import com.lamashkevich.orderservice.dto.ChangeOrderItemStatusRequest;
import com.lamashkevich.orderservice.dto.ChangeOrderStatusRequest;
import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.entity.OrderItemStatus;
import com.lamashkevich.orderservice.entity.OrderStatus;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(String user);

    OrderDto placeOrder(@Valid PlaceOrderDto placeOrderDto, String userId);

    OrderStatus changeOrderStatus(@Valid ChangeOrderStatusRequest request, String userId);

    OrderItemStatus changeOrderItemStatus(@Valid ChangeOrderItemStatusRequest request, String userId);
}
