package com.mindhub.homebanking.RepositoriesTests;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;


import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
public class RepositoriesTestTransaction {
    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existTransaction(){
        List<Transaction> transactions=transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existTypeTransaction(){
        List<Transaction>transactions=transactionRepository.findAll();
        assertThat(transactions,hasItem(hasProperty("description",is("Puchito suelto"))));
    }
}
