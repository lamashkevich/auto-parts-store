package com.lamashkevich.orderservice.dto;

import com.lamashkevich.orderservice.entity.OrderItemStatus;
import com.lamashkevich.orderservice.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       String email,
                       String name,
                       OrderStatus status,
                       List<OrderItemDto> items) {

    public record OrderItemDto(Integer quantity,
                               BigDecimal price,
                               String code,
                               String brand,
                               String externalId,
                               OrderItemStatus status,
                               LocalDateTime expectedDeliveryDate){}
}
