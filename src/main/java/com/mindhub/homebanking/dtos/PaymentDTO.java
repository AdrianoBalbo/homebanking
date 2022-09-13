package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;

public class PaymentDTO {

    private long paymentID;

    private String cardNumber;

    private Integer cvv;

    private double amount;

    private String description;

    private LocalDate thruDate;

    private String cardHolder;

    private String accountNumber;







    public PaymentDTO() {
    }

    public PaymentDTO(Card card, Transaction transaction, Account account) {
        this.cardNumber = card.getNumber();
        this.cvv = card.getCvv();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardHolder();
        this.accountNumber = account.getNumber();
    }


    public long getPaymentID() {
        return paymentID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Integer getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

}
