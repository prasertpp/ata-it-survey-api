package com.atait.exercises.model.response;

import com.atait.exercises.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Status(StatusCode code, String message, List<String> errors) {

    public Status(StatusCode code, String message){
        this(code,message,null);
    }


}