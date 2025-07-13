package com.example.baas.accounts;

import com.example.baas.messaging.AccountEventProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountEventProducer accountEventProducer;

    public AccountService(AccountRepository accountRepository, AccountEventProducer accountEventProducer) {
        this.accountRepository = accountRepository;
        this.accountEventProducer = accountEventProducer;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account createAccount(Account account) {
        Account savedAccount = accountRepository.save(account);
        accountEventProducer.sendAccountCreatedEvent(savedAccount);
        return savedAccount;
    }
}