package org.jpc.tool.models;

import org.jpc.tool.enums.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unchecked")
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomResponse {
    boolean hasError = false;
    String message;
    Object data;

    private <T> ResponseEntity<T> builder(HttpStatus status, T body) {
        return ResponseEntity.status(status == null? HttpStatus.OK : status).body(body);
    }

    public ResponseEntity<CustomResponse> error(String message, Object data) {
        this.hasError = true;
        this.message = message == null? ResponseMessage.ERROR.text : message;
        this.data = data;
        return builder(HttpStatus.INTERNAL_SERVER_ERROR, this);
    }

    public ResponseEntity<CustomResponse> created(String message, Object data) {
        this.hasError = false;
        this.message = message == null? ResponseMessage.CREATED.text : message;
        this.data = data;
        return builder(HttpStatus.CREATED, this);
    }

    public ResponseEntity<CustomResponse> custom(Boolean hasError, HttpStatus status, String message, Object data) {
        this.hasError = true;
        this.message = message;
        this.data = data;
        return builder(status, this);
    }

    public <T> ResponseEntity<T> customEX(Boolean hasError, HttpStatus status, String message, Object data, Class<T> returnClass) {
        this.hasError = true;
        this.message = message;
        this.data = data;
        return builder(status, (T) this);
    }

    public ResponseEntity<CustomResponse> alreadyExist(Object data) {
        this.hasError = true;
        this.message = ResponseMessage.ALREADY_EXIST.text;
        this.data = data;
        return builder(HttpStatus.CONFLICT, this);
    }

    public ResponseEntity<CustomResponse> accessDenied() {
        this.hasError = true;
        this.message = ResponseMessage.ACCESS_DENIED.text;
        this.data = null;
        return builder(HttpStatus.BAD_REQUEST, this);
    }

    public ResponseEntity<CustomResponse> ok(String message, Object data) {
        this.hasError = false;
        this.message = message;
        this.data = data;
        return builder(null, this);
    }

    public ResponseEntity<CustomResponse> conflict(String message) {
        this.hasError = true;
        this.message = message;
        this.data = null;
        return builder(HttpStatus.CONFLICT, this);
    }

    public ResponseEntity<CustomResponse> badRequest(String message, Object data) {
        this.hasError = true;
        this.message = message;
        this.data = data;
        return builder(HttpStatus.BAD_REQUEST, this);
    }
}
