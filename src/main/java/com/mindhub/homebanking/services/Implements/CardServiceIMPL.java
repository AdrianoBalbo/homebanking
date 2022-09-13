package com.mindhub.homebanking.services.Implements;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CardServiceIMPL implements CardService {
    @Autowired
    CardRepository cardRepository;


    @Override
    public Card findCardByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
}
