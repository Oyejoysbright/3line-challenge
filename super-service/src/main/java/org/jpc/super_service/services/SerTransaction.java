/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.super_service.services;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jpc.super_service.configs.Properties;
import org.jpc.super_service.entities.TransactionHistory;
import org.jpc.super_service.entities.Wallet;
import org.jpc.super_service.repos.RepoTransactionHistory;
import org.jpc.super_service.repos.RepoWallet;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.enums.TransactionType;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.models.TransactionInfo;
import org.jpc.tool.utils.Helper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Service
@RequiredArgsConstructor
public class SerTransaction {
    private final Properties properties;
    private final RepoWallet repoWallet;
    private final RepoTransactionHistory repoTransactionHistory;
    
    public ResponseEntity<CustomResponse> receiveCommission(HttpServletRequest request, TransactionInfo payload) {
        String userKey = Helper.getBearerValue(request.getHeader("Authorization"));
        if (!properties.getSecretKey().equals(userKey)) {
            return Helper.RESPONSE.accessDenied();
        }
        Wallet wallet = new Wallet();
        Optional<Wallet> existingWallet = repoWallet.findById(1);
        if(existingWallet.isPresent()) {
            wallet = existingWallet.get();
        }
        TransactionHistory history = new TransactionHistory();
        BeanUtils.copyProperties(payload, history);
        history.setType(TransactionType.CR);
        wallet.setBalance(wallet.getBalance() + payload.getAmount());
        repoTransactionHistory.save(history);
        repoWallet.save(wallet);
        return Helper.RESPONSE.ok(ResponseMessage.OK.text, null);
    }
}
