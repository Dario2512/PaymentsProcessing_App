package com.aciworldwide.aclabs22.services;

import com.aciworldwide.aclabs22.dto.TransactionDTO;
import com.aciworldwide.aclabs22.entities.AccountModel;
import com.aciworldwide.aclabs22.entities.TransactionModel;
import com.aciworldwide.aclabs22.repositories.AccountRepository;
import com.aciworldwide.aclabs22.repositories.TransactionRepository;
import com.aciworldwide.aclabs22.validators.TransactionValidator;
import com.aciworldwide.aclabs22.validators.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionValidator transactionValidator;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public ResponseEntity<?> processTransaction(TransactionDTO dto) {
        ValidationResult result = transactionValidator.validate(dto);

        // Structural failure (invalid & NOT business failure)
        if (!result.isValid() && !result.isBusinessFailure()) {
            log.error("Transaction rejected by application. Bad Request: {}", result.getMessage());
            return ResponseEntity.badRequest().body(result);
        }

        // Save all transactions (both business failures and success)
        TransactionModel transaction = new TransactionModel(dto);
        transaction.setReturnCode(result.getCode());
        TransactionModel savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction saved: {}", savedTransaction.getId());

        // Business failure case - return OK but no account update
        if (result.isBusinessFailure()) {
            log.error("Transaction failed business validation: {}", result.getMessage());
            return ResponseEntity.ok().body(result);
        }

        // Success - update account balance and counters
        AccountModel account = accountRepository.findAccountByCardNumber(dto.getCardNumber());
        updateAccount(account, dto.getAmount());

        return ResponseEntity.ok(result);
    }

    private void updateAccount(AccountModel accountModel, Double amount) {
        accountModel.setAmount(accountModel.getAmount() - amount);

        // Increment daily transactions counter and daily transactions sum
        accountModel.setDailyTx(accountModel.getDailyTx() + 1);
        accountModel.setDailyTxSum(accountModel.getDailyTxSum() + amount);
    }

    // Pagination methods
    public Page<TransactionModel> getAllTransactionsPagination(Pageable pages) {
        return transactionRepository.findAll(pages);
    }

    public Page<TransactionModel> getTransactionsPaginationOfAccount(String cardNumber, Pageable pages) {
        return transactionRepository.findAllByCardNumber(cardNumber, pages);
    }

    // List all transactions (no pagination)
    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // List transactions for an account
    public List<TransactionModel> getTransactionsOfAccount(String cardNumber) {
        return transactionRepository.findAllByCardNumber(cardNumber);
    }

    // List transactions filtered by card number and return code
    public List<TransactionModel> getTransactionsOfAccountByStatus(String cardNumber, String returnCode) {
        return transactionRepository.findAllByCardNumberAndReturnCode(cardNumber, returnCode);
    }

    // Return DTOs for anomaly detection
    public List<TransactionDTO> getAllTransactionsForAnomaly() {
        List<TransactionModel> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(tm -> {
                    TransactionDTO dto = new TransactionDTO();
                    BeanUtils.copyProperties(tm, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
