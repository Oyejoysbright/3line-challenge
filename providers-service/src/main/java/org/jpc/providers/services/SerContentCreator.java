package org.jpc.providers.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.jpc.providers.configs.JwtTokenUtil;
import org.jpc.providers.entities.Content;
import org.jpc.providers.entities.ContentCreator;
import org.jpc.providers.models.ContentRequest;
import org.jpc.providers.models.CreatePinRequest;
import org.jpc.providers.models.LoginRequest;
import org.jpc.providers.models.UserDTO;
import org.jpc.providers.repos.RepoContent;
import org.jpc.providers.repos.RepoContentCreator;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.utils.Helper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class SerContentCreator {

    private final RepoContentCreator repoContentCreator;
    private final SerJwtUserDetails serJwtUserDetails;
    private final JwtTokenUtil jwtTokenUtil;
    private final RepoContent repoContent;

    public ContentCreator register(ContentCreator contentCreator) throws IllegalArgumentException {
        Optional<ContentCreator> existingContentCreator = repoContentCreator.findByEmailAddressOrPhoneNumber(contentCreator.getEmailAddress(), contentCreator.getPhoneNumber());
        if (existingContentCreator.isPresent()) {
            throw new IllegalArgumentException(ResponseMessage.ALREADY_EXIST.text);
        }
        contentCreator.setPassword(
                BCrypt.hashpw(contentCreator.getPassword(), BCrypt.gensalt())
        );
        contentCreator = repoContentCreator.save(contentCreator);
        return contentCreator;
    }

    public ResponseEntity<CustomResponse> signIn(LoginRequest request) throws Exception {
        String username = request.getUsername();
        String password = request.getPassword();
        Optional<ContentCreator> existing = repoContentCreator.findByEmailAddressOrPhoneNumber(username, username);
        if (existing.isEmpty()) {
            return Helper.RESPONSE.custom(true, HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_CREDENTIALS.text, null);
        }
        ContentCreator cc = existing.get();

        if (BCrypt.checkpw(password, cc.getPassword())) {
            cc.setAccessToken(serJwtUserDetails.createJwtToken(username, password));
            cc.setRefreshToken(serJwtUserDetails.createRefreshJwtToken(username, password));
            UserDTO loginData = new UserDTO(cc);
            repoContentCreator.save(cc);
            return Helper.RESPONSE.ok(ResponseMessage.LOGIN_SUCCEED.text, loginData);
        } else {
            return Helper.RESPONSE.conflict(ResponseMessage.INVALID_CREDENTIALS.text);
        }
    }

    public ResponseEntity<CustomResponse> createPin(CreatePinRequest request) {
        String username = request.getUsername();
        Optional<ContentCreator> cc = repoContentCreator.findByEmailAddressOrPhoneNumber(username, username);
        if (cc.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.USERNAME_NOT_FOUND.text, null);
        }
        if (cc.get().getPin() != null) {
            return Helper.RESPONSE.badRequest(ResponseMessage.PIN_ALREADY_CREATED.text, null);
        }
        cc.get().setPin(
                BCrypt.hashpw(request.getPin(), BCrypt.gensalt())
        );
        repoContentCreator.save(cc.get());
        return Helper.RESPONSE.ok(ResponseMessage.PIN_CREATED.text, null);
    }

    public ResponseEntity<CustomResponse> refreshToken(String token) {
        try {
            // TODO: Implement refresh token
            return null;
        } catch (Exception e) {
            return Helper.RESPONSE.error(null, null);
        }
    }

    public ResponseEntity<CustomResponse> createContent(ContentRequest payload) {
        ContentCreator cc = jwtTokenUtil.getCreator();
        Content content = new Content();
        BeanUtils.copyProperties(payload, content);
        content.setCreatorId(cc.getId());
        cc.getContents().add(content);
        repoContentCreator.save(cc);
        return Helper.RESPONSE.created(ResponseMessage.CREATED.text, null);
    }
    
    public ResponseEntity<CustomResponse> getContents(int page, int size) {
        List<Content> contents = repoContent.findAll(PageRequest.of(page, size)).getContent();
        return Helper.RESPONSE.ok(ResponseMessage.DATA_FETCHED.text, contents);
    }

}
