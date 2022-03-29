package com.kakaobank.KakaoFriendTransfer.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Batch RestController Exception Handler */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity handleValidationExceptions(GlobalException exception) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("errCode",exception.getErrCode());
        errMap.put("errMsg",exception.getMessage());

        return new ResponseEntity(errMap, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleRequestParamExceptions(MissingServletRequestParameterException exception) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("errMsg",exception.getMessage());

        return new ResponseEntity(errMap, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleRequestParamExceptions(HttpRequestMethodNotSupportedException exception) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("errMsg",exception.getMessage());

        return new ResponseEntity(errMap, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("errMsg",exception.getMessage());

        return new ResponseEntity(errMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}