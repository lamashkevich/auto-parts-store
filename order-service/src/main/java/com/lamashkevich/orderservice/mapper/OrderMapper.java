package com.lamashkevich.orderservice.mapper;

import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order placeDtoToOrder(PlaceOrderDto placeOrderDto);

}
