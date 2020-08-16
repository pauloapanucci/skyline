package com.papp.skyline.dto;

import java.math.BigDecimal;

public class TransactionDTO {
    private String cpf;
    private BigDecimal amount;

    public TransactionDTO(String cpf, BigDecimal amount) {
        this.cpf = cpf;
        this.amount = amount;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
