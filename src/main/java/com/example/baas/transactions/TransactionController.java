package com.example.baas.transactions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Gets all transactions for a specific account.
     * @param accountId The ID of the account.
     * @return A list of transactions for that account.
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.findTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}