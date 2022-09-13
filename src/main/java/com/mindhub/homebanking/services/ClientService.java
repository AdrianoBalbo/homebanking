package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

     public List<Client> getEveryClient();

     public Client getClientById(Long id);

     public Client findClientByEmail(String email);

     public Client saveClient(Client client);

}
