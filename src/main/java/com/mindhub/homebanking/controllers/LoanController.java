package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

//    @Autowired
//    private LoanRepository loanRepository;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;


    @RequestMapping("/loans")
    public List<LoanDTO> getLoanDto() {
        return loanService.getLoan().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findByNumber(loanApplicationDTO.getDestinyAccountNumber());
        Loan loan = loanService.findLoanByName(loanApplicationDTO.getLoanName());

        if (loanApplicationDTO.getDestinyAccountNumber().isEmpty() || loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Missing data, the loan request failed. Please complete all the fields.", HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()) {
            return new ResponseEntity<>("This account is disabled, you cannot apply for a Loan with a disabled account.", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("The loan does not exist.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("You cannot request that amount of capital.", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Please select one of the options.", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("The destiny account does not exist, please verify de number and try again.", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The destiny account does not belong to you, please try again with one of your accounts.", HttpStatus.FORBIDDEN);
        }
        if (client.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan() == loan).count() > 0) {
            return new ResponseEntity<>("You actually have an active " + loan.getName() + " Loan", HttpStatus.FORBIDDEN);

        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20, loanApplicationDTO.getPayments(), client, loan);


        switch (loan.getName()) {
            case "Personal":
                switch (clientLoan.getPayments()) {
                    case 12:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.25);
                        break;
                    case 24:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.30);
                        break;
                    default:
                        break;
                }
                break;
            case "Mortgage":
                switch (clientLoan.getPayments()) {
                    case 24:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.25);
                        break;
                    case 36:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.30);
                        break;
                    case 48:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.35);
                        break;
                    case 60:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.40);
                        break;
                    default:
                        break;

                }
                break;
            case "Car":
                switch (clientLoan.getPayments()) {
                    case 12:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.25);
                        break;
                    case 24:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.30);
                        break;
                    case 36:
                        clientLoan.setAmount(loanApplicationDTO.getAmount() * 1.35);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;}

        clientLoanService.saveClientLoan(clientLoan);

    Transaction transaction = new Transaction(account, TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved.", LocalDateTime.now());
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance()+loanApplicationDTO.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan application successful",HttpStatus.ACCEPTED);


}
}
