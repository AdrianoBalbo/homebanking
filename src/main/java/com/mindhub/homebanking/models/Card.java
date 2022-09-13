package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardHolder ;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private Integer cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    private boolean cardActive = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "client_id")
    private Client client;

    public Card() {
    }

    public Card(Client client, CardType cardType, CardColor cardColor, String number, Integer cvv, LocalDate thruDate, LocalDate fromDate) {
        this.cardHolder = (client.getFirstName() + " " + client.getLastName());
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, Integer cvv, LocalDate thruDate, LocalDate fromDate) {
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, Integer cvv, LocalDate thruDate, LocalDate fromDate, Client client, boolean cardActive) {
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.client = client;
        this.cardActive = cardActive;

    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public boolean isCardActive() {
        return cardActive;
    }

    public void setCardActive(boolean cardActive) {
        this.cardActive = cardActive;
    }
}
