package org.jpc.providers.services;


import org.jpc.providers.entities.TransactionHistory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpc.providers.configs.Properties;
import org.jpc.providers.entities.ContentCreator;
import org.jpc.providers.entities.Institution;
import org.jpc.providers.repos.RepoContentCreator;
import org.jpc.providers.repos.RepoInstitution;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.enums.TransactionType;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.models.TransactionInfo;
import org.jpc.tool.utils.Helper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class SerTransactions {
    private final Properties properties;
    private final RepoContentCreator repoContentCreator;
    private final RepoInstitution repoInstitution;

    public ResponseEntity<CustomResponse> receiveCommission(HttpServletRequest request, TransactionInfo payload) {
        String userKey = Helper.getBearerValue(request.getHeader("Authorization"));
        if (!properties.getSecretKey().equals(userKey)) {
            return Helper.RESPONSE.accessDenied();
        }
        
        TransactionHistory history = new TransactionHistory();
        BeanUtils.copyProperties(payload, history);
        history.setType(TransactionType.CR);
        
        ContentCreator creator = repoContentCreator.getById(payload.getCreatorId());
        creator.getWallet().setBalance(creator.getWallet().getBalance() + payload.getCreatorAmount());
        creator.getWallet().getHistories().add(history);
        
        Institution ins = repoInstitution.getById(creator.getInstitutionId());
        ins.getWallet().setBalance(ins.getWallet().getBalance() + payload.getInstitutionAmount());
        ins.getWallet().getHistories().add(history);
        
        repoContentCreator.save(creator);
        repoInstitution.save(ins);
        return Helper.RESPONSE.ok(ResponseMessage.OK.text, null);
    }

}
