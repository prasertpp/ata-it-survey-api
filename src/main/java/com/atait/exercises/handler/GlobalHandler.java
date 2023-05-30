package com.atait.exercises.handler;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.exception.ValidationException;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.Status;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handlerException(Exception e) {
        CommonResponse response = new CommonResponse();
        Status status = new Status(StatusCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.setStatus(status);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse> handleValidation(ValidationException e) {
        CommonResponse response = new CommonResponse();

        List<String> errors =  CollectionUtils.isEmpty(e.getErrors())?
                Arrays.asList(e.getMessage()):
                e.getErrors();

        Status status = new Status(StatusCode.BAD_REQUEST,
                "invalid parameter",
                errors);
        response.setStatus(status);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}