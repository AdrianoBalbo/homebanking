package com.mindhub.homebanking.RepositoriesTests;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;


import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTestAccount {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void existAccount(){
        List<Account> accounts=accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void existNumberAccount(){
        List<Account> accounts=accountRepository.findAll();
        assertThat(accounts,hasItem(hasProperty("number", is("VIN001"))));
    }

}
