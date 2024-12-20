package com.lamashkevich.orderservice.dto;

import com.lamashkevich.orderservice.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequest(
        @NotNull Long orderId,
        @NotNull OrderStatus status) {
}
