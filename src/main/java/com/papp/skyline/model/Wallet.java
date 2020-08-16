package com.papp.skyline.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal brlBalance;
    private BigDecimal btcBalance;

    protected Wallet() {
    }

    public Wallet(BigDecimal brlBalance, BigDecimal btcBalance) {
        this.brlBalance = brlBalance;
        this.btcBalance = btcBalance;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBrlBalance() {
        return brlBalance;
    }

    public BigDecimal getBtcBalance() {
        return btcBalance;
    }

    public void setBrlBalance(BigDecimal brlBalance) {
        this.brlBalance = brlBalance;
    }

    public void setBtcBalance(BigDecimal btcBalance) {
        this.btcBalance = btcBalance;
    }
}
