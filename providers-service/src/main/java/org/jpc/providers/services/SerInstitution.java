package org.jpc.providers.services;

import java.util.Optional;

import org.jpc.providers.configs.JwtTokenUtil;
import org.jpc.providers.entities.Institution;
import org.jpc.providers.entities.SecurityQuestion;
import org.jpc.providers.models.ChangePasswordReq;
import org.jpc.providers.models.ChangePinRequest;
import org.jpc.providers.models.CreatePinRequest;
import org.jpc.providers.models.UserDTO;
import org.jpc.providers.models.LoginRequest;
import org.jpc.providers.models.SecurityQuestionReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.jpc.providers.entities.ContentCreator;
import org.jpc.providers.repos.RepoInstitution;
import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.utils.Helper;

@Service
@RequiredArgsConstructor
public class SerInstitution {
    private final RepoInstitution repoInstitution;
    private final SerJwtUserDetails serJwtUserDetails;
    private final JwtTokenUtil jwtTokenUtil;
    
    public ResponseEntity<CustomResponse> register(Institution institution) throws Exception {
        Optional<Institution> existingInstitution = repoInstitution.findByEmailAddressOrPhoneNumber(institution.getEmailAddress(), institution.getPhoneNumber());
        if (existingInstitution.isPresent()) {
            return Helper.RESPONSE.alreadyExist(null);
        }
        institution.setPassword(
            BCrypt.hashpw(institution.getPassword(), BCrypt.gensalt())
        );
        repoInstitution.save(institution);
        UserDTO dto = new UserDTO(institution);
        return Helper.RESPONSE.created(null, dto);
    }

    public ResponseEntity<CustomResponse> signIn(LoginRequest request) throws Exception {
        String username = request.getUsername();
        String password = request.getPassword();
        Optional<Institution> existing = repoInstitution.findByEmailAddressOrPhoneNumber(username, username);
        if (existing.isEmpty()) {
            return Helper.RESPONSE.custom(true, HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_CREDENTIALS.text, null);
        }
        Institution ins = existing.get();

        if(BCrypt.checkpw(password, ins.getPassword())) {
            ins.setAccessToken(serJwtUserDetails.createJwtToken(username, password));
            ins.setRefreshToken(serJwtUserDetails.createRefreshJwtToken(username, password));
            UserDTO loginData = new UserDTO(ins);
            repoInstitution.save(ins);
            return Helper.RESPONSE.ok(ResponseMessage.LOGIN_SUCCEED.text, loginData);
        } else {
            return Helper.RESPONSE.conflict(ResponseMessage.INVALID_CREDENTIALS.text);
        }
    }

    public ResponseEntity<CustomResponse> createPin(CreatePinRequest request) {
        String username = request.getUsername();
        Optional<Institution> ins = repoInstitution.findByEmailAddressOrPhoneNumber(username, username);
        if(ins.isEmpty()) {
            return Helper.RESPONSE.badRequest(ResponseMessage.USERNAME_NOT_FOUND.text, null);
        }
        if (ins.get().getPin() != null) {
            return Helper.RESPONSE.badRequest(ResponseMessage.PIN_ALREADY_CREATED.text, null);
        }
        ins.get().setPin(
            BCrypt.hashpw(request.getPin(), BCrypt.gensalt())
        );
        repoInstitution.save(ins.get());
        return Helper.RESPONSE.ok(ResponseMessage.PIN_CREATED.text, null);
    }

    public ResponseEntity<CustomResponse> changePin(ChangePinRequest request) {
        Institution ins = jwtTokenUtil.getInstitution();
        if (!BCrypt.checkpw(request.getOldPin(), ins.getPin())) {
            return Helper.RESPONSE.conflict(ResponseMessage.WRONG_PIN.text);
        }
        ins.setPin(
            BCrypt.hashpw(request.getNewPin(), BCrypt.gensalt())
        );
        repoInstitution.save(ins);
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

    public ResponseEntity<CustomResponse> changePassword(ChangePasswordReq req) {
        Institution ins = jwtTokenUtil.getInstitution();
        if (!BCrypt.checkpw(req.getOldPassword(), ins.getPassword())) {
            return Helper.RESPONSE.conflict(ResponseMessage.WRONG_PASSWORD.text);  
        }
        ins.setPassword(
            BCrypt.hashpw(req.getNewPassword(), BCrypt.gensalt())
        );
        repoInstitution.save(ins);
        return Helper.RESPONSE.ok(ResponseMessage.PASSWORD_CHANGED.text, null);
    }

    public ResponseEntity<CustomResponse> changeImagePicture(String image64) {
        Institution ins = jwtTokenUtil.getInstitution();
        ins.setImage(image64);
        repoInstitution.save(ins);
        return Helper.RESPONSE.ok(ResponseMessage.OK.text, null);
    }

    public ResponseEntity<CustomResponse> updateSecurityQuestion(SecurityQuestionReq req) {
        Institution ins = jwtTokenUtil.getInstitution();
        if(BCrypt.checkpw(req.getPassword(), ins.getPassword())) {
            return Helper.RESPONSE.conflict(ResponseMessage.WRONG_PASSWORD.text);
        }
        if(ins.getSecurityQuestion() == null){
            ins.setSecurityQuestion(new SecurityQuestion());
        }
        ins.getSecurityQuestion().setAnswer(req.getAnswer());
        ins.getSecurityQuestion().setQuestion(req.getQuestion());
        repoInstitution.save(ins);
        return Helper.RESPONSE.ok(ResponseMessage.OK.text, null);
    }
    
    public ResponseEntity<CustomResponse> registerCreator(ContentCreator payload) {
        Institution ins = jwtTokenUtil.getInstitution();
        payload.setInstitutionId(ins.getId());
        ins.getCreators().add(payload);
        repoInstitution.save(ins);
        return Helper.RESPONSE.created(ResponseMessage.CREATED.text, ins);
    }
}
