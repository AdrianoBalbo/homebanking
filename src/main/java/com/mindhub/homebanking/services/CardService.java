package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import org.springframework.http.ResponseEntity;

public interface CardService {

    public Card findCardByNumber(String cardNumber);

    public Card saveCard(Card card);
}
