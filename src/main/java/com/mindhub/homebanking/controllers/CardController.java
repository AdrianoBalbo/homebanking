package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;


    @PostMapping("/client/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardColor cardColor, @RequestParam CardType cardType,
            Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        List<Card> cardList = client.getCards().stream().filter(activeCard -> activeCard.isCardActive()).collect(Collectors.toList());
        if (cardList.stream().filter(card -> card.getCardType().equals(cardType)).toArray().length >=3){
            return new ResponseEntity<>("You already have 3 cards of this type", HttpStatus.FORBIDDEN);
        }else {
            Random randomNumber = new Random();
            Random randomCVV = new Random();
            String cardNumber = randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000);
            while (cardService.findCardByNumber(cardNumber)!=null){
                cardNumber = randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000)+"-"+randomNumber.nextInt(10000);
            }
            Card card = new Card(client.getFirstName()+" "+client.getLastName(), cardType, cardColor, cardNumber, randomCVV.nextInt(1000),  LocalDate.now().plusYears(5),LocalDate.now(), client, true);
            cardService.saveCard(card);
            return new ResponseEntity<>("Congratulations, your card was created successfully", HttpStatus.CREATED);
        }

    }

    @PatchMapping("/client/current/cards")
    public ResponseEntity<Object> deleteCard(
            @RequestParam String number, Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        Card card = cardService.findCardByNumber(number);
        if (number.isEmpty()){
            return new ResponseEntity<>("Error, Card number not found.", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("This card does not belong to you, you cant delete it.", HttpStatus.FORBIDDEN);
        }
        card.setCardActive(false);
        cardService.saveCard(card);
        return new ResponseEntity<>("Card disabled successfully", HttpStatus.ACCEPTED);
    }

}
