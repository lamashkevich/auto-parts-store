package com.lamashkevich.orderservice.dto;

import com.lamashkevich.orderservice.entity.OrderItemStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderItemStatusRequest(
        @NotNull Long orderItemId,
        @NotNull OrderItemStatus status) {
}
