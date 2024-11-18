package com.lamashkevich.orderservice.service;

import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.entity.Order;
import com.lamashkevich.orderservice.entity.OrderItemStatus;
import com.lamashkevich.orderservice.entity.OrderStatus;
import com.lamashkevich.orderservice.mapper.OrderMapper;
import com.lamashkevich.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        log.info("get Orders by userId: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto, String userId) {
        log.info("Create order");

        Order newOrder = orderMapper.placeDtoToOrder(placeOrderDto);
        newOrder.setUserId(userId);
        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.getItems().forEach(item -> {
            item.setOrder(newOrder);
            item.setStatus(OrderItemStatus.CREATED);
        });
        orderRepository.save(newOrder);

        return orderMapper.toOrderDto(newOrder);
    }
}
