package com.lamashkevich.cartservice.service;

import com.lamashkevich.cartservice.dto.CartDto;
import com.lamashkevich.cartservice.dto.CreateItemRequest;
import com.lamashkevich.cartservice.entity.Cart;
import com.lamashkevich.cartservice.entity.CartItem;
import com.lamashkevich.cartservice.mapper.CartMapper;
import com.lamashkevich.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDto getCartByUserId(String userId) {
        log.info("Get cart by userId: {}", userId);
        Cart cart = findCartByUserId(userId);
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public void addItemToCart(CreateItemRequest item, String userId) {
        log.info("Add item to cart by userId: {}", userId);
        var cart = findCartByUserId(userId);

        Optional<CartItem> searchedItem = cart.getItems().stream()
                .filter(i ->
                        i.getCode().equals(item.code()) && i.getBrand().equals(item.brand())
                                && i.getExternalId().equals(item.externalId()))
                .findFirst();

        if (searchedItem.isPresent()) {
            searchedItem.get().setQuantity(searchedItem.get().getQuantity() + item.quantity());
        } else {
            var cartItem = cartMapper.toCartItem(item);
            cartItem.setId(UUID.randomUUID().toString());
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
    }

    private Cart findCartByUserId(String userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .items(new ArrayList<>())
                    .build();
            return cartRepository.save(newCart);
        }
        return cart.orElseThrow(() -> new RuntimeException("Can't find cart by userId: " + userId));
    }

}
