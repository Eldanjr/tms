package com.bassure.applicantservice.customerrorhandling;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.response.ResponseCode;
import com.bassure.applicantservice.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomApiException {
    @Autowired
    ResponseCode code;

    @Autowired
    ResponseMessage message;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse apiException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ApiResponse(code.getNotValid(), null, errors);
    }

    @ExceptionHandler(SQLException.class)
    public ApiResponse sqlException(SQLException ex) {
        return new ApiResponse(code.getAlreadyExists(), null, Map.of(message.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResponse misMatch(NullPointerException me) {
        return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), me.getMessage()));

    }
}
