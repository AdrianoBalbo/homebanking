package com.mindhub.homebanking.RepositoriesTests;

import com.mindhub.homebanking.models.Client;


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
public class RepositoriesTestClient {
    @Autowired
    ClientRepository clientRepository;


    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void existFirstnameClient(){
        List<Client> clients=clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));

    }
}
