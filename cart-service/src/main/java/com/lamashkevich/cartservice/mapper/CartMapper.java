package com.lamashkevich.cartservice.mapper;

import com.lamashkevich.cartservice.dto.CartDto;
import com.lamashkevich.cartservice.dto.CreateItemRequest;
import com.lamashkevich.cartservice.entity.Cart;
import com.lamashkevich.cartservice.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toCartDto(Cart cart);

    CartItem toCartItem(CreateItemRequest item);
}
