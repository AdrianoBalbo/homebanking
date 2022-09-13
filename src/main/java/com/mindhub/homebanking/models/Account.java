package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    private boolean accountActive;

    private AccountType accountType;




//    DE ACA PARA ABAJO DEBERIA FUNCIONAR
@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
private Set<Transaction> transactions = new HashSet<>();


    public Account() {
    }

    public Account(String numero, LocalDateTime fechaCreacion, double balanceo){
        this.number = numero;
        this.creationDate = fechaCreacion;
        this.balance = balanceo;
    }

    public Account(String numero, LocalDateTime fechaCreacion, double balanceo, Client client, AccountType accountType){
        this.number = numero;
        this.creationDate = fechaCreacion;
        this.balance = balanceo;
        this.client = client;
        this.accountActive = true;
        this.accountType = accountType;
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

//    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void addTransaction (Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
}
