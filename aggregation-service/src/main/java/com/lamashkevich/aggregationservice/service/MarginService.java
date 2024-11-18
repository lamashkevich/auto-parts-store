package com.lamashkevich.aggregationservice.service;

import java.math.BigDecimal;

public interface MarginService {

    BigDecimal getPriceWithMargin(BigDecimal price);

}
