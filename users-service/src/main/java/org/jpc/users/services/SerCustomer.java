package org.jpc.users.services;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

import org.jpc.jspring.core.Response;
import org.jpc.jspring.enums.MediumEnum;
import org.jpc.jspring.flutterwave.dtos.ValidateOtpPayload;
import org.jpc.users.configs.JwtTokenUtil;
import org.jpc.users.configs.Properties;
import org.jpc.users.entities.Customer;
import org.jpc.users.entities.PaymentSource;
import org.jpc.users.models.CardPaymentRequest;
import org.jpc.users.models.ChangePasswordReq;
import org.jpc.users.models.ChangePinRequest;
import org.jpc.users.models.CompleteForgotPwdReq;
import org.jpc.users.models.ConfirmForgotPwdReq;
import org.jpc.users.models.CreatePinRequest;
import org.jpc.users.models.CustomerDTO;
import org.jpc.users.models.ForgotPwdRequest;
import org.jpc.users.models.LoginRequest;
import org.jpc.users.models.OtpResponse;
import org.jpc.users.models.PaymentResponseData;
import org.jpc.users.models.SecurityQuestionReq;
import org.jpc.users.models.VerifyOtpRequest;
import org.jpc.users.repos.RepoCustomer;
import org.jpc.users.repos.RepoPaymentSource;
import org.jpc.users.utils.Helper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.models.CustomResponse;

@Service
@RequiredArgsConstructor
public class SerCustomer {
    private final RepoCustomer repoCustomer;
    private final SerJwtUserDetails serJwtUserDetails;
    private final JwtTokenUtil jwtTokenUtil;
    private final Helper helper;
    private Response processorRes = null;
    
    public ResponseEntity<CustomResponse> registerCustomer(Customer customer) throws Exception {
        Optional<Customer> existingCustomer = repoCustomer.findByEmailAddressOrPhoneNumber(customer.getEmailAddress(), customer.getPhoneNumber());
        if (existingCustomer.isPresent()) {
            return Helper.RESPONSE.alreadyExist(null);
        }
        customer.setPassword(
            BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt())
        );
        repoCustomer.save(customer);
        CustomerDTO customerDTO = new CustomerDTO(customer);
        return Helper.RESPONSE.created(null, customerDTO);
    }

    public ResponseEntity<CustomResponse> signInCustomer(LoginRequest request) throws Exception {
        String username = request.getUsername();
        String password = request.getPassword();
        Optional<Customer> existingCustomer = repoCustomer.findByEmailAddressOrPhoneNumber(username, username);
        if (existingCustomer.isEmpty()) {
            return Helper.RESPONSE.custom(true, HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_CREDENTIALS.text, null);
        }
        Customer customer = existingCustomer.get();

        if(BCrypt.checkpw(password, customer.getPassword())) {
            customer.setAccessToken(serJwtUserDetails.createJwtToken(username, password));
            customer.setRefreshToken(serJwtUserDetails.createRefreshJwtToken(username, password));
            CustomerDTO loginData = new CustomerDTO(customer);
            repoCustomer.save(customer);
            return Helper.RESPONSE.ok(ResponseMessage.LOGIN_SUCCEED.text, loginData);
        } else {
            return Helper.RESPONSE.conflict(ResponseMessage.INVALID_CREDENTIALS.text);
        }
    }

    public ResponseEntity<CustomResponse> createPin(CreatePinRequest request) {
        String username = request.getUsername();
        Optional<Customer> customer = repoCustomer.findByEmailAddressOrPhoneNumber(username, username);
        if(customer.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.USERNAME_NOT_FOUND.text, null);
        }
        if (customer.get().getPin() != null) {
            return Helper.RESPONSE.badRequest(ResponseMessage.PIN_ALREADY_CREATED.text, null);
        }
        customer.get().setPin(
            BCrypt.hashpw(request.getPin(), BCrypt.gensalt())
        );
        repoCustomer.save(customer.get());
        return Helper.RESPONSE.ok(ResponseMessage.PIN_CREATED.text, null);
    }

    public ResponseEntity<CustomResponse> changePin(ChangePinRequest request) {
        Customer customer = jwtTokenUtil.getCustomer();
        if (!BCrypt.checkpw(request.getOldPin(), customer.getPin())) {
            return Helper.RESPONSE.conflict(ResponseMessage.WRONG_PIN.text);
        }
        customer.setPin(
            BCrypt.hashpw(request.getNewPin(), BCrypt.gensalt())
        );
        repoCustomer.save(customer);
        return Helper.RESPONSE.ok(ResponseMessage.PIN_CHANGED.text, null);
    }

    public ResponseEntity<CustomResponse> refreshToken(String token) {
        try {
            // TODO: Implement refresh token
            return null;
        } catch (Exception e) {
            return Helper.RESPONSE.error(null, null);
        }
    }

    public ResponseEntity<CustomResponse> initForgotPassword(ForgotPwdRequest request) {
        String username = request.getUsername();
        Optional<Customer> customer = repoCustomer.findByEmailAddressOrPhoneNumber(username, username);
        if(customer.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.USERNAME_NOT_FOUND.text, null);
        }
        processorRes = helper.getPaymentProcessor().otpManager.sendOtp(helper.getOtpPayload(username, MediumEnum.SMS));
        if (processorRes.getHasError()) {
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }
        OtpResponse otpResponse = new OtpResponse(processorRes.parseDataToArray().get(0));
        customer.get().setResetPasswordRef(otpResponse.getReference());
        repoCustomer.save(customer.get());
        return Helper.RESPONSE.ok(processorRes.getMessage(), otpResponse);
    }

    public ResponseEntity<CustomResponse> confirmForgotPassword(ConfirmForgotPwdReq request) throws Exception {
        ValidateOtpPayload processorPayload = new ValidateOtpPayload(request.getOtp(), request.getRef());
        processorRes = helper.getPaymentProcessor().otpManager.validateOtp(processorPayload);
        if (processorRes.getHasError()) {
            return Helper.RESPONSE.badRequest(processorRes.getMessage(), null);
        }
        // Set  reset password token
        Optional<Customer> customer = repoCustomer.findByResetPasswordRef(request.getRef());
        if (customer.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.INVALID_REFERENCE.text, null);
        }
        String token = serJwtUserDetails.createOtherToken(request.getUsername());
        customer.get().setResetPasswordToken(token);
        repoCustomer.save(customer.get());
        return Helper.RESPONSE.ok(processorRes.getMessage(), Map.of("token", token));
    }

    public ResponseEntity<CustomResponse> completeForgotPassword(CompleteForgotPwdReq req) throws Exception {
        Optional<Customer> customer = repoCustomer.findByResetPasswordToken(req.getToken());
        if(customer.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.INVALID_TOKEN.text, null);
        }
        
        if (!serJwtUserDetails.validateOtherToken(req.getUsername(), req.getToken())) {
            return Helper.RESPONSE.badRequest(ResponseMessage.EXPIRED_TOKEN.text, null);            
        }

        customer.get().setPassword(
            BCrypt.hashpw(req.getNewPassword(), BCrypt.gensalt())
        );
        customer.get().setResetPasswordRef(null);
        customer.get().setResetPasswordToken(null);
        repoCustomer.save(customer.get());
        return Helper.RESPONSE.ok(ResponseMessage.PASSWORD_RESET.text, null);
    }

    public ResponseEntity<CustomResponse> changePassword(ChangePasswordReq req) {
        Customer customer = jwtTokenUtil.getCustomer();
        if (!BCrypt.checkpw(req.getOldPassword(), customer.getPassword())) {
            return Helper.RESPONSE.conflict(ResponseMessage.WRONG_PASSWORD.text);  
        }
        customer.setPassword(
            BCrypt.hashpw(req.getNewPassword(), BCrypt.gensalt())
        );
        repoCustomer.save(customer);
        return Helper.RESPONSE.ok(ResponseMessage.PASSWORD_CHANGED.text, null);
    }

    public ResponseEntity<CustomResponse> changeImagePicture(String image64) {
        Customer customer = jwtTokenUtil.getCustomer();
        customer.setImage(image64);
        repoCustomer.save(customer);
        return Helper.RESPONSE.ok(ResponseMessage.OK.text, null);
    }

}
