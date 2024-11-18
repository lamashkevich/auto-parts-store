package com.lamashkevich.orderservice.controller;

import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.service.OrderService;
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
    public OrderDto placeOrder(@RequestBody PlaceOrderDto placeOrderDto, @AuthenticationPrincipal Jwt jwt) {
        return orderService.placeOrder(placeOrderDto, getUserIdFromJwt(jwt));
    }

    @GetMapping
    public List<OrderDto> getOrdersForUser(@AuthenticationPrincipal Jwt jwt) {
        return orderService.getOrdersByUserId(getUserIdFromJwt(jwt));
    }

    private String getUserIdFromJwt(Jwt jwt) {
        return jwt.getSubject();
    }
}
