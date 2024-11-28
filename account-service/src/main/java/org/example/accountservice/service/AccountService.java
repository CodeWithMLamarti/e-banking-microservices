package org.example.accountservice.service;

import org.example.accountservice.dto.AccountDto;
import org.example.accountservice.model.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> getAccounts();
    public Optional<Account> getAccount(Long id);
    public AccountDto createAccount(AccountDto accountDto);
    public AccountDto updateAccounts(Account account);
    public boolean deleteAccount(Long id);

    public void doTransaction(Long sender, Long id, double amount);
}
