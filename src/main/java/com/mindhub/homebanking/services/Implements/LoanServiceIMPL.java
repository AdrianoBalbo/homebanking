package com.mindhub.homebanking.services.Implements;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceIMPL implements LoanService {
    @Autowired
    LoanRepository loanRepository;


    @Override
    public List<Loan> getLoan() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanByName(String name) {
        return loanRepository.findByName(name);
    }
}
