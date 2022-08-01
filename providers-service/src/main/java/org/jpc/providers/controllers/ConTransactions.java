package org.jpc.providers.controllers;

import javax.servlet.http.HttpServletRequest;

import org.jpc.providers.services.SerTransactions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.models.TransactionInfo;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class ConTransactions {
    private final SerTransactions sTransactions;

    @PostMapping("/new")
    public ResponseEntity<CustomResponse> receiveNewTransaction(HttpServletRequest req, @RequestBody TransactionInfo payload) {
        return sTransactions.receiveCommission(req, payload);
    }
}
