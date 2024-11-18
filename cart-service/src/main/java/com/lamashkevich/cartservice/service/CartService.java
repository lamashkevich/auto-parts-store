package com.lamashkevich.cartservice.service;

import com.lamashkevich.cartservice.dto.CartDto;
import com.lamashkevich.cartservice.dto.CreateItemRequest;

public interface CartService {

    CartDto getCartByUserId(String userId);

    void addItemToCart(CreateItemRequest item, String userId);

}
