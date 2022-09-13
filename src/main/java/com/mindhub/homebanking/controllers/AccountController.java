package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccountsDTO(){
        return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
//    map==asocia

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }

    @PostMapping("/client/current/accounts")
    public ResponseEntity<Object> createAccount(
            @RequestParam AccountType accountType, Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        List<Account> accountList = client.getAccounts().stream().filter(activeAccount->activeAccount.isAccountActive()).collect(Collectors.toList());
        if (accountList.toArray().length>=3){
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }else {
            Random randomNumber = new Random();
            Account account = new Account("VIN-"+randomNumber.nextInt(100000000) , LocalDateTime.now(), 0.00, client, accountType);
            accountService.saveAccount(account);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        }
    }

    @PatchMapping("/client/current/accounts")
    public ResponseEntity<Object> deleteAccount(
            @RequestParam String number, Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findByNumber(number);
        if (client.getAccounts().equals(1)){
            return new ResponseEntity<>("You can't delete your account if it is the only one you have.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance()>0){
            return new ResponseEntity<>("This account has a balance, you cant delete it. Try again when your account is empty.", HttpStatus.FORBIDDEN);
        }
        if (number.isEmpty()){
            return new ResponseEntity<>("Error: Account number not found.", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This Account does nor belong to you, you can't delete it", HttpStatus.FORBIDDEN);
        }
        account.setAccountActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.ACCEPTED);
    }

}
