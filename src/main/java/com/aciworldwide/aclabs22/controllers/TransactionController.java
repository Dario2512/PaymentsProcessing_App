package com.aciworldwide.aclabs22.controllers;


import com.aciworldwide.aclabs22.dto.TransactionDTO;
import com.aciworldwide.aclabs22.services.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Log4j2
@RestController
@RequestMapping(path = "/payments")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/")
    public ResponseEntity<?> processTransaction(
            @RequestBody TransactionDTO transactionDto) {

        log.info("Received transaction request: {}", transactionDto);
        return transactionService.processTransaction(transactionDto);
    }
}
