package com.lamashkevich.cartservice.repository;

import com.lamashkevich.cartservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository  extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserId(String userId);

}
