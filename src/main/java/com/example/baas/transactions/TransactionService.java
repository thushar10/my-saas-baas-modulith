package com.example.baas.transactions;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction recordTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Add this new method
    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}