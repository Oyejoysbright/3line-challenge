package org.jpc.providers.controllers;

import javax.validation.Valid;

import org.jpc.providers.entities.Institution;
import org.jpc.providers.models.ChangePasswordReq;
import org.jpc.providers.models.ChangePinRequest;
import org.jpc.providers.models.CreatePinRequest;
import org.jpc.providers.models.LoginRequest;
import org.jpc.providers.models.ProfileUpdateRequest;
import org.jpc.providers.models.SecurityQuestionReq;
import org.jpc.providers.services.SerInstitution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.jpc.providers.entities.ContentCreator;
import org.jpc.tool.models.CustomResponse;

@RestController
@RequestMapping("/institution")
@AllArgsConstructor
public class ConInstitution {
    private final SerInstitution serInstitution;

    @PostMapping("/no-auth/register")
    public ResponseEntity<CustomResponse> register(@Valid @RequestBody Institution payload) throws Exception {
        return serInstitution.register(payload);
    }

    @PostMapping("/no-auth/login")
    public ResponseEntity<CustomResponse> login(@Valid @RequestBody LoginRequest payload) throws Exception {
        return serInstitution.signIn(payload);
    }

    @PostMapping("/no-auth/create-pin")
    public ResponseEntity<CustomResponse> createTransactionPin(@Valid @RequestBody CreatePinRequest request) throws Exception {
        return serInstitution.createPin(request);
    }
    
    @PutMapping("/change-pin")
    public ResponseEntity<CustomResponse> changeTransactionPin(@Valid @RequestBody ChangePinRequest request) throws Exception {
        return serInstitution.changePin(request);
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<CustomResponse> changePassword(@Valid @RequestBody ChangePasswordReq request) throws Exception {
        return serInstitution.changePassword(request);
    }
    
    @PutMapping("/change-dp")
    public ResponseEntity<CustomResponse> changeDP(@Valid @RequestBody ProfileUpdateRequest request) throws Exception {
        return serInstitution.changeImagePicture(request.getImage());
    }

    @PutMapping("/change-security-qa")
    public ResponseEntity<CustomResponse> updateSecurityQA(@Valid @RequestBody SecurityQuestionReq request) throws Exception {
        return serInstitution.updateSecurityQuestion(request);
    }

    @PostMapping("/create-creator")
    public ResponseEntity<CustomResponse> createCreator(@Valid @RequestBody ContentCreator payload) throws Exception {
        return serInstitution.registerCreator(payload);
    }
}
