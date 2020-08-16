package com.papp.skyline.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private BigDecimal amount;
    @Enumerated
    @Column(columnDefinition = "smallint")
    private TransactionType transactionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;
    private BigDecimal brlAmount;
    private Date transactionDateTime;

    protected Transaction() {
    }

    public Transaction(Builder builder) {
        this.user = builder.user;
        this.amount = builder.amount;
        this.transactionType = builder.transactionType;
        this.brlAmount = builder.brlAmount;
        this.transactionDateTime = builder.transactionDateTime;
    }

    public static class Builder {
        private final User user;
        private final BigDecimal amount;
        private final TransactionType transactionType;

        private BigDecimal brlAmount = BigDecimal.ZERO;
        private Date transactionDateTime = new Date();

        public Builder(User user, BigDecimal amount, TransactionType transactionType) {
            this.user = user;
            this.amount = amount;
            this.transactionType = transactionType;
        }

        public Builder brlAmount(BigDecimal brlAmount) {
            this.brlAmount = brlAmount;
            return this;
        }

        public Builder transactionDateTime(Date transactionDateTime) {
            this.transactionDateTime = transactionDateTime;
            return this;
        }

        public Transaction build(){
            return new Transaction(this);
        }
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBrlAmount() {
        return brlAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public long getId() {
        return id;
    }
}
