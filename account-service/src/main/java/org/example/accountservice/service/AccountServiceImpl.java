package org.example.accountservice.service;

import jakarta.transaction.Transactional;
import org.example.accountservice.dto.AccountDto;
import org.example.accountservice.model.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    @Override

    public AccountDto createAccount(AccountDto accountDto) {
        Account account = new Account(0L, accountDto.getBalance());
        Account savedAccount = accountRepository.save(account);

        return new AccountDto(savedAccount.getBalance());
    }


    @Override
    public AccountDto updateAccounts(Account account) {
        Optional<Account> existingAccount = accountRepository.findById(account.getId());
        if (existingAccount.isPresent()) {
            Account updatedAccount = existingAccount.get();
            updatedAccount.setBalance(account.getBalance());
            accountRepository.save(updatedAccount);

            return new AccountDto(updatedAccount.getBalance());
        } else {
            throw new RuntimeException("Account not found");
        }
    }


    @Override
    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void doTransaction(Long senderId, Long recipientId, double amount) {
        // Validate input parameters
        if (senderId == null || recipientId == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid transaction parameters.");
        }

        // Retrieve both sender and recipient accounts from the database
        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found."));

        Account recipient = accountRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found."));

        // Check if sender has enough balance
        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in sender's account.");
        }

        // Perform the transaction
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Save the updated accounts to the database
        accountRepository.save(sender);
        accountRepository.save(recipient);

        // Optionally log the transaction or trigger events (not shown here)
    }

}
