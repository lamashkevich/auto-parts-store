package com.lamashkevich.aggregationservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MarginServiceImpl implements MarginService {

    @Override
    public BigDecimal getPriceWithMargin(BigDecimal price) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(getMargin(price))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getMargin(BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(500)) > 0) {
            return BigDecimal.valueOf(1.10);
        }
        if (price.compareTo(BigDecimal.valueOf(100)) > 0) {
            return BigDecimal.valueOf(1.15);
        }
        return BigDecimal.valueOf(1.20);
    }

}
