package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {

    private long loanID;

    private double amount;

    private int payments;

    private String destinyAccountNumber;

    private String loanName;


    public LoanApplicationDTO(){}

    public LoanApplicationDTO(ClientLoan clientLoan, Loan loan, Account account){
        this.loanID = loan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.destinyAccountNumber = account.getNumber();
        this.loanName = loan.getName();
    }



    public long getLoanID() {
        return loanID;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinyAccountNumber() {
        return destinyAccountNumber;
    }

    public String getLoanName() {
        return loanName;
    }
}
