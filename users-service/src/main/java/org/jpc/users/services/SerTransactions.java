package org.jpc.users.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpc.tool.enums.TransactionMethod;
import org.jpc.tool.enums.TransactionType;
import org.jpc.tool.models.Response;
import org.jpc.tool.models.TransactionInfo;
import org.jpc.tool.utils.Helper;
import org.jpc.users.configs.JwtTokenUtil;
import org.jpc.users.configs.Properties;
import org.jpc.users.entities.TransactionHistory;
import org.jpc.users.entities.Wallet;
import org.jpc.users.models.ContentData;
import org.jpc.users.models.PaymentResponseData;
import org.jpc.users.repos.RepoTransactionHistory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;

@Service
@RequiredArgsConstructor
@Slf4j
public class SerTransactions extends TransactionOperations<ContentData> {

    private final JwtTokenUtil jwtTokenUtil;
    private final Properties properties;
    private final HttpHeaders headers = new HttpHeaders();
    private final RepoTransactionHistory repoTransactionHistory;

    @Override
    protected void addToExactAccountHistory(Wallet wallet, TransactionHistory history) {
        history.setSuperWalletPercent(properties.getSuperWalletPercent());
        history.setClientWalletPercent(properties.getClientWalletPercent());
        history.setCreatorWalletPercent(properties.getCreatorWalletPercent());
        wallet.getHistories().add(history);
    }

    @Async("asyncExecutor")
    private void sendToSuperWallet(TransactionInfo info, TransactionHistory history) throws JsonProcessingException {
        String rawResponse = Helper.REST_CONNECTOR.exchange(
                Helper.anyToString(info),
                properties.getSuperWalletUrl(),
                Helper.updateBearerToken(headers, properties.getSuperWalletSecret()),
                HttpMethod.POST).getBody();
        Response res = Helper.stringToAny(rawResponse, Response.class);
        if (!res.getHasError()) {
            history.setSuperWalletReceived(true);
            repoTransactionHistory.save(history);
            log.info("Super has received commision for " + info.getRef());
        }
        log.error("Super could not receive commision for " + info.getRef());
    }

    @Async("asyncExecutor")
    private void sendToProvidersWallet(TransactionInfo info, TransactionHistory history) throws JsonProcessingException {
        String rawResponse = Helper.REST_CONNECTOR.exchange(
                Helper.anyToString(info),
                properties.getProvidersWalletUrl(),
                Helper.updateBearerToken(headers, properties.getProvidersWalletSecret()),
                HttpMethod.POST).getBody();
        Response res = Helper.stringToAny(rawResponse, Response.class);
        if (!res.getHasError()) {
            history.setProviderWalletReceived(true);
            repoTransactionHistory.save(history);
            log.info("Provider has received commision for " + info.getRef());
        }
        log.error("Provider could not receive commision for " + info.getRef());
    }

    @Override
    protected void completed(PaymentResponseData responeData, TransactionHistory history, ContentData data) {
        TransactionInfo info = new TransactionInfo();
        double totalAmount = Double.parseDouble(responeData.getAmount().toString());
        info.setAmount(totalAmount);
        info.setContentId(data.getContentId());
        info.setCreatorAmount(
                Helper.getPercentageValue(properties.getCreatorWalletPercent(), totalAmount, true)
        );
        info.setCreatorId(data.getCreatorId());
        info.setInstitutionAmount(
                Helper.getPercentageValue(properties.getClientWalletPercent(), totalAmount, true)
        );
        info.setMethod(TransactionMethod.BANK_CARD);
        info.setRef(responeData.getTxRef().toString());
        info.setSuperAmount(
                Helper.getPercentageValue(properties.getSuperWalletPercent(), totalAmount, true)
        );
        info.setTransactionId(responeData.getTxId().toString());
        info.setType(TransactionType.CR);
        info.setUserId(jwtTokenUtil.getCustomer().getId());
        try {
            sendToProvidersWallet(info, history);
            sendToSuperWallet(info, history);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SerTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
