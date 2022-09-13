package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
//    @Autowired //Generamos una instancia del repositorio. (inyeccion de dependencia)
//    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/client")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getEveryClient().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }


    @RequestMapping("/client/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.getClientById(id));
    }

    @PostMapping("/client")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data, please complete all the areas", HttpStatus.FORBIDDEN);
        }
        if (clientService.findClientByEmail(email) != null){
            return new ResponseEntity<>("Email is already in use, try with another one!", HttpStatus.FORBIDDEN);
        }
        if(!email.contains("@") || !email.contains(".com") || !email.contains("@") && !email.contains(".com")){
            return new ResponseEntity<>("Email not valid.",HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @RequestMapping("/client/current")
    public ClientDTO currentClient (Authentication authentication){
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }
}
