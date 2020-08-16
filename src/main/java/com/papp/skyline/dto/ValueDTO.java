package com.papp.skyline.dto;

import java.math.BigDecimal;

public class ValueDTO {
    private BigDecimal value;

    public ValueDTO(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
