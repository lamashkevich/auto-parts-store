package com.lamashkevich.orderservice.controller;

import com.lamashkevich.orderservice.dto.ChangeOrderItemStatusRequest;
import com.lamashkevich.orderservice.dto.ChangeOrderStatusRequest;
import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.entity.OrderItemStatus;
import com.lamashkevich.orderservice.entity.OrderStatus;
import com.lamashkevich.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDto placeOrder(@RequestBody @Valid PlaceOrderDto placeOrderDto, @AuthenticationPrincipal Jwt jwt) {
        return orderService.placeOrder(placeOrderDto, getUserIdFromJwt(jwt));
    }

    @GetMapping
    public List<OrderDto> getOrdersForUser(@AuthenticationPrincipal Jwt jwt) {
        return orderService.getOrdersByUserId(getUserIdFromJwt(jwt));
    }

    @PostMapping("/status")
    public OrderStatus changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest request,
                                         @AuthenticationPrincipal Jwt jwt) {
        return orderService.changeOrderStatus(request, getUserIdFromJwt(jwt));
    }

    @PostMapping("/items/status")
    public OrderItemStatus changeOrderItemStatus(@RequestBody @Valid ChangeOrderItemStatusRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserIdFromJwt(jwt);
        return orderService.changeOrderItemStatus(request, userId);
    }

    private String getUserIdFromJwt(Jwt jwt) {
        return jwt.getSubject();
    }
}
