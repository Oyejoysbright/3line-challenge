package org.jpc.providers.controllers;

import javax.validation.Valid;

import org.jpc.providers.models.CreatePinRequest;
import org.jpc.providers.models.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.jpc.providers.models.ContentRequest;
import org.jpc.providers.services.SerContentCreator;
import org.jpc.tool.models.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/content-creator")
@AllArgsConstructor
public class ConContentCreator {
    private final SerContentCreator serContentCreator;
    
    @PostMapping("/no-auth/login")
    public ResponseEntity<CustomResponse> login(@Valid @RequestBody LoginRequest payload) throws Exception {
        return serContentCreator.signIn(payload);
    }

    @PostMapping("/no-auth/create-pin")
    public ResponseEntity<CustomResponse> createTransactionPin(@Valid @RequestBody CreatePinRequest request) throws Exception {
        return serContentCreator.createPin(request);
    }

    @PostMapping("/create-content")
    public ResponseEntity<CustomResponse> createContent(@Valid @RequestBody ContentRequest request) throws Exception {
        return serContentCreator.createContent(request);
    }
    
    @GetMapping("/no-auth/get-contents")
    public ResponseEntity<CustomResponse> getContents(int page, int size) {
        return serContentCreator.getContents(page, size);
    }
    
}
