package com.atait.exercises.handler;

import com.atait.exercises.enums.StatusCode;
import com.atait.exercises.model.response.CommonResponse;
import com.atait.exercises.model.response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handlerException(Exception e){
        CommonResponse response = new CommonResponse();
        Status status = new Status(StatusCode.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.setStatus(status);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}