package org.jpc.users.services;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jpc.jspring.core.Response;
import org.jpc.jspring.flutterwave.dtos.CardPaymentPayload;
import org.jpc.jspring.flutterwave.dtos.TokenPaymentPayload;
import org.jpc.users.configs.JwtTokenUtil;
import org.jpc.users.entities.Customer;
import org.jpc.users.entities.PaymentSource;
import org.jpc.users.entities.TransactionHistory;
import org.jpc.users.entities.Wallet;
import org.jpc.users.models.CardPaymentRequest;
import org.jpc.users.models.PaymentResponseData;
import org.jpc.users.models.SavedCardPaymentRequest;
import org.jpc.users.models.VerifyOtpRequest;
import org.jpc.users.repos.RepoCustomer;
import org.jpc.users.repos.RepoPaymentSource;
import org.jpc.users.repos.RepoWallet;
import org.jpc.users.utils.Helper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.enums.TransactionMethod;
import org.jpc.tool.enums.TransactionType;
import org.jpc.tool.models.CustomResponse;
import org.jpc.users.models.TempTransactionCardData;
import org.jpc.users.repos.RepoTransactionHistory;

@Service
@SuppressWarnings("unused")
@Slf4j
public abstract class TransactionOperations<T> {
    @Autowired
    private Helper helper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RepoWallet repoWallet;
    @Autowired
    private RepoCustomer repoCustomer;
    @Autowired
    private RepoPaymentSource repoPaymentSource;
    @Autowired
    private RepoTransactionHistory repoTransactionHistory;

    private T callerData = null;
    private String customerId;
    private Wallet wallet;
    private Response processorRes = null;
    private PaymentResponseData resData = new PaymentResponseData();
    private TempTransactionCardData tempCardData = new TempTransactionCardData();

    /**
     * This help to add to the actual account under the wallet like QuickSave, VaultSave
     * 
     * @param wallet
     * @param amount - Amount of the transaction
     * @param otherData
     */
    protected void addToExactBal(Wallet wallet, Double amount, T otherData) {
        throw new UnsupportedOperationException(ResponseMessage.UNIMPLEMENTED_FEATURE.text);
    };
    
    /**
     * This help to add to the actual account under the wallet like QuickSave, VaultSave
     * 
     * @param wallet
     * @param amount - Amount of the transaction
     */
    protected void addToExactUnsettledBal(Wallet wallet, Double amount) {
        throw new UnsupportedOperationException(ResponseMessage.UNIMPLEMENTED_FEATURE.text);};
    
    /**
     * This help to remove to the actual account under the wallet like QuickSave, VaultSave
     * 
     * @param wallet
     * @param amount - Amount of the transaction
     */
    protected void removeFromExactUnsettledBal(Wallet wallet, Double amount){
        throw new UnsupportedOperationException(ResponseMessage.UNIMPLEMENTED_FEATURE.text);};
    
    /**
     * Add To Transaction history
     * 
     * @param wallet
     * @param history
     */
    protected void addToExactAccountHistory(Wallet wallet, TransactionHistory history){
        throw new UnsupportedOperationException(ResponseMessage.UNIMPLEMENTED_FEATURE.text);
    };

    protected boolean bypassTransactionHistoryCheck() {
        return false;
    }
    
    /**
     * Triggered when transaction has been completed
     */
    protected void completed(PaymentResponseData resData, TransactionHistory history, T data) {
        
    }

    private void addToUnsettledBalance(Wallet wallet, PaymentResponseData res, TransactionMethod method){
        Double amount = Double.parseDouble(res.getAmount().toString());
        //Reflect on main wallet balance
        wallet.setUnsettledBalance(wallet.getUnsettledBalance()+amount);
        // Create history of the transaction
        TransactionHistory history = new TransactionHistory(
            getCustomerId(),
            String.valueOf(res.getTxRef()),
            String.valueOf(res.getTransactionId()),
            TransactionType.CR,
            method, amount, false);
        addToExactAccountHistory(wallet, history);
        //Reflect on transaction balance e.g quick save
        addToExactUnsettledBal(wallet, amount);
    };
    
    private void removeFromUnsettledBalance(Wallet wallet, Object amt, TransactionHistory history){
        Double amount = Double.parseDouble(amt.toString());
        //Reflect on main wallet balance
        wallet.setUnsettledBalance(wallet.getUnsettledBalance()-amount);
        //Update History
        history.setCompleted(true);
        repoTransactionHistory.save(history);
        //Reflect on transaction balance e.g quick save
        removeFromExactUnsettledBal(wallet, amount);
    };

    private void addToBalance(Wallet wallet, Object amt, TransactionMethod method, TransactionHistory history){
        Double amount = Double.parseDouble(amt.toString());
        //Reflect on wallet balance
        wallet.setBalance(wallet.getBalance()+amount);
        //Update transaction history
        history.setApproved(true);
        repoTransactionHistory.save(history);
        //Reflect on transaction balance e.g quick save
        addToExactBal(wallet, amount, callerData);
    };

    private void completeTransaction(Wallet wallet, PaymentResponseData res, TransactionMethod method, TransactionHistory history, T callerData){
        removeFromUnsettledBalance(wallet, res.getAmount(), history);
        addToBalance(wallet, res.getAmount(), method, history);
    };

    public T getCallerData() {
        return this.callerData;
    }

    public String getPaymentRef() {
        return resData.getTxRef().toString();
    }

    public String getCustomerId() {
        if (customerId == null) {
            customerId = jwtTokenUtil.getCustomer().getId();
        }
        return customerId;
    }

    public ResponseEntity<CustomResponse> payViaNewBankCard(CardPaymentRequest<T> request) throws UnknownHostException{
        processorRes = helper.getPaymentProcessor().payWithCard.pay(
            helper.buildPaymentPayload(request, new CardPaymentPayload())
        );
        if (processorRes.getHasError()) {
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }
        wallet = jwtTokenUtil.getWallet();
        BeanUtils.copyProperties(processorRes.getData(), resData);
        addToUnsettledBalance(wallet, resData, TransactionMethod.BANK_CARD);
        this.tempCardData = new TempTransactionCardData(resData.getTransactionId(), request.isSaveCard());
        repoWallet.save(wallet);
        this.callerData = request.getOtherData();
        return Helper.RESPONSE.ok(processorRes.getMessage(), resData);
    };

    public ResponseEntity<CustomResponse> validateOtp(VerifyOtpRequest<T> request){
        processorRes = helper.getPaymentProcessor().payWithCard.validate(request.getFlwRef(), request.getOtp());
        BeanUtils.copyProperties(processorRes.getData(), resData);
        wallet = jwtTokenUtil.getWallet();
        if (processorRes.getHasError()) {
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }

        return verifyTransaction(resData);
    };

    public ResponseEntity<CustomResponse> verifyTransaction(PaymentResponseData paymentResData){
        ResponseEntity<CustomResponse> finalRes = null;
        Optional<TransactionHistory> history = Optional.empty();
        try {
            // Check if transaction exists
            history = repoTransactionHistory.findByRef(paymentResData.getTxRef().toString());
            if (!bypassTransactionHistoryCheck() && history.isEmpty()) {
                return Helper.RESPONSE.badRequest(ResponseMessage.INVALID_REFERENCE.text, null);
            }
            else {
                if (history.isPresent()? history.get().isCompleted() : false) {
                    return Helper.RESPONSE.conflict(ResponseMessage.TRANSACTION_COMPLETED_INITIALLY.text);                
                }
                // Confirm transaction from FLW server
                processorRes = helper.getPaymentProcessor().payWithCard.verifyTxRef(paymentResData.getTxRef().toString());
                if (processorRes.getHasError()) {
                    return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
                }
    
                // Confirm value properties and give customer value
                if (resData.getStatus().equals("successful")
                    && resData.getChargedAmount().equals(paymentResData.getAmount())
                    && resData.getCurrency().equals(paymentResData.getCurrency())) {
                    // Transaction valid
                    wallet = jwtTokenUtil.getWallet();
                    TransactionMethod method = TransactionMethod.BANK_CARD;
                    completeTransaction(wallet, resData, method, history.isPresent()? history.get() : new TransactionHistory(), callerData);
                    repoWallet.save(wallet);
                    
                    //Check if required to save bank card, if yes then save it
                    if (tempCardData != null && tempCardData.getTransactionId().equals(resData.getTxId())) {
                        if (tempCardData.isSaveCard()) {
                            Customer customer = jwtTokenUtil.getCustomer();
                            String psName = Helper.maskCard(resData.getCard().getLast4());
                            String psValue = resData.getCard().getToken();
                            Optional<PaymentSource> source = repoPaymentSource.findByNameOrValue(psName, psValue);
                            if (source.isEmpty()) {
                                customer.getPaymentSources().add(
                                    new PaymentSource(customerId, psName, psValue)
                                );
                                repoCustomer.save(customer);                            
                            }
                        }                      
                    }
                    completed(resData, history.get(), this.callerData);
                    finalRes = Helper.RESPONSE.ok(ResponseMessage.TRANSACTION_COMPLETED.text, resData);
                } else {
                    removeFromUnsettledBalance(wallet, paymentResData.getAmount(), history.get());
                    finalRes = Helper.RESPONSE.custom(true, HttpStatus.NOT_ACCEPTABLE, ResponseMessage.PAYMENT_NOT_ACCEPTABLE.text, resData);
                }
                repoWallet.save(wallet);
                return finalRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Helper.RESPONSE.error(ResponseMessage.ERROR.text, null);
        }
    };

    public ResponseEntity<CustomResponse> payViaSavedBankCard(SavedCardPaymentRequest<T> request) throws UnknownHostException {
        this.callerData = request.getOtherData();
        processorRes = helper.getPaymentProcessor().payWithCard.tokenizedPayment(
            helper.buildPaymentPayload(request, new TokenPaymentPayload())
        );
        if (processorRes.getHasError()) {
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }
        wallet = jwtTokenUtil.getWallet();
        BeanUtils.copyProperties(processorRes.getData(), resData);
        addToUnsettledBalance(wallet, resData, TransactionMethod.SAVED_CARD);
        repoWallet.save(wallet);
        return verifyTransaction(resData);
    };

    public ResponseEntity<CustomResponse> retrieveHistory(int page, int size, TransactionType type) {
        List<TransactionHistory> data = new ArrayList<>();
        if (type == null) {
            data = repoTransactionHistory.getByCustomerId(getCustomerId(), PageRequest.of(page, size)).getContent();            
        } else {
            data = repoTransactionHistory.getByCustomerIdAndType(getCustomerId(), type, PageRequest.of(page, size)).getContent();
        }
        return Helper.RESPONSE.ok(ResponseMessage.DATA_FETCHED.text, data);
    }

    @Async("asyncExecutor")
    public ResponseEntity<CustomResponse> refund(Object ref, Object amount) {
        processorRes = helper.getPaymentProcessor().payWithCard.refund(ref.toString(), amount);
        
        if (processorRes.getHasError()) {
            log.info(":::::::::::: Refund Failed");
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }
        log.info(":::::::::::: Refund Completed");
        return Helper.RESPONSE.ok(ResponseMessage.DATA_FETCHED.text, processorRes.getData());
    }
 }
