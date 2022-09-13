package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    public List<Account> getAllAccounts();

    public Account findById(Long id);

    public Account saveAccount(Account account);

    public Account findByNumber(String number);

}
