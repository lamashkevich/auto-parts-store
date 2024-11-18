package com.lamashkevich.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String brand;

    private String externalId;

    private LocalDateTime expectedDeliveryDate;

    private BigDecimal price;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
