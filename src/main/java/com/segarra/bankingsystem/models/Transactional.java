package com.segarra.bankingsystem.models;

import java.math.BigDecimal;

public interface Transactional {
    BigDecimal increaseAmount(BigDecimal addAmount);
    BigDecimal decreaseAmount(BigDecimal addAmount);
    String toString();
}
