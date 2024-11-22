package com.lamashkevich.cartservice.controller;

import com.lamashkevich.cartservice.dto.CartDto;
import com.lamashkevich.cartservice.dto.CreateItemRequest;
import com.lamashkevich.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartDto getCart(@AuthenticationPrincipal Jwt jwt) {
        return cartService.getCartByUserId(getUserIdFromJwt(jwt));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToCart(@AuthenticationPrincipal Jwt jwt,
                              @RequestBody CreateItemRequest cartItemRequest) {
        cartService.addItemToCart(cartItemRequest, getUserIdFromJwt(jwt));
    }

    private String getUserIdFromJwt(Jwt jwt) {
        return jwt.getSubject();
    }
}
