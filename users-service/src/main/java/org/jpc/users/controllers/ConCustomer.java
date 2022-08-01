package org.jpc.users.controllers;

import java.net.UnknownHostException;
import javax.validation.Valid;

import org.jpc.users.entities.Customer;
import org.jpc.users.models.ChangePasswordReq;
import org.jpc.users.models.ChangePinRequest;
import org.jpc.users.models.CompleteForgotPwdReq;
import org.jpc.users.models.ConfirmForgotPwdReq;
import org.jpc.users.models.CreatePinRequest;
import org.jpc.users.models.ForgotPwdRequest;
import org.jpc.users.models.LoginRequest;
import org.jpc.users.models.ProfileUpdateRequest;
import org.jpc.users.services.SerCustomer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.jpc.tool.enums.TransactionType;
import org.jpc.tool.models.CustomResponse;
import org.jpc.users.models.CardPaymentRequest;
import org.jpc.users.models.ContentData;
import org.jpc.users.models.SavedCardPaymentRequest;
import org.jpc.users.models.VerifyOtpRequest;
import org.jpc.users.services.SerTransactions;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class ConCustomer {
    private final SerCustomer serCustomer;
    private final SerTransactions serTransactions;

    @PostMapping("/no-auth/register")
    public ResponseEntity<CustomResponse> registerCustomer(@Valid @RequestBody Customer customer) throws Exception {
        return serCustomer.registerCustomer(customer);
    }

    @PostMapping("/no-auth/login")
    public ResponseEntity<CustomResponse> loginCustomer(@Valid @RequestBody LoginRequest request) throws Exception {
        return serCustomer.signInCustomer(request);
    }

    @PostMapping("/no-auth/create-pin")
    public ResponseEntity<CustomResponse> createTransactionPin(@Valid @RequestBody CreatePinRequest request) throws Exception {
        return serCustomer.createPin(request);
    }
    
    @PutMapping("/change-pin")
    public ResponseEntity<CustomResponse> changeTransactionPin(@Valid @RequestBody ChangePinRequest request) throws Exception {
        return serCustomer.changePin(request);
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<CustomResponse> changePassword(@Valid @RequestBody ChangePasswordReq request) throws Exception {
        return serCustomer.changePassword(request);
    }

    @PostMapping("/no-auth/init-forgot-password")
    public ResponseEntity<CustomResponse> initForgotPassword(@Valid @RequestBody ForgotPwdRequest request) throws Exception {
        return serCustomer.initForgotPassword(request);
    }

    @PostMapping("/no-auth/confirm-forgot-password")
    public ResponseEntity<CustomResponse> confirmForgotPassword(@Valid @RequestBody ConfirmForgotPwdReq request) throws Exception {
        return serCustomer.confirmForgotPassword(request);
    }

    @PostMapping("/no-auth/complete-forgot-password")
    public ResponseEntity<CustomResponse> completeForgotPassword(@Valid @RequestBody CompleteForgotPwdReq request) throws Exception {
        return serCustomer.completeForgotPassword(request);
    }

    @PutMapping("/change-dp")
    public ResponseEntity<CustomResponse> changeDP(@Valid @RequestBody ProfileUpdateRequest request) throws Exception {
        return serCustomer.changeImagePicture(request.getImage());
    }
    
    @PostMapping("/new-card")
    public ResponseEntity<CustomResponse> transactWithNewCard(@RequestBody CardPaymentRequest<ContentData> request) throws IllegalArgumentException, UnknownHostException {
        if (request.getOtherData() == null) {
            throw new IllegalArgumentException();
        }
        return serTransactions.payViaNewBankCard(request);
    }
    
    @PostMapping("/validate-otp")
    public ResponseEntity<CustomResponse> validateOTP(@RequestBody VerifyOtpRequest<ContentData> request) throws IllegalArgumentException {
        return serTransactions.validateOtp(request);
    }
    
    @PostMapping("/saved-card")
    public ResponseEntity<CustomResponse> transactWithSavedCard(@RequestBody SavedCardPaymentRequest<ContentData> request) throws IllegalArgumentException, UnknownHostException {
        if (request.getOtherData() == null) {
            throw new IllegalArgumentException();
        }
        return serTransactions.payViaSavedBankCard(request);
    }
    
    @GetMapping("/histories")
    public ResponseEntity<CustomResponse> retrieveHistories(int page, int size, TransactionType type) throws IllegalArgumentException, UnknownHostException {
        return serTransactions.retrieveHistory(page, size, type);
    }

}
