package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;

public interface ClientLoanService {

    public ClientLoan saveClientLoan(ClientLoan clientLoan);
}
