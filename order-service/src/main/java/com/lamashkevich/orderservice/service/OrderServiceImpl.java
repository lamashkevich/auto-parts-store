package com.lamashkevich.orderservice.service;

import com.lamashkevich.orderservice.dto.ChangeOrderItemStatusRequest;
import com.lamashkevich.orderservice.dto.ChangeOrderStatusRequest;
import com.lamashkevich.orderservice.dto.OrderDto;
import com.lamashkevich.orderservice.dto.PlaceOrderDto;
import com.lamashkevich.orderservice.entity.Order;
import com.lamashkevich.orderservice.entity.OrderItem;
import com.lamashkevich.orderservice.entity.OrderItemStatus;
import com.lamashkevich.orderservice.entity.OrderStatus;
import com.lamashkevich.orderservice.event.OrderPlacedEvent;
import com.lamashkevich.orderservice.exception.OrderItemNotFoundException;
import com.lamashkevich.orderservice.exception.OrderNotFoundException;
import com.lamashkevich.orderservice.exception.OrderStatusChangeException;
import com.lamashkevich.orderservice.mapper.OrderMapper;
import com.lamashkevich.orderservice.repository.OrderItemRepository;
import com.lamashkevich.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        log.info("Get orders by userId: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto, String userId) {
        log.info("Place order");

        Order newOrder = orderMapper.placeDtoToOrder(placeOrderDto);
        newOrder.setUserId(userId);
        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.getItems().forEach(item -> {
            item.setOrder(newOrder);
            item.setStatus(OrderItemStatus.CREATED);
        });
        orderRepository.save(newOrder);

        var orderPlacedEvent = OrderPlacedEvent.builder()
                .orderId(newOrder.getId())
                .email(placeOrderDto.email())
                .name(placeOrderDto.name())
                .build();
        kafkaTemplate.send("order-placed", orderPlacedEvent);

        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    @Transactional
    public OrderStatus changeOrderStatus(ChangeOrderStatusRequest request, String userId) {
        log.info("Change order status: {}", request.status());

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(userId)) {
            throw new OrderStatusChangeException("Order does not belong to the authorized user");
        }

        order.setStatus(request.status());
        orderRepository.save(order);

        return order.getStatus();
    }

    @Override
    public OrderItemStatus changeOrderItemStatus(ChangeOrderItemStatusRequest request, String userId) {
        log.info("Change order item status: {}", request.status());

        OrderItem orderItem = orderItemRepository.findById(request.orderItemId())
                .orElseThrow(OrderItemNotFoundException::new);

        if (!orderItem.getOrder().getUserId().equals(userId)) {
            throw new OrderStatusChangeException("Order item does not belong to the authorized user");
        }

        orderItem.setStatus(request.status());
        orderItemRepository.save(orderItem);

        return orderItem.getStatus();
    }
}
