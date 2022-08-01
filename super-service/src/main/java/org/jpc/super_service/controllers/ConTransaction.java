/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.super_service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.jpc.super_service.services.SerTransaction;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.models.TransactionInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@RestController
@RequiredArgsConstructor
public class ConTransaction {
    private final SerTransaction serTransaction;
    
    @PostMapping("/new-transaction")
    public ResponseEntity<CustomResponse> postNewTransaction(HttpServletRequest req, @Valid @RequestBody TransactionInfo payload) {
        return serTransaction.receiveCommission(req, payload);
    }
    
}
