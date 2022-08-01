package org.jpc.tool.config;

import java.rmi.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.jpc.tool.enums.ResponseMessage;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.utils.Helper;
import org.jpc.tool.utils.ValidationHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Incompatible data found. Processing error response");
        Map<String, String> resData = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            FieldError error = (FieldError) err;
            resData.put(error.getField(), ValidationHelper.DESCRIPTIVE_MESSAGE(error.getDefaultMessage()));
        });
        return Helper.RESPONSE.customEX(
                true, HttpStatus.UNPROCESSABLE_ENTITY,
                ResponseMessage.BAD_DATA.text, resData, Object.class);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGeneralException(Exception ex) {
        return Helper.RESPONSE.error(ResponseMessage.ERROR.text, null);
    }

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<CustomResponse> handleUnknownHostException(UnknownHostException ex) {
        log.error(":::::::::::::::::: Unknown Host");
        return Helper.RESPONSE.error(ResponseMessage.ERROR.text, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse> handleIllegalArgException(IllegalArgumentException ex) {
        log.error(":::::::::::::::::: Invalid Arg");
        return Helper.RESPONSE.badRequest(ResponseMessage.BAD_DATA.text, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(":::::::::::::::::: Mismatched Arg");
        return Helper.RESPONSE.badRequest(ResponseMessage.BAD_DATA.text, ex.getMessage());
    }
}
