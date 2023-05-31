package com.atait.exercises.handler;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.exception.MapperErrorException;
import com.atait.exercises.exception.ValidationException;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalHandler {

    final Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handlerException(Exception e) {
        logger.error("INTERNAL SERVER ERROR : {}", e);
        CommonResponse response = new CommonResponse();
        Status status = new Status(StatusCode.INTERNAL_SERVER_ERROR, StatusCode.INTERNAL_SERVER_ERROR.toString());
        response.setStatus(status);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MapperErrorException.class})
    public ResponseEntity<CommonResponse> handlerMapperException(MapperErrorException e) {
        logger.error("MAPPER ERROR exception : {}", e);
        CommonResponse response = new CommonResponse();
        Status status = new Status(StatusCode.INTERNAL_SERVER_ERROR, StatusCode.INTERNAL_SERVER_ERROR.toString(),Arrays.asList(e.getMessage()));
        response.setStatus(status);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse> handleValidation(ValidationException e) {
        logger.error("VALIDATION EXCEPTION {}", e.getErrors());
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