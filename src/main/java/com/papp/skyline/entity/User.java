package com.papp.skyline.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;
    private String name;
    @Column(unique = true)
    private String cpf;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    Wallet wallet;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Transaction> transactions;

    protected User() {
    }

    private User(Builder builder) {
        this.name = builder.name;
        this.cpf = builder.cpf;
        this.wallet = builder.wallet;
        this.transactions = builder.transactions;
    }

    public static class Builder {
        private final String name;
        private final String cpf;
        private final Wallet wallet;

        private List<Transaction> transactions;

        public Builder(String name, String cpf, Wallet wallet) {
            this.name = name;
            this.cpf = cpf;
            this.wallet = wallet;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
