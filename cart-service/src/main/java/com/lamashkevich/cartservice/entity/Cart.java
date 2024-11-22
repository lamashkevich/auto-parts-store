package com.lamashkevich.cartservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(value = "cart")
public class Cart {

    @Id
    private String id;
    private String userId;
    private List<CartItem> items;

}
